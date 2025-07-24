package gitlet;

import java.io.File;
import java.io.Serializable;
import gitlet.Utils.*;
import java.util.*;

public class Blob implements Serializable {
    /**
     * The content of the file
     */
    private String content;

    /**
     * The SHA-1 id of the blob object
     */
    private String UID;

    public Blob(File file) {
        this.content = Utils.readContentsAsString(file);
        this.UID = Utils.sha1(content);
    }

    public String getContent() {
        return content;
    }

    public String getUID() {
        return UID;
    }
}
