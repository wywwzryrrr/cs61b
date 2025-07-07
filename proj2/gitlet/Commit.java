package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    private TreeMap<String, String> blob;
    /**
     * The SHA-1 id of this Commit.
     */
    private String UID;

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        this.timestamp = generateTimestamp();
        this.blob = new TreeMap<>();
        this.UID = generateUID();
    }

    public String getMessage() {
        return message;
    }

    public String getUID() {
        return UID;
    }

    private String generateTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if (parent == null) {
            return "THU JAN 1 8:00:00 1970 +0800";
        }
        return sdf.format(new Date());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getParent() {
        return parent;
    }

    public TreeMap<String, String> getBlob() {
        return new TreeMap<>(blob);
    }

    public void setBlob(TreeMap<String, String> blob) {
        this.blob = blob;
    }

    public String generateUID() {
        TreeMap<String, String> sortedBlob = new TreeMap<>(blob);
        String blobString = sortedBlob.toString();
        this.UID = Utils.sha1(message, timestamp, parent != null ? parent : "", blobString);
        return this.UID;
    }
    /** The message of this Commit. */
}
