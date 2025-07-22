package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

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
        for (File dir : dirs) {dir.mkdirs();}
        // Initiate commit
        Commit initCommit = new Commit("initial commit", null, null);
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
        Commit newCommit = new Commit(message, parentCommit.getUID(), null);
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
     * Adds a copy of the file as it currently exists to the staging area
     * Staging an already-staged file overwrites the previous entry
     * in the staging area with the new contents.
     * The staging area should be somewhere in .gitlet.
     * If the current working version of the file
     * is identical to the version in the current commit,
     * do not stage it to be added, and remove it
     * from the staging area if it is already there
     * (as can happen when a file is changed, added,
     * and then changed back to its original version).
     * The file will no longer be staged for removal
     * @param filename
     */
    @Override
    @SuppressWarnings("unchecked")
    public void add(String filename) {
        // 1. 检查文件是否存在于工作目录
        File inFile = Utils.join(CWD, filename);
        if (!inFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        // 2. 读取暂存区状态
        TreeMap<String, String> addMap = readAddMap();
        TreeMap<String, String> removeMap = readRemoveMap();
        String inFilePath = inFile.getAbsolutePath();
        // 3. 如果文件在待删除区，先把它移除，实现 "un-remove"
        if (removeMap.containsKey(inFilePath)) {
            removeMap.remove(inFilePath);
        }
        // 4. 计算当前文件内容的 Blob UID
        Blob currentBlob = new Blob(inFile);
        String currentBlobUID = currentBlob.getUID();
        // 5. 与 HEAD Commit 的版本进行比较
        Commit headCommit = readHeadCommit();
        boolean isUnchanged = false;
        // 处理 initial commit 的情况
        if (headCommit != null) {
            String trackedBlobUID = headCommit.getBlob().get(inFilePath);
            if (currentBlobUID.equals(trackedBlobUID)) {
                isUnchanged = true;
            }
        }
        // 6. 根据比较结果更新暂存区
        if (isUnchanged) {
            // 如果文件内容和 HEAD 一致，它不应该被暂存
            // 如果它之前在 addMap 里，就移除它
            addMap.remove(inFilePath);
        } else {
            // 文件是新的，或者被修改了，暂存它
            // a. 将 blob 对象写入 .gitlet/blobs
            File blobFile = Utils.join(BLOBS_DIR, currentBlobUID);
            Utils.writeObject(blobFile, currentBlob);
            // b. 将 (路径, UID) 存入 addMap
            addMap.put(inFilePath, currentBlobUID);
        }
        // 7. 将更新后的两个 Map 写回磁盘
        Utils.writeObject(Utils.join(ADD_DIR, "addMap"), addMap);
        Utils.writeObject(Utils.join(REMOVE_DIR, "removeMap"), removeMap);
    }

    /**
     * Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit,
     * stage it for removal and remove the file from the working directory
     * if the user has not already done so
     * (do not remove it unless it is tracked in the current commit).
     * If the file is neither staged nor tracked by the head commit,
     * print the error message No reason to remove the file.
     *
     * @Usage java gitlet.Main rm [file name]
     */
    @Override
    public void rm(String filename) {
        Commit headCommit = readHeadCommit();
        boolean isTracked = checkFileExistsInCommit(filename, headCommit);
        boolean isStaged = checkFileStaged(filename);
        // If the file is neither in staging area nor tracked in headCommit
        if (!isTracked && !isStaged) {
            System.out.println("No reason to remove the file.");
            return;
        }
        if (isStaged) {
            unstageFile(filename);
        }
        if (isTracked) {
            stageForRemoval(filename, headCommit);
            deleteFileFromCWD(filename);
        }
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
            logPrint(headCommit);
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

    /**
     *  Like log, except displays information about all commits ever made.
     *  The order of the commits does not matter.
     *  Hint: there is a useful method in
     *  gitlet.Utils that will help you iterate over files within a directory.
     *
     * @Usage java gitlet.Main global-log
     */
    @Override
    public void globalLog() {
        List<String> commitList = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String commitUID : commitList) {
            Commit commit = readCommit(commitUID);
            logPrint(commit);
        }
    }

    /**
     * Prints out the ids of all commits that have the given commit message, one per line.
     * If there are multiple such commits, it prints the ids out on separate lines.
     * The commit message is a single operand;
     * to indicate a multiword message, put the operand in quotation marks,
     * as for the commit command below.
     * Hint: the hint for this command is the same as the one for global-log
     * If no such commit exists, prints the error message Found no commit with that message.
     *
     * @Usage java gitlet.Main find [commit message]
     */
    @Override
    public void find(String message) {
        List<String> commitList = Utils.plainFilenamesIn(COMMITS_DIR);
        StringBuffer sb = new StringBuffer();
        for (String commitUID : commitList) {
            Commit commit = readCommit(commitUID);
            if (commit.getMessage().equals(message)) {
                sb.append(commit.getUID() + "\n");
            }
        }
        if (sb.length() == 0) {
            System.out.println("Found no commit with that message.");
            return;
        }
        System.out.println(sb);
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

    /**
     *  Displays what branches currently exist,
     *  and marks the current branch with a *.
     *  Also displays what files have been staged for addition or removal.
     *  An example of the exact format it should follow is as follows.
     *
     * === Branches ===
     * *master
     * other-branch
     *
     * === Staged Files ===
     * wug.txt
     * wug2.txt
     *
     * === Removed Files ===
     * goodbye.txt
     *
     * === Modifications Not Staged For Commit ===
     * junk.txt (deleted)
     * wug3.txt (modified)
     *
     * === Untracked Files ===
     * random.stuff
     *
     * There is an empty line between sections,
     * and the entire status ends in an empty line as well.
     * Entries should be listed in lexicographic order,
     * using the Java string-comparison order (the asterisk doesn’t count)
     *
     * @Usage java gitlet.Main status
     */
    @Override
    public void status() {
        printBranches();
        printStagedFiles();
        printRemovedFiles();
        printModifiedNotStagedFiles();
        printUntrackedFiles();
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
        String fullCommitUID = findFullCommitUID(commitUID);
        if (fullCommitUID == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit commit = readCommit(fullCommitUID);
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
        Commit branchCommit = readBranchCommit(branchName);
        Commit currentCommit = readHeadCommit();
        // Check if files are untracked in the current branch and would be overwritten by checkout
        if (checkUntrackedFileToCheckout(branchCommit)) {
            System.out.println("There is an untracked file in the way, delete it, " +
                               "or add and commit it first.");
            return;
        }
        // Check if the branch with the branchName is the current branch
        if (checkIsCurrentBranch(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        overwriteAllFiles(branchCommit);
        clearRedundantFiles(currentCommit, branchCommit);
        clearStagingArea();
        Utils.writeContents(HEAD_FILE, "refs/heads/" + branchName);
    }

    /**
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node.
     * See the intro for an example of what happens to the head pointer after using reset.
     * The [commit id] may be abbreviated as for checkout.
     * The staging area is cleared.
     * The command is essentially checkout of an arbitrary commit
     * that also changes the current branch head.
     * If no commit with the given id exists, print
     * 'No commit with that id exists. '
     * If a working file is untracked in the current branch
     * and would be overwritten by the reset, print
     * 'There is an untracked file in the way; delete it, or add and commit it first.'
     * @param commitUID
     */
    @Override
    public void reset(String commitUID) {
        String fullCommitUID = findFullCommitUID(commitUID);
        if (fullCommitUID == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit branchCommit = readCommit(fullCommitUID);
        Commit headCommit = readHeadCommit();
        if (checkUntrackedFileToCheckout(branchCommit)) {
            System.out.println("There is an untracked file in the way; " +
                               "delete it, or add and commit it first.");
            return;
        }
        overwriteAllFiles(branchCommit);
        clearRedundantFiles(headCommit, branchCommit);
        clearStagingArea();
        Utils.writeContents(MASTER_FILE, fullCommitUID);
    }

    @Override
    public void merge(String branchName) {
        if (!checkStagingAreaIsEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (!checkBranchExist(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        Commit branchCommit = readBranchCommit(branchName);
        Commit headCommit = readHeadCommit();
        Commit splitPoint = findSplitPoint(headCommit, branchCommit);
        if (checkIsCurrentBranch(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        if (checkUntrackedFileToCheckout(branchCommit)) {
            System.out.println("There is an untracked file in the way; " +
                               "delete it, or add and commit it first.");
            return;
        }
        // if the split point is the same commit as the given branch
        if (splitPoint.getUID().equals(branchCommit.getUID())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        // If the split point is the current branch,
        // then the effect is to checkout the given branch
        if (splitPoint.getUID().equals(headCommit.getUID())) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        // All the filePaths shown in three commits
        HashSet<String> allFilePaths = filePathsInCommits(branchCommit, splitPoint, headCommit);
        // Compare the blobUIDs of all the files in each commit
        boolean mergeConflict = false;
        for (String filePath : allFilePaths) {
            String headCommitBlobUID = getBlobUID(headCommit, filePath);
            String branchCommitBlobUID = getBlobUID(branchCommit, filePath);
            String splitPointBlobUID = getBlobUID(splitPoint, filePath);
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            if (case1(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                mergeCheckoutStage(branchCommit.getUID(), fileName);
            } else if (case2(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                continue;
            } else if (case3Part1(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                continue;
            } else if (case3Part2(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                mergeConflict = true;
                recordMergeConflict(fileName, headCommitBlobUID, branchCommitBlobUID);
                add(fileName);
            } else if (case4(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                continue;
            } else if (case5(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                mergeCheckoutStage(branchCommit.getUID(), fileName);
            } else if (case6(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                mergeRemoveUntrack(fileName);
            } else if (case7(headCommitBlobUID, branchCommitBlobUID, splitPointBlobUID)) {
                mergeRemoveUntrack(fileName);
            }
        }
        if (mergeConflict) {
            System.out.println("Encountered a merge conflict.");
            return;
        }
        String mergeCommitMessage = "Merged " + branchName + " into " + getCurrentBranchName() + ".";
        Commit mergeCommit = new Commit(mergeCommitMessage, headCommit.getUID(), branchCommit.getUID());
        // update the mergeCommit blob
        TreeMap<String, String> finalBlobMap = buildMergeMapFromHead(headCommit);
        mergeCommit.setBlob(finalBlobMap);
        saveCommit(mergeCommit);
        updateCurrentBranch(getCurrentBranchName(), mergeCommit.getUID());
        clearStagingArea();
    }
}
