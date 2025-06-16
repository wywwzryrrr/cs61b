package gitlet;

import java.io.File;

public class Commands implements CommandsInterface {

    @Override
    public void init() {
        File initDir = new File(System.getProperty("user.dir"));
        Commit initial = new Commit("initial commit", null);
        // TODO: initial a master branch and have it point to initial commit
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
