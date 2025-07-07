package gitlet;

import java.io.File;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author zng.xee
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * representation of a gitlet repo
     * CWD (dir)
     * └── .gitlet/ (dir)
     *     ├── HEAD (file)
     *     ├── refs/ (dir)
     *     │   └── heads/ (dir)
     *     │       └── master (file)
     *     ├── blobs/ (dir)
     *     ├── commits/ (dir)
     *     └── stage/ (dir)
     *         ├── add (dir)
     *         └── remove (dir)
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The references' directory
     */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /**
     * The blobs directory
     */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /**
     * The HEAD pointer
     */
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    /**
     * The stages directory
     */
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");
    /**
     * The commits file
     */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /**
     * The heads directory
     * */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    /**
     * The master file
     */
    public static final File MASTER_FILE = join(HEADS_DIR, "master");
    /**
     * The add directory and remove dir
     */
    public static final File ADD_DIR = join(STAGE_DIR, "add");
    public static final File REMOVE_DIR = join(STAGE_DIR, "remove");
}
