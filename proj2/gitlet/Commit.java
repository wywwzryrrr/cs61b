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
    //Pointer that tracks the commit
    private Commit parent;

    public Commit(String message, Commit parent) {
        this.message = message;
        this.parent = parent;
        if (parent == null) {
            this.timestamp = "00:00:00 UTC, Thursday, 1 January 1970";
        }
        this.timestamp = getTimestamp();
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        Date timeStamp = new Date();
        return timeStamp.toString();
    }

    public Commit getParent() {
        return parent;
    }

    /** The message of this Commit. */

    /* TODO: fill in the rest of this class. */
}
