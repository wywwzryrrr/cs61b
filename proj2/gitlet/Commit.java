package gitlet;

// TODO: any imports you need here
import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private String message;
    private String timestamp;
    /**
     * Pointer that tracks the commit
     */
    private String parent;
    /**
     * The snapshots of files of this commit.
     * <p>
     * The keys are files in CWD with absolute path.
     * <p>
     * The values are blobs in BLOB_DIR/shortCommitUid
     */
    private HashMap<String, String> blob;
    /**
     * The SHA-1 id of this Commit.
     */
    private String UID;

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        this.timestamp = (parent == null) ? "00:00:00 UTC, Thursday, 1 January 1970" : new Date().toString();
        this.blob = new HashMap<>();
        this.UID = Utils.sha1(message, timestamp, parent == null ? "" : parent);
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getParent() {
        return parent;
    }

    public HashMap<String, String> getBlob() {
        return blob;
    }

    /** The message of this Commit. */

    /* TODO: fill in the rest of this class. */
}
