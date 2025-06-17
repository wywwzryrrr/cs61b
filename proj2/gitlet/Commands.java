package gitlet;

import java.io.*;
import java.util.List;

import static gitlet.Repository.*;

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
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        List<File> dirs = List.of(GITLET_DIR, BLOBS_DIR, HEADS_DIR, STAGES_DIR, COMMITS_DIR);
        for (File dir : dirs) {
            dir.mkdirs();
        }
        Commit initCommit = new Commit("initial commit", null);
        String UID = Utils.sha1(initCommit);
        Utils.writeContents(MASTER_FILE, UID);
        Utils.writeContents(HEAD_FILE, "refs/heads/master");
        File commitsFile = Utils.join(COMMITS_DIR, UID);
        Utils.writeObject(commitsFile, initCommit);
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
