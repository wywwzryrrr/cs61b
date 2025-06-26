package gitlet;

import java.io.*;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Commit.*;

public class Commands implements CommandsInterface, Serializable {

    /**
     * Command 'init' initialize `.gitlet`
     * to initialize gitlet repository
     */
    @Override
    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        // Create all dirs
        List<File> dirs = List.of(GITLET_DIR, BLOBS_DIR, HEADS_DIR, STAGE_DIR,
                                  COMMITS_DIR, STAGE_DIR, ADD_DIR, REMOVE_DIR);
        for (File dir : dirs) {
            dir.mkdirs();
        }
        // Initiate commit
        Commit initCommit = new Commit("initial commit", null);
        String UID = initCommit.getUID();
        Utils.writeContents(MASTER_FILE, UID);
        Utils.writeContents(HEAD_FILE, "refs/heads/master");
        // Store the init commit
        File commitsFile = Utils.join(COMMITS_DIR, UID);
        Utils.writeObject(commitsFile, initCommit);
        // Create metadata of add and remove dirs
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        Utils.writeObject(addMapFile, new HashMap<String, String>());
        Utils.writeObject(removeMapFile, new HashMap<String, String>());
    }

    /**
     * Command 'commit + message
     * to make a commit
     * @param message
     */
    @Override
    public void commit(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }
        Commit parentCommit = readHeadCommit();
        if (parentCommit == null) {
            return;
        }
        Commit newCommit = new Commit(message, parentCommit.getUID());
        HashMap<String, String> addMap = readAddMap();
        HashMap<String, String> removeMap = readRemoveMap();
        if (addMap == null && removeMap == null) {
            System.out.println("No changes added to the commit.");
            return;
        }
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        if (addMap != null) {
            addMap.put(addMapFile.getAbsolutePath(), Utils.sha1(Utils.readContentsAsString(addMapFile)));
        }
        if (removeMap != null) {
            removeMap.put(removeMapFile.getAbsolutePath(), Utils.sha1(Utils.readContentsAsString(removeMapFile)));
        }
        newCommit.setBlob(addMap);
        newCommit.setBlob(removeMap);
        addMap.clear();
        removeMap.clear();
    }

    /**
     * Return the HashMap in the add staging area
     * @return
     */
    private HashMap<String, String> readAddMap() {
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        if (!addMapFile.exists()) {
            return null;
        }
        return Utils.readObject(addMapFile, HashMap.class);
    }

    /**
     * Return the HashMap in the remove staging area
     * @return
     */
    private HashMap<String, String> readRemoveMap() {
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        if (!removeMapFile.exists()) {
            return null;
        }
        return Utils.readObject(removeMapFile, HashMap.class);
    }

    /**
     * Command 'add + fileName'.
     * to add file to staging for addition
     * @param filename
     */
    @Override
    public void add(String filename) {
        if (filename == null || filename.isEmpty()) {
            System.out.println("File does not exist.");
            return;
        }
        File inFile = Utils.join(CWD, filename);
        if (!inFile.exists() || inFile.isDirectory()) {
            System.out.println("File does not exist.");
            return;
        }
        // Create Blob object
        Blob blob = new Blob(inFile);
        String blobUID = blob.getUID();
        // Store the blob to BLOB_DIR
        File blobFile = Utils.join(BLOBS_DIR, blobUID);
        Utils.writeObject(blobFile, blob);
        // Add the blobFile to the staging area
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        if (!addMapFile.exists() && !removeMapFile.exists()) {
            return;
        }
        // 反序列化为hashmap然后进行更新
        HashMap<String, String> updatedAddMap = Utils.readObject(addMapFile, HashMap.class);
        HashMap<String, String> updatedRemoveMap = Utils.readObject(removeMapFile, HashMap.class);
        // 用绝对路径作为key
        String absolutePath = inFile.getAbsolutePath();
        // 获取headCommit及其blob
        Commit headCommit = readHeadCommit();
        if (headCommit == null) {
            return;
        }
        HashMap<String, String> headBlobMap = Utils.readObject(addMapFile, HashMap.class);
        // 检查文件是否在 HEAD Commit 中，且版本是否一致
        if (headBlobMap.containsKey(absolutePath) && headBlobMap.get(absolutePath).equals(blobUID)) {
            // 若一致则移除
            if (updatedAddMap.containsKey(absolutePath)) {
                updatedAddMap.remove(absolutePath);
            }
            if (updatedRemoveMap.containsKey(absolutePath)) {
                updatedRemoveMap.remove(absolutePath);
            }
        } else {
            updatedAddMap.put(absolutePath, blobUID);
            // 若文件在stagedRemove中则将其删除
            if (updatedRemoveMap.containsKey(absolutePath)) {
                updatedRemoveMap.remove(absolutePath);
            }
        }
        // 将更新过后的hashmap写入
        Utils.writeObject(addMapFile, updatedAddMap);
        Utils.writeObject(addMapFile, updatedAddMap);
    }

    /**
     * Return the latest commit as a Commit object
     * @return
     */
    private Commit readHeadCommit() {
        String headPath = Utils.readContentsAsString(HEAD_FILE); // "refs/heads/master"
        File branchFile = Utils.join(GITLET_DIR, headPath); // .gitlet/refs/heads/master 拼接路径找到commitUID
        String commitUID = Utils.readContentsAsString(branchFile); // 读取commitUID
        File commitFIle = Utils.join(COMMITS_DIR, commitUID); // 找到commit对象
        if (!commitFIle.exists()) {
            return null;
        }
        return Utils.readObject(commitFIle, Commit.class); //反序列化为java对象
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
