package gitlet;

import java.io.*;

public class Commands implements CommandsInterface, Serializable {

    //Creates a new Gitlet version-control system in the current directory.
    // This system will automatically start with one commit:
    // a commit that contains no files and has the commit message initial commit
    // (just like that, with no punctuation). It will have a single branch: master,
    // which initially points to this initial commit, and master will be the current branch.
    // The timestamp for this initial commit will be
    // 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose for dates
    // (this is called “The (Unix) Epoch”, represented internally by the time 0.)
    // Since the initial commit in all repositories created by Gitlet will have exactly the same content,
    // it follows that all repositories will automatically share this commit (they will all have the same UID)
    // and all commits in all repositories will trace back to it.
    // If there is already a Gitlet version-control system in the current directory, it should abort.
    // It should NOT overwrite the existing system with a new one.
    // Should print the error message
    // "A Gitlet version-control system already exists in the current directory."
    @Override
    public void init() {
        File initDir = new File(System.getProperty("user.dir"));
        File gitletDir = new File(initDir, ".gitlet");
        if (gitletDir.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        gitletDir.mkdir();
        Commit initCommit = new Commit("initial commit", null);
        String UID = Utils.sha1(initCommit);
        File blobsDir = Utils.join(gitletDir, "blobs");
        blobsDir.mkdirs();
        File headsDir = Utils.join(gitletDir, "refs", "heads");
        headsDir.mkdirs();
        File masterFile = Utils.join(headsDir, "master");
        Utils.writeContents(masterFile, UID);
        File headFile = Utils.join(gitletDir, "HEAD");
        Utils.writeContents(headFile, "refs/heads/master");
        File stagesDir = Utils.join(gitletDir, "stage");
        stagesDir.mkdirs();
        File commitsDir = Utils.join(gitletDir, "commits", UID);
        Utils.writeObject(commitsDir, initCommit);
    }

    @Override
    public void commit() {

    }

    @Override
    public void add() {

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
