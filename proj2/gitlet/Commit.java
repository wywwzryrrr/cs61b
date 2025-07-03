package gitlet;

import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author zng.xee
 */
public class Commit implements Serializable {
    /**
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
    }

    public String getMessage() {
        return message;
    }

    public String getUID() {
        return UID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getParent() {
        return parent;
    }

    public HashMap<String, String> getBlob() {
        return new HashMap<>(blob);
    }

    public void setBlob(HashMap<String, String> blob) {
        this.blob = blob;
    }

    public String generateUID() {
        HashMap<String, String> sortedBlob = new HashMap<>(blob);
        String blobString = sortedBlob.toString();
        this.UID = Utils.sha1(message, timestamp, parent != null ? parent : "", blobString);
        return this.UID;
    }
    /** The message of this Commit. */
}
