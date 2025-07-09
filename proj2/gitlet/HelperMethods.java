package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Repository.*;

public class HelperMethods {
    /**
     * Return the TreeMap in the add staging area
     * @return
     */
    @SuppressWarnings("unchecked")
    public static TreeMap<String, String> readAddMap() {
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        if (!addMapFile.exists()) {
            return new TreeMap<>();
        }
        return (TreeMap<String, String>) Utils.readObject(addMapFile, TreeMap.class);
    }

    /**
     * Return the TreeMap in the remove staging area
     * @return
     */
    @SuppressWarnings("unchecked")
    public static TreeMap<String, String> readRemoveMap() {
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        if (!removeMapFile.exists()) {
            return new TreeMap<>();
        }
        return (TreeMap<String, String>) Utils.readObject(removeMapFile, TreeMap.class);
    }

    /**
     * Check if the staging area is empty
     * @return True if the addMap and the removeMap is empty else false
     */
    public static boolean checkStagingAreaIsEmpty() {
        return (readAddMap().isEmpty() && readRemoveMap().isEmpty());
    }

    /**
     * Clear the staging area by adding new empty TreeMap
     */
    public static void clearStagingArea() {
        Utils.writeObject(Utils.join(ADD_DIR, "addMap"), new TreeMap<String, String>());
        Utils.writeObject(Utils.join(REMOVE_DIR, "removeMap"), new TreeMap<String, String>());
    }

    /**
     * Return the latest commit as a Commit object
     * @return
     */
    public static Commit readHeadCommit() {
        // "refs/heads/master"
        String headPath = Utils.readContentsAsString(HEAD_FILE);
        // .gitlet/refs/heads/master 拼接路径找到commitUID
        File branchFile = Utils.join(GITLET_DIR, headPath);
        // 读取commitUID
        String commitUID = Utils.readContentsAsString(branchFile);
        // 找到commit对象
        File commitFile = Utils.join(COMMITS_DIR, commitUID);
        if (!commitFile.exists()) {
            return null;
        }
        //反序列化为Commit对象
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Return the Commit object through the commitUID
     * @param commitUID
     * @return
     */
    public static Commit readCommit(String commitUID) {
        File commitFile = Utils.join(COMMITS_DIR, commitUID);
        if (!commitFile.exists()) {
            return null;
        }
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Return the Commit object through the given branchName
     * @param branchName
     * @return
     */
    public static Commit readBranchCommit(String branchName) {
        File branchFile = Utils.join(HEADS_DIR, branchName);
        String commitUID = Utils.readContentsAsString(branchFile);
        File commitFile = Utils.join(COMMITS_DIR, commitUID);
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Check if the branch with the BranchName exists
     * @param branchName
     * @return True if the branch with the branchName exists else false
     */
    public static boolean checkBranchExist(String branchName) {
        File branchFile = Utils.join(HEADS_DIR, branchName);
        return branchFile.exists();
    }

    /**
     * Check if the given branchName is the current branch
     * @param branchName
     * @return True if the given branchName is the current branch else false
     */
    public static boolean checkIsCurrentBranch(String branchName) {
        String branchPath = "refs/heads/" + branchName;
        String currentBranch = Utils.readContentsAsString(HEAD_FILE);
        return branchPath.equals(currentBranch);
    }

    /**
     * Check if the file of the given fileName exists in the commit of the commitUID
     * @param fileName
     * @param commit
     */
    public static boolean checkFileExistsInCommit(String fileName, Commit commit) {
        if (commit == null) {
            return false;
        }
        File Infile = Utils.join(CWD, fileName);
        if (!Infile.exists()) {
            return false;
        }
        String filePath = Infile.getAbsolutePath();
        TreeMap<String, String> commitBlobMap = commit.getBlob();
        return (commitBlobMap.containsKey(filePath));
    }

    /**
     * Overwrite the file if it exists in the given commit,
     * create one if it doesn't
     * @param fileName
     * @param commit
     */
    public static void overwriteFile(String fileName, Commit commit) {
        File inFile = Utils.join(CWD, fileName);
        String filePath = inFile.getAbsolutePath();
        TreeMap<String, String> commitBlobMap = commit.getBlob();
        // The pointer points to the content of the file in the Commit dir
        String blobUID = commitBlobMap.get(filePath);
        // Get the content of the file
        File blobFile = Utils.join(BLOBS_DIR, blobUID);
        // Deserialize the blobFile
        Blob blob = Utils.readObject(blobFile, Blob.class);
        // Overwrite the file's content if the file exists, create it if it isn't
        Utils.writeContents(inFile, blob.getContent());
    }

    /**
     * Overwrite all the files in the CWD to the files in the given commit
     * @param commit
     */
    public static void overwriteAllFiles(Commit commit) {
        TreeMap<String, String> commitBlobMap = commit.getBlob();
        for (String filePath : commitBlobMap.keySet()) {
            File file = new File(filePath);
            String fileName = file.getName();
            overwriteFile(fileName, commit);
        }
    }

    /**
     * Clear the redundant files which are tracked in the current commit
     * but are not in the given target commit when switch to another branch
     */
    public static void clearRedundantFiles(Commit currentCommit, Commit targetCommit) {
        Set<String> trackedAbsolutePaths = currentCommit.getBlob().keySet();
        Set<String> targetAbsolutePaths = targetCommit.getBlob().keySet();
        for (String filePath : trackedAbsolutePaths) {
            File file = new File(filePath);
            String fileName = file.getName();
            if (!targetAbsolutePaths.contains(filePath)) {
                Utils.restrictedDelete(fileName);
            }
        }
    }

    /**
     * Check if a working file is untracked in the current branch,
     * and would be overwritten by the checkout
     * @param branchName
     * @return
     */
    public static boolean checkUntrackedFileToCheckout(String branchName) {
        // All file names in the CWD
        List<String> fileNames = Utils.plainFilenamesIn(CWD);
        // The absolute path tracked in the current branch
        Set<String> trackedAbsolutePaths = readHeadCommit().getBlob().keySet();
        // The absolute path tracked in the target branch
        Set<String> targetBranchFilesPaths = readCommit(branchName).getBlob().keySet();
        for (String fileName : fileNames) {
            File currentFile = Utils.join(CWD, fileName);
            String absolutePath = currentFile.getAbsolutePath();
            // Files that are tracked in the target branch but are not in the current branch
            if (!trackedAbsolutePaths.contains(absolutePath) &&
                 targetBranchFilesPaths.contains(absolutePath)) {
                return true;
            }
        }
        return false;
    }
}
