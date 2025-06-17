package gitlet;

import java.io.File;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The references' directory */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The blobs directory */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** The HEAD pointer */
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    /** The stages directory */
    public static final File STAGES_DIR = join(GITLET_DIR, "stage");
    /** The commits file */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** The heads directory */
    public static final File HEADS_DIR = join(GITLET_DIR, "heads");
    /** The master file */
    public static final File MASTER_FILE = join(HEADS_DIR, "master");
}
