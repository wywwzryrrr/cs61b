package gitlet;

import java.io.*;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Commit.*;

public class Commands implements CommandsInterface, Serializable {

//     Creates a new Gitlet version-control system in the current directory.
//     This system will automatically start with one commit:
//     a commit that contains no files and has the commit message initial commit
//     (just like that, with no punctuation). It will have a single branch: master,
//     which initially points to this initial commit, and master will be the current branch.
//     The timestamp for this initial commit will be
//     00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose for dates
//     (this is called “The (Unix) Epoch”, represented internally by the time 0.)
//     Since the initial commit in all repositories created by Gitlet will have exactly the same content,
//     it follows that all repositories will automatically share this commit (they will all have the same UID)
//     and all commits in all repositories will trace back to it.
//     If there is already a Gitlet version-control system in the current directory, it should abort.
//     It should NOT overwrite the existing system with a new one.
//     Should print the error message
//     "A Gitlet version-control system already exists in the current directory."
    @Override
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        List<File> dirs = List.of(GITLET_DIR, BLOBS_DIR, HEADS_DIR, STAGE_DIR, COMMITS_DIR, STAGE_DIR);
        for (File dir : dirs) {
            dir.mkdirs();
        }
        Commit initCommit = new Commit("initial commit", null);
        String UID = initCommit.getUID();
        Utils.writeContents(MASTER_FILE, UID);
        Utils.writeContents(HEAD_FILE, "refs/heads/master");
        Utils.writeObject(ADD_FILE, new HashMap<String, String>());
        Utils.writeObject(REMOVE_FILE, new HashMap<String, String>());
        File commitsFile = Utils.join(COMMITS_DIR, UID);
        Utils.writeObject(commitsFile, initCommit);
        File stageAddDir = Utils.join(STAGE_DIR, "add");
        File stageRemoveDir = Utils.join(STAGE_DIR, "remove");
        stageRemoveDir.mkdirs();
        stageAddDir.mkdirs();
    }


    @Override
    public void commit() {

    }

//     Adds a copy of the file as it currently exists to the staging area
//     (see the description of the commit command).
//     For this reason, adding a file is also called staging the file for addition.
//     Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
//     The staging area should be somewhere in .gitlet.
//     If the current working version of the file is identical to the version in the current commit,
//     do not stage it to be added, and remove it from the staging area if it is already there
//     (as can happen when a file is changed, added, and then changed back to its original version).
//     The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
//     If the file does not exist, print the error message File does not exist. and exit without changing anything.
    @Override
    public void add(String[] args) {
        if (args == null || args.length == 0 || args[0] == null || args[0].isEmpty()) {
            System.out.println("File does not exist.");
            return;
        }
        File inFile = Utils.join(CWD, args[0]);
        System.out.println("CWD: " + CWD.getAbsolutePath());
        System.out.println("Checking file: " + inFile.getAbsolutePath());
        System.out.println("File exists: " + inFile.exists());
        if (!inFile.exists() || inFile.isDirectory()) {
            System.out.println("File does not exist.");
            return;
        }
        String filename = inFile.getName();
        String content = Utils.readContentsAsString(inFile);
        String UID = Utils.sha1(content);
        Commit headCommit = readHeadCommit();
        if (headCommit == null) {
            return;
        }
        HashMap<String, String> blob = headCommit.getBlob();
        File blobFile = Utils.join(BLOBS_DIR, UID);
        // Check if the file is in the latest commit
        if (blob.containsKey(filename) && blob.get(filename).equals(UID)) {
            File stagedFile = Utils.join(STAGE_DIR, "add", filename);
            if (Utils.restrictedDelete(stagedFile)) {
                return;
            }
        }
        if (!blobFile.exists()) {
            Utils.writeContents(blobFile, content);
        }
        File stagedFile = Utils.join(STAGE_DIR, "add", filename);
        Utils.writeContents(stagedFile, content);
        File removeStagedFile = Utils.join(STAGE_DIR, "remove", filename);
        if (removeStagedFile.exists()) {
            removeStagedFile.delete();
        }
    }

    /**
     * Return the latest commit as a Commit object
     */
    private Commit readHeadCommit() {
        String headPath = Utils.readContentsAsString(HEAD_FILE); // "refs/heads/master"
        File branchFile = Utils.join(GITLET_DIR, headPath); // .gitlet/refs/heads/master 拼接路径找到commitUID
        String commitUID = Utils.readContentsAsString(branchFile); // 读取commitUID
        File commitFIle = Utils.join(COMMITS_DIR, commitUID); // 找到commit对象
        if (!commitFIle.exists()) {
            return null;
        }
        return Utils.readObject(commitFIle, Commit.class); //反序列化为java对象
    }

    @Override
    public void rm() {

    }

    @Override
    public void log() {

    }

    @Override
    public void globalLog() {

    }

    @Override
    public void find() {

    }

    @Override
    public void branch() {

    }

    @Override
    public void rmBranch() {

    }

    @Override
    public void status() {

    }

    @Override
    public void checkout() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void merge() {

    }
}
