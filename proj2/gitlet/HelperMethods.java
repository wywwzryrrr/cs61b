package gitlet;

import java.io.File;
import java.util.*;

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
     * Check if the file with the given fileName in staged for addition
     * @param fileName
     * @return True if the file is in the addMap
     */
    public static boolean checkFileStaged(String fileName) {
        TreeMap<String, String> addMap = readAddMap();
        File inFile = Utils.join(CWD, fileName);
        String filePath = inFile.getAbsolutePath();
        return addMap.containsKey(filePath);
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
     * Check if the given commit is the current branch
     * @param commit
     * @return
     */
    public static boolean checkIsCurrentBranch(Commit commit) {
        String branPath = Utils.readContentsAsString(HEAD_FILE);
        File branchFile = new File(branPath);
        Commit branchCommit = Utils.readObject(branchFile, Commit.class);
        return Objects.equals(branchCommit.getUID(), commit.getUID());
    }

    /**
     * Get the current branchName through headCommit
     * @return
     */
    public static String getCurrentBranchName() {
        Commit headCommit = readHeadCommit();
        List<String> fileNames = Utils.plainFilenamesIn(HEADS_DIR);
        for (String fileName : fileNames) {
            File branchFile = Utils.join(HEADS_DIR, fileName);
            String commitUID = headCommit.getUID();
            if (commitUID.equals(Utils.readContentsAsString(branchFile))) {
                return fileName;
            }
        }
        return null;
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
     * Check if a working file is untracked in the given commit,
     * and would be overwritten by checkout
     * @param commit
     * @return
     */
    public static boolean checkUntrackedFileToCheckout(Commit commit) {
        // All file names in the CWD
        List<String> fileNames = Utils.plainFilenamesIn(CWD);
        // The absolute path tracked in the current commit
        Set<String> trackedAbsolutePaths = readHeadCommit().getBlob().keySet();
        // The absolute path tracked in the target commit
        Set<String> targetFilesPaths = commit.getBlob().keySet();
        for (String fileName : fileNames) {
            File currentFile = Utils.join(CWD, fileName);
            String absolutePath = currentFile.getAbsolutePath();
            // Files that are tracked in the target commit but are not in the current commit
            if (!trackedAbsolutePaths.contains(absolutePath) &&
                 targetFilesPaths.contains(absolutePath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the full sha-1 UID of a commit with the given shortUID
     * @param shortUID
     * @return
     */
    public static String findFullCommitUID(String shortUID) {
        List<String> commitUIDs = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String commitUID : commitUIDs) {
            if (commitUID.startsWith(shortUID)) {
                 return commitUID;
            }
        }
        return null;
    }

    /**
     * Display what branches currently exist,
     * and marks the current branch with a *.
     */
    public static void printBranches() {
        System.out.println("=== Branches ===");
        List<String> branches = Utils.plainFilenamesIn(HEADS_DIR);
        String headContent = Utils.readContentsAsString(HEAD_FILE);
        // Extract branch name from HEAD_FILE (e.g., "refs/heads/master" -> "master")
        String currentBranch = headContent.replace("refs/heads/", "");
        for (String branch : branches) {
            if (branch.equals(currentBranch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
    }

    /**
     * Display the files that have been staged for addition
     */
    public static void printStagedFiles() {
        System.out.println("=== Staged Files ===");
        TreeMap<String, String> addMap = readAddMap();
        for (String filePath : addMap.keySet()) {
            File addFile = new File(filePath);
            System.out.println(addFile.getName());
        }
        System.out.println();
    }

    /**
     * Display the files that have been staged for removal
     */
    public static void printRemovedFiles() {
        System.out.println("=== Removed Files ===");
        TreeMap<String, String> removeMap = readRemoveMap();
        for (String filePath : removeMap.keySet()) {
            File removeFile = new File(filePath);
            System.out.println(removeFile.getName());
        }
        System.out.println();
    }

    public static void printModifiedNotStagedFiles() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
    }

    public static void printUntrackedFiles() {
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /**
     * Add the file in the headCommit to the removeMap
     * @param fileName
     */
    public static void stageForRemoval(String fileName, Commit headCommit) {
        TreeMap<String, String> removeMap = readRemoveMap();
        for (String filePath : headCommit.getBlob().keySet()) {
            File file = new File(filePath);
            if (file.getName().equals(fileName)) {
                removeMap.put(filePath, headCommit.getBlob().get(filePath));
            }
        }
        File removeMapFile = Utils.join(REMOVE_DIR, "removeMap");
        Utils.writeObject(removeMapFile, removeMap);
    }

    /**
     * Add the file in the headCommit to the addMap
     * @param fileName
     */
    public static void stageForAddition(String fileName, Commit headCommit) {
        TreeMap<String, String> addMap = readAddMap();
        for (String filePath : headCommit.getBlob().keySet()) {
            File file = new File(filePath);
            if (file.getName().equals(fileName)) {
                addMap.put(filePath, headCommit.getBlob().get(filePath));
            }
        }
        File addMapFile = Utils.join(ADD_DIR, "addMap");
        Utils.writeObject(addMapFile, addMap);
    }

    /**
     * Delete the file of the given fileName from CWD
     * @param fileName
     */
    public static void deleteFileFromCWD(String fileName) {
        File inFile = Utils.join(CWD, fileName);
        inFile.delete();
    }

    /**
     * Delete the file of the given fileName from the addMap
     * @param fileName
     */
    public static void unstageFile(String fileName) {
        File inFile = Utils.join(CWD, fileName);
        String inFilePath = inFile.getAbsolutePath();
        TreeMap<String, String> addMap = readAddMap();
        addMap.remove(inFilePath);
        Utils.writeObject(Utils.join(ADD_DIR, "addMap"), addMap);
    }

    /**
     * Check if the file exists in the CWD
     * @param fileName
     * @return
     */
    public static boolean checkFileExistsInCWD(String fileName) {
        File inFile = Utils.join(CWD, fileName);
        return inFile.exists();
    }

    /**
     * Print the details of a commit
     * @param commit
     */
    public static void logPrint(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + commit.getUID());
        if (commit.getSecondParent() != null) {
            String parent1Short = commit.getParent().substring(0, 7);
            String parent2Short = commit.getSecondParent().substring(0, 7);
            System.out.println("Merge: " + parent1Short + " " + parent2Short);
        }
        System.out.println("Date: " + commit.getTimestamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    /**
     * Check if the commit of the given commitUID exists
     * @param commitUID
     * @return
     */
    public static boolean checkCommitExists(String commitUID) {
        List<String> commitUIDs = Utils.plainFilenamesIn(COMMITS_DIR);
        return commitUIDs.contains(commitUID);
    }

    /**
     * Find the split point commit from the branch of the given branchName
     * @return
     */
    public static Commit findSplitPoint(Commit headCommit, Commit branchCommit) {
        Queue<Commit> commitQueue = new LinkedList<>();
        // The parentUIDs of the headCommit
        HashSet<String> parentUIDs = new HashSet<>();
        // Prevent iterate repetitively
        HashSet<String> visitedUIDs = new HashSet<>();
        visitedUIDs.add(headCommit.getUID());
        commitQueue.add(headCommit);
        // First: Iterate all the parents of the headCommit
        while (!commitQueue.isEmpty()) {
            Commit currentCommit = commitQueue.poll();
            parentUIDs.add(currentCommit.getUID());
            String parent1UID = currentCommit.getParent();
            String parent2UID = currentCommit.getSecondParent();
            addParent(parent1UID, parent2UID, commitQueue, visitedUIDs);
        }
        commitQueue.clear();
        visitedUIDs.clear();
        commitQueue.add(branchCommit);
        visitedUIDs.add(branchCommit.getUID());
        // Second: Iterate the branchCommit and return the first parent found in the parentUIDs
        while (!commitQueue.isEmpty()) {
            Commit branchCurrentCommit = commitQueue.poll();
            if (parentUIDs.contains(branchCurrentCommit.getUID())) {
                return branchCurrentCommit;
            }
            String branchParent1UID = branchCurrentCommit.getParent();
            String branchParent2UID = branchCurrentCommit.getSecondParent();
            addParent(branchParent1UID, branchParent2UID, commitQueue, visitedUIDs);
        }
        return null;
    }

    /**
     * Add the parent and the second parent Commit to the commitQueue,
     * and mark its id visited
     * @param parent1UID
     * @param parent2UID
     * @param commitQueue
     * @param visitedUIDs
     */
    private static void addParent(String parent1UID, String parent2UID,
                            Queue<Commit> commitQueue, HashSet<String> visitedUIDs) {
        if (parent1UID != null && !visitedUIDs.contains(parent1UID)) {
            commitQueue.add(readCommit(parent1UID));
            visitedUIDs.add(parent1UID);
        }
        if (parent2UID != null && !visitedUIDs.contains(parent2UID)) {
            commitQueue.add(readCommit(parent2UID));
            visitedUIDs.add(parent2UID);
        }
    }

    /**
     * Put all the filePaths in the specific target commits into a HashSet
     * @param branchCommit
     * @param splitPoint
     * @param headCommit
     * @return
     */
    public static HashSet<String> filePathsInCommits(Commit branchCommit,
                                                     Commit splitPoint,
                                                     Commit headCommit) {
        HashSet<String> filePaths = new HashSet<>();
        addFilePaths(filePaths, branchCommit);
        addFilePaths(filePaths, splitPoint);
        addFilePaths(filePaths, headCommit);
        return filePaths;
    }

    /**
     * Helper method that iterate all the filePaths in the given commit
     * and add them to the HashSet if any of them wasn't added before
     * @param filePaths
     * @param commit
     * @return
     */
    private static HashSet<String> addFilePaths(HashSet<String> filePaths, Commit commit) {
        for (String filePath : commit.getBlob().keySet()) {
            if (!filePaths.contains(filePath)) {
                filePaths.add(filePath);
            }
        }
        return filePaths;
    }

    /**
     * Get the blobUID in a commit through its filePath
     * If it doesn't exist, return null
     * @param commit
     * @param filePath
     * @return
     */
    public static String getBlobUID(Commit commit, String filePath) {
        TreeMap<String, String> commitBlobMap = commit.getBlob();
        if (commitBlobMap.containsKey(filePath)) {
            return commitBlobMap.get(filePath);
        }
        return null;
    }

    /**
     * Record the content as follows:
     * <<<<<<< HEAD
     * contents of file in current branch
     * =======
     * contents of file in given branch
     * >>>>>>>
     * write the content to the file in the CWD
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     */
    public static void recordMergeConflict(String fileName, String headCommitBlobUID, String branchCommitBlobUID) {
        String headFileContent = ""; // 默认为空字符串
        // 只有当 head 分支中存在这个文件时，才去读取它的内容
        if (headCommitBlobUID != null) {
            File fileInHead = Utils.join(BLOBS_DIR, headCommitBlobUID);
            headFileContent = Utils.readObject(fileInHead, Blob.class).getContent();
        }
        String branchFileContent = ""; // 默认为空字符串
        // 只有当 branch 分支中存在这个文件时，才去读取它的内容
        if (branchCommitBlobUID != null) {
            File fileInBranch = Utils.join(BLOBS_DIR, branchCommitBlobUID);
            branchFileContent = Utils.readObject(fileInBranch, Blob.class).getContent();
        }
        String conflictContent = "<<<<<<< HEAD\n" +
                headFileContent +
                "=======\n" +
                branchFileContent +
                ">>>>>>>\n";
        Utils.writeContents(Utils.join(CWD, fileName), conflictContent);
    }

    /**
     * Any files that have been modified in the given branch since the split point,
     * but not modified in the current branch since the split point
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case1(String headCommitBlobUID,
                                String branchCommitBlobUID,
                                String splitPointBlobUID) {
        return (Objects.equals(headCommitBlobUID, splitPointBlobUID) &&
                branchCommitBlobUID != null &&
                !Objects.equals(branchCommitBlobUID, splitPointBlobUID));
    }

    /**
     * Any files that have been modified in the given branch since the split point,
     * but not modified in the current branch since the split point
     * should be changed to their versions in the given branch
     * (checked out from the commit at the front of the given branch).
     * These files should then all be automatically staged.
     * To clarify, if a file is “modified in the given branch since the split point”
     * this means the version of the file as it exists in the commit
     * at the front of the given branch has different content
     * from the version of the file at the split point.
     * @return
     */
    public static void mergeCheckoutStage(String commitUID, String fileName) {
        Commands commands = new Commands();
        commands.checkoutCommitFile(commitUID, fileName);
        commands.add(fileName);
    }

    /**
     * Any files that have been modified in the current branch
     * but not in the given branch since the split point should stay as they are.
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case2(String headCommitBlobUID,
                                String branchCommitBlobUID,
                                String splitPointBlobUID) {
        return (!Objects.equals(headCommitBlobUID, splitPointBlobUID) &&
                Objects.equals(branchCommitBlobUID, splitPointBlobUID));
    }

    /**
     * Any files that have been modified in both the current
     * and given branch in the same way are left unchanged by the merge.
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case3Part1(String headCommitBlobUID,
                                     String branchCommitBlobUID,
                                     String splitPointBlobUID) {
        return (Objects.equals(headCommitBlobUID, branchCommitBlobUID) &&
                Objects.equals(branchCommitBlobUID, splitPointBlobUID));
    }

    /**
     * Files in both branch are been modified and have different contents
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case3Part2(String headCommitBlobUID,
                                     String branchCommitBlobUID,
                                     String splitPointBlobUID) {
        return (!Objects.equals(headCommitBlobUID, branchCommitBlobUID) &&
                !Objects.equals(branchCommitBlobUID, splitPointBlobUID) &&
                !Objects.equals(headCommitBlobUID, splitPointBlobUID));
    }

    /**
     * Any files that were not present at the split point
     * and are present only in the current branch should remain as they are.
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case4(String headCommitBlobUID,
                                String branchCommitBlobUID,
                                String splitPointBlobUID) {
        return (splitPointBlobUID == null &&
                branchCommitBlobUID == null &&
                headCommitBlobUID != null);
    }

    /**
     * Any files that were not present at the split point and
     * are present only in the given branch should be checked out and staged.
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case5(String headCommitBlobUID,
                                String branchCommitBlobUID,
                                String splitPointBlobUID) {
        return (splitPointBlobUID == null &&
                headCommitBlobUID == null &&
                branchCommitBlobUID != null);
    }

    /**
     * Any files present at the split point, unmodified in the current branch,
     * and absent in the given branch should be removed (and untracked).
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case6(String headCommitBlobUID,
                                String branchCommitBlobUID,
                                String splitPointBlobUID) {
        return (splitPointBlobUID != null &&
                branchCommitBlobUID  == null &&
                Objects.equals(headCommitBlobUID, splitPointBlobUID));
    }

    public static void mergeRemoveUntrack(String fileName) {
        Commands commands = new Commands();
        commands.rm(fileName);
    }

    /**
     * Any files present at the split point, unmodified in the given branch,
     * and absent in the current branch should remain absent.
     * @param headCommitBlobUID
     * @param branchCommitBlobUID
     * @param splitPointBlobUID
     * @return
     */
    public static boolean case7(String headCommitBlobUID,
                                String branchCommitBlobUID,
                                String splitPointBlobUID) {
        return (splitPointBlobUID != null &&
                Objects.equals(splitPointBlobUID, branchCommitBlobUID) &&
                headCommitBlobUID == null);
    }

    /**
     * Update the blob of the merge commit from staging area
     * @param commit
     */
    public static TreeMap<String, String> buildMergeMapFromHead(Commit commit) {
        TreeMap<String, String> commitBlobMap = commit.getBlob();
        TreeMap<String, String> addMap = readAddMap();
        TreeMap<String, String> removeMap = readRemoveMap();
        for (String filePath : addMap.keySet()) {
            String blobUID = addMap.get(filePath);
            commitBlobMap.put(filePath, blobUID);
        }
        for (String filePath : removeMap.keySet()) {
            commitBlobMap.remove(filePath);
        }
        return commitBlobMap;
    }

    /**
     * update the current branch content with the commitUID
     * @param branchName
     * @param commitUID
     */
    public static void updateCurrentBranch(String branchName, String commitUID) {
        File branchFile = Utils.join(HEADS_DIR, branchName);
        Utils.writeContents(branchFile, commitUID);
    }

    public static void saveCommit(Commit commit) {
        String commitUID = commit.generateUID();
        File commitFile = Utils.join(COMMITS_DIR, commitUID);
        Utils.writeObject(commitFile, commit);
    }
}