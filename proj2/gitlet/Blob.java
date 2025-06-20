package gitlet;

import java.io.File;
import java.io.Serializable;
import gitlet.Utils.*;
import java.util.*;

public class Blob implements Serializable {
    /**
     * The content of the blob
     */
    private String content;

    /**
     * The SHA-1 id of the blob object
     */
    private String UID;

    public Blob(File file) {
        this.content = Utils.readContentsAsString(file);
        this.UID = getBlobName(file);
    }

    /**
     * create blob id using its file content and file name
     */
    public static String getBlobName(File file) {
        return Utils.sha1(Utils.readContentsAsString(file) + file.getName());
    }

    public String getContent() {
        return content;
    }
}
