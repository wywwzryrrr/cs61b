package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Repository.*;
import static gitlet.HelperMethods.*;

public class Commands implements CommandsInterface, Serializable {

    /**
     * Command 'init' initialize `.gitlet`
     * to initialize gitlet repository
     */
    @Override
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system " +
                               "already exists in the current directory.");
            return;
        }
        // Create all dirs
        List<File> dirs = List.of(GITLET_DIR, BLOBS_DIR, HEADS_DIR,
                                  COMMITS_DIR, STAGE_DIR, ADD_DIR, REMOVE_DIR);
        for (File dir : dirs) {
            dir.mkdirs();
        }
        // Initiate commit
        Commit initCommit = new Commit("initial commit", null);
        String UID = initCommit.generateUID();
        Utils.writeContents(MASTER_FILE, UID);
        Utils.writeContents(HEAD_FILE, "refs/heads/master");
        // Store the init commit
        File commitsFile = Utils.join(COMMITS_DIR, UID);
        Utils.writeObject(commitsFile, initCommit);
        // Create metadata of add and remove dirs
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        Utils.writeObject(addMapFile, new TreeMap<String, String>());
        Utils.writeObject(removeMapFile, new TreeMap<String, String>());
    }

    /**
     * Command 'commit + message'
     * to make a commit
     * @param message
     */
    @Override
    public void commit(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }
        // Check if the staging area is empty
        if (checkStagingAreaIsEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        // Read the staging area
        TreeMap<String, String> addMap = readAddMap();
        TreeMap<String, String> removeMap = readRemoveMap();
        // Get parent commit and copy its blob
        Commit parentCommit = readHeadCommit();
        TreeMap<String, String> newCommitFilesMap = parentCommit.getBlob();
        // Update the blob in the staging area
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            newCommitFilesMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : removeMap.entrySet()) {
            newCommitFilesMap.remove(entry.getKey());
        }
        // Create new commit object and set its blob
        Commit newCommit = new Commit(message, parentCommit.getUID());
        newCommit.setBlob(newCommitFilesMap);
        // Recalculate the UID and put it in the COMMITS_DIR
        String newCommitUID = newCommit.generateUID();
        File newCommitFile = Utils.join(COMMITS_DIR, newCommitUID);
        Utils.writeObject(newCommitFile, newCommit);
        // Update the HEAD pointer, get it point to the new commit
        String headPath = Utils.readContentsAsString(HEAD_FILE);
        File branchFile = Utils.join(GITLET_DIR, headPath);
        Utils.writeContents(branchFile, newCommitUID);
        // Clear the staging area by adding new empty TreeMap
        clearStagingArea();
    }

    /**
     * Command 'add + fileName'.
     * to add file to staging for addition
     * @param filename
     */
    @Override
    @SuppressWarnings("unchecked")
    public void add(String filename) {
        if (filename == null || filename.isEmpty()) {
            System.out.println("File does not exist.");
            return;
        }
        File inFile = Utils.join(CWD, filename);
        if (!inFile.exists() || inFile.isDirectory()) {
            System.out.println("File does not exist.");
            return;
        }
        // Create Blob object
        Blob blob = new Blob(inFile);
        String blobUID = blob.getUID();
        // Store the blob to BLOB_DIR
        File blobFile = Utils.join(BLOBS_DIR, blobUID);
        Utils.writeObject(blobFile, blob);
        // Add the blobFile to the staging area
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        if (!addMapFile.exists() && !removeMapFile.exists()) {
            return;
        }
        // 反序列化为TreeMap然后进行更新
        TreeMap<String, String> updatedAddMap = readAddMap();
        TreeMap<String, String> updatedRemoveMap = readRemoveMap();
        // 用绝对路径作为key
        String absolutePath = inFile.getAbsolutePath();
        // 获取headCommit及其blob
        Commit headCommit = readHeadCommit();
        if (headCommit == null) {
            return;
        }
        TreeMap<String, String> headBlobMap = headCommit.getBlob();
        // 检查文件是否在 HEAD Commit 中，且版本是否一致
        if (blobUID.equals(headBlobMap.get(absolutePath))) {
            // 文件内容和当前提交的版本完全一样，没必要暂存
            // 如果它在暂存区，应该被移除
            if (updatedAddMap.containsKey(absolutePath)) {
                updatedAddMap.remove(absolutePath);
            }
            // (这里也应该处理 removeMap，如果一个被标记为删除的文件被重新add，要从removeMap中移除)
        } else {
            // 文件内容有变化，或者是一个新文件，加入暂存区
            updatedAddMap.put(absolutePath, blobUID);
            if (updatedRemoveMap.containsKey(absolutePath)) {
                updatedRemoveMap.remove(absolutePath);
            }
        }
        // 将更新过后的TreeMap写入
        Utils.writeObject(addMapFile, updatedAddMap);
        Utils.writeObject(removeMapFile, updatedRemoveMap);
    }

    @Override
    public void rm() {

    }

    /**
     * ===
     * commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
     * Date: Thu Nov 9 20:00:05 2017 -0800
     * A commit message.
     *
     * ===
     * commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff
     * Date: Thu Nov 9 17:01:33 2017 -0800
     * Another commit message.
     *
     * ===
     * commit e881c9575d180a215d1a636545b8fd9abfb1d2bb
     * Date: Wed Dec 31 16:00:00 1969 -0800
     * initial commit
     */
    @Override
    public void log() {
        Commit headCommit = readHeadCommit();
        while (headCommit != null) {
            // Print the messages
            System.out.println("===");
            System.out.println("commit " + headCommit.getUID());
            System.out.println("Date: " + headCommit.getTimestamp());
            System.out.println(headCommit.getMessage());
            System.out.println();
            // Get parentUID
            String parentUID = headCommit.getParent();
            // Trace back the parent of the parent commit
            if (parentUID != null) {
                File parentFile = Utils.join(COMMITS_DIR, parentUID);
                headCommit = Utils.readObject(parentFile, Commit.class);
            } else {
                headCommit = null;
            }
        }
    }

    @Override
    public void globalLog() {

    }

    @Override
    public void find() {

    }

    /**
     * Creates a new branch with the given name,
     * and points it at the current head commit.
     * A branch is nothing more than a name for a reference
     * (a SHA-1 identifier) to a commit node.
     * This command does NOT immediately switch to
     * the newly created branch (just as in real Git).
     * Before you ever call branch,
     * your code should be running with a default branch called “master”.
     * Failure cases: If a branch with the given name already exists,
     * print the error message
     * A branch with that name already exists.
     *
     * @Usage java gitlet.Main branch [branch name]
     */
    @Override
    public void branch(String branchName) {
        if (checkBranchExist(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        Commit headCommit = readHeadCommit();
        String commitUID = headCommit.getUID();
        File newBranchFile = Utils.join(HEADS_DIR, branchName);
        Utils.writeContents(newBranchFile, commitUID);
    }

    /**
     *  Deletes the branch with the given name.
     *  This only means to delete the pointer associated with the branch;
     *  it does not mean to delete all commits that were created under the branch,
     *  or anything like that.
     *  Failure cases: If a branch with the given name does not exist, aborts.
     *  Print the error message A branch with that name does not exist.
     *  If you try to remove the branch you’re currently on, aborts,
     *  printing the error message Cannot remove the current branch.
     *
     *  @Usage java gitlet.Main rm-branch [branch name]
     */
    @Override
    public void rmBranch(String branchName) {
        if (!checkBranchExist(branchName)) {
            System.out.println("A branch with that name dose not exists.");
            return;
        }
        if (checkIsCurrentBranch(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        File branchFile = Utils.join(HEADS_DIR, branchName);
        branchFile.delete();
    }

    @Override
    public void status() {

    }

    /**
     * Takes the version of the file as it exists in the head commit
     * and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     * If the file does not exist in the previous commit, abort,
     * printing the error message
     * File does not exist in that commit.
     * Do not change the CWD.
     *
     * @Usage java gitlet.Main checkout -- [file name]
     * @param filename
     */
    @Override
    public void checkoutFile(String filename) {
        Commit headCommit = readHeadCommit();
        // Check if the file is in the Commit dir
        if (!checkFileExistsInCommit(filename, headCommit)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        overwriteFile(filename, headCommit);
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory,
     * overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     * If no commit with the given id exists,
     * print No commit with that id exists.
     * Otherwise, if the file does not exist in the given commit,
     * print the same message as for failure case 1.
     * Do not change the CWD.
     *
     * @Usage java gitlet.Main checkout [commit id] -- [file name]
     * @param commitUID
     * @param filename
     */
    @Override
    public void checkoutCommitFile(String commitUID, String filename) {
        Commit commit = readCommit(commitUID);
        if (commit == null) {
            System.out.println("No commit with that ID exists.");
            return;
        }
        if (!checkFileExistsInCommit(filename, commit)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        overwriteFile(filename, commit);
    }

    /**
     * Takes all files in the commit at the head of the given branch,
     * and puts them in the working directory,
     * overwriting the versions of the files that are already there if they exist.
     * Also, at the end of this command,
     * the given branch will now be considered the current branch (HEAD).
     * Any files that are tracked in the current branch
     * but are not present in the checked-out branch are deleted.
     * The staging area is cleared,
     * unless the checked-out branch is the current branch.
     * If no branch with that name exists, print No such branch exists.
     * If that branch is the current branch,
     * print No need to checkout the current branch.
     * If a working file is untracked in the current branch
     * and would be overwritten by the checkout, print
     * There is an untracked file in the way, delete it, or add and commit it first. and exit;
     * perform this check before doing anything else. Do not change the CWD.
     *
     * @Usage java gitlet.Main checkout [branch name]
     * @param branchName
     */
    @Override
    public void checkoutBranch(String branchName) {
        // Check if the branch with the branchName exist
        if (!checkBranchExist(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        // Check if files are untracked in the current branch and would be overwritten by checkout
        if (checkUntrackedFileToCheckout(branchName)) {
            System.out.println("There is an untracked file in the way, delete it, " +
                               "or add and commit it first.");
            return;
        }
        // Check if the branch with the branchName is the current branch
        if (checkIsCurrentBranch(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        Commit branchCommit = readBranchCommit(branchName);
        Commit currentCommit = readHeadCommit();
        overwriteAllFiles(branchCommit);
        clearRedundantFiles(currentCommit, branchCommit);
        clearStagingArea();
        Utils.writeContents(HEAD_FILE, "refs/heads/" + branchName);
    }

    @Override
    public void reset() {

    }

    @Override
    public void merge() {

    }
}
