package gitlet;

import com.sun.source.tree.Tree;

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
        String UID = initCommit.generateUID();
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
     * Command 'commit + message'
     * to make a commit
     * @param message
     */
    @Override
    public void commit(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if (readAddMap() == null && readRemoveMap() == null) {
            System.out.println("No changes added to the commit.");
            return;
        }
        // Read the staging area
        HashMap<String, String> addMap = readAddMap();
        HashMap<String, String> removeMap = readRemoveMap();
        // Get parent commit and copy its blob
        Commit parentCommit = readHeadCommit();
        HashMap<String, String> newCommitFilesMap = parentCommit.getBlob();
        // Update the blob in the staging area
        for (Map.Entry<String, String> entry : addMap.entrySet()) {
            newCommitFilesMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : removeMap.entrySet()) {
            newCommitFilesMap.remove(entry.getKey());
        }
        // Create new commit object and set its blob
        Commit newCommit = new Commit(message, parentCommit.getUID());
        newCommit.setBlob(newCommitFilesMap);
        // Recalculate the UID and put it in the COMMITS_DIR
        String newCommitUID = newCommit.generateUID();
        File newCommitFile = Utils.join(COMMITS_DIR, newCommitUID);
        Utils.writeObject(newCommitFile, newCommit);
        // Update the HEAD pointer, get it point to the new commit
        String headPath = Utils.readContentsAsString(HEAD_FILE);
        File branchFile = Utils.join(GITLET_DIR, headPath);
        Utils.writeContents(branchFile, newCommitUID);
        // Clear the staging area by adding new empty HashMap
        Utils.writeObject(Utils.join(ADD_DIR, "addMap"), new HashMap<String, String>());
        Utils.writeObject(Utils.join(REMOVE_DIR, "removeMap"), new HashMap<String, String>());
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
        HashMap<String, String> headBlobMap = headCommit.getBlob();
        // 检查文件是否在 HEAD Commit 中，且版本是否一致
        if (blobUID.equals(headBlobMap.get(absolutePath))) { // 直接比较blobUID更简洁
            // 文件内容和当前提交的版本完全一样，没必要暂存
            // 如果它在暂存区，应该被移除
            if (updatedAddMap.containsKey(absolutePath)) {
                updatedAddMap.remove(absolutePath);
            }
            // (这里也应该处理 removeMap，如果一个被标记为删除的文件被重新add，要从removeMap中移除)
        } else {
            // 文件内容有变化，或者是一个新文件，加入暂存区
            updatedAddMap.put(absolutePath, blobUID);
            if (updatedRemoveMap.containsKey(absolutePath)) {
                updatedRemoveMap.remove(absolutePath);
            }
        }
        // 将更新过后的hashmap写入
        Utils.writeObject(addMapFile, updatedAddMap);
        Utils.writeObject(removeMapFile, updatedRemoveMap);
    }

    /**
     * Return the latest commit as a Commit object
     * @return
     */
    private Commit readHeadCommit() {
        String headPath = Utils.readContentsAsString(HEAD_FILE); // "refs/heads/master"
        File branchFile = Utils.join(GITLET_DIR, headPath); // .gitlet/refs/heads/master 拼接路径找到commitUID
        String commitUID = Utils.readContentsAsString(branchFile); // 读取commitUID
        File commitFile = Utils.join(COMMITS_DIR, commitUID); // 找到commit对象
        if (!commitFile.exists()) {
            return null;
        }
        return Utils.readObject(commitFile, Commit.class); //反序列化为java对象
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
