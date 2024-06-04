package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** Commit directory */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** Objects directory */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    /** Staging directory */
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /** Current Head*/
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");

    public static final File CURR_BRANCH = join(BRANCHES_DIR, "current");

    private static StagingArea staging;



    /* TODO: fill in the rest of this class. */

    /*
    *TODO: add master and head references (using files? instance variables?)
    * What would an intance of Repositoy be like?
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        STAGING_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        try {
            HEAD.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeContents(CURR_BRANCH, "master");
        Commit initialCommit = new Commit();
        initialCommit.save();
        staging = new StagingArea();
        staging.createFile();
        staging.save();
    }

    public static void add(String fileName) {
        staging = StagingArea.load();
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Blob blob = new Blob(file);
        blob.saveBlob();
        staging.add(file, blob.getHash());
        staging.save();
    }

    public static void commit(String message) {
        staging = StagingArea.load();
        Commit newCommit = new Commit(message);
        newCommit.addCommit(staging);
        staging.save();
    }

    public static void remove(String fileName) {
        staging = StagingArea.load();
        File file = join(CWD, fileName);
        staging.remove(file);
        staging.save();
    }

    public static void log() {
        Commit current = Repository.getHead();
        while (true) {
            System.out.println(current);
            if (current.getParent() == null) {
                break;
            }
            current = readObject(join(COMMITS_DIR, current.getParent()), Commit.class);
        }
    }

    public static void globalLog() {
        List<String> files = plainFilenamesIn(COMMITS_DIR);
        for (String s : files) {
            System.out.println(readObject(join(COMMITS_DIR, s), Commit.class));
        }
    }

    public static void checkout(String fileName) {
        Commit head = getHead();
        File target = join(CWD, fileName);
        Map<File, String> headFiles = head.getFiles();
        if (!headFiles.keySet().contains(target)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        String targetHash = headFiles.get(target);
        Blob.copyBlob(targetHash, target);
    }

    public static void checkout(String fileName, String commitID) {
        List<String> files = plainFilenamesIn(COMMITS_DIR);
        if (!files.contains(join(COMMITS_DIR, commitID))) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit commit = Commit.getCommit(commitID);
        File target = join(CWD, fileName);
        Map<File, String> commitFiles = commit.getFiles();
        if (!commitFiles.keySet().contains(target)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        String targetHash = commitFiles.get(target);
        Blob.copyBlob(targetHash, target);
    }

    public static void checkoutBranch(String branchName) {
        File branchFile = join(BRANCHES_DIR, branchName);
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            return;
        }
        if (readContentsAsString(CURR_BRANCH).equals(branchName)) {
            System.out.println("No need to checkout the current branch");
            return;
        }
        Commit branchCommit = readObject(join(COMMITS_DIR, readContentsAsString(branchFile)), Commit.class);
        List<String> fileList = plainFilenamesIn(CWD);
        for (String s : fileList) {
            if (!getHead().containsFile(join(CWD, s))) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        Map<File, String> branchFiles = branchCommit.getFiles();
        for (File file : branchFiles.keySet()) {
            Blob.copyBlob(branchFiles.get(file), join(CWD, file.getName()));
        }
        for (String s : fileList) {
            File CWDFile = join(CWD, s);
            if (!branchFiles.keySet().contains(CWDFile)) {
                CWDFile.delete();
            }
        }
        writeContents(HEAD, branchCommit.getHash());
        writeContents(CURR_BRANCH, branchName);
    }

    public static void find(String message) {
        Commit current = Repository.getHead();
        while (true) {
            if (current.getMessage().equals(message)) {
                System.out.println(current.getHash());
            }
            if (current.getParent() == null) {
                break;
            }
            current = readObject(join(COMMITS_DIR, current.getParent()), Commit.class);
        }
    }

    public static void status() {
        //TODO: complete rest of this method
        staging = StagingArea.load();
        System.out.println("=== Branches ===");
        String currentBranch = readContentsAsString(CURR_BRANCH);
        for (String s : plainFilenamesIn(BRANCHES_DIR)) {
            if (s.equals("current")) {
                continue;
            }
            if (s.equals(currentBranch)) {
                System.out.println("*" + s);
            } else {
                System.out.println(s);
            }
        }
        System.out.println("=== Staged Files ===");
        for (File file : staging.getAdded().keySet()) {
            System.out.println(file.getName());
        }
        System.out.println("=== Removed Files ===");
        for (File file : staging.getRemoved()) {
            System.out.println(file.getName());
        }
        System.out.println("=== Modifications Not Stages For Commit ===");
        System.out.println("=== Untracked Files ===");
    }

    public static void branch(String branchName) {
        File branchFile = join(BRANCHES_DIR, branchName);
        if (branchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        writeContents(branchFile, readContentsAsString(HEAD));
    }

    public static void removeBranch(String branchName) {
        if (!plainFilenamesIn(BRANCHES_DIR).contains(branchName)) {
            System.out.println("A branch with that name does not exist");
            return;
        }
        if (readContentsAsString(CURR_BRANCH).equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        }
        join(BRANCHES_DIR, branchName).delete();
    }

    public static void reset(String commitID) {
        if (!plainFilenamesIn(COMMITS_DIR).contains(commitID)) {
            System.out.println("No commits with that id exists.");
            return;
        }
        Commit commit = readObject(join(COMMITS_DIR, commitID), Commit.class);
        Set<File> commitFiles = commit.getFiles().keySet();
        for (String fileName : plainFilenamesIn(CWD)) {
            File file = join(CWD, fileName);
            if (!commitFiles.contains(file)) {
                file.delete();
            }
        }
        for (File file : commitFiles) {
            checkout(file.getName(), commitID);
        }
        writeContents(HEAD, commitID);
        writeContents(join(BRANCHES_DIR, readContentsAsString(CURR_BRANCH)), commitID);
    }

    public static void merge(String branchName) {
        staging = StagingArea.load();
        for (String s : plainFilenamesIn(CWD)) {
            if (!getHead().getFiles().containsKey(join(CWD, s))) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        if (!staging.getAdded().isEmpty() || !staging.getRemoved().isEmpty()) {
            System.out.println("You have uncommitted changes");
            return;
        }
        if (!plainFilenamesIn(BRANCHES_DIR).contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        String branchHeadHash = readContentsAsString(join(BRANCHES_DIR, branchName));
        Commit given = readObject(join(COMMITS_DIR, branchHeadHash), Commit.class);
        Commit current = getHead();
        if (given == current) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        Commit splitPoint = Commit.latestCommonAncestor(given, current);
        if (splitPoint == given) {
            System.out.println("Given branch is an ancestor of the current branch");
            return;
        }
        if (splitPoint == current) {
            checkout(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        boolean conflict = false;
        Map<File, String> givenFiles = given.getFiles();
        Map<File, String> currentFiles = current.getFiles();
        Map<File, String> splitFiles = splitPoint.getFiles();
        for (File splitFile : splitFiles.keySet()) {
            //if file exists in all three commits
            if (givenFiles.containsKey(splitFile) && currentFiles.containsKey(splitFiles)) {
                //if file modified in given but not in current
                if (currentFiles.get(splitFile) == splitFiles.get(splitFile) && givenFiles.get(splitFile) != splitFiles.get(splitFile)) {
                    checkout(splitFile.getName(), given.getHash());
                    add(splitFile.getName());
                //if file modified in current but not in given
                } else if (currentFiles.get(splitFile) != splitFiles.get(splitFile) && givenFiles.get(splitFile) == splitFiles.get(splitFile)) {
                    checkout(splitFile.getName(), current.getHash());
                    add(splitFile.getName());
                }
            //if file doesn't exist in given and not changed in current
            } else if (!givenFiles.containsKey(splitFile) && currentFiles.get(splitFile) == splitFiles.get(splitFile)) {
                remove(splitFile.getName());
            } else if ((!givenFiles.containsKey(splitFile) && currentFiles.get(splitFile) != splitFiles.get(splitFile)) ||
                    (!currentFiles.containsKey(splitFile) && givenFiles.get(splitFile) != splitFiles.get(splitFile)) ||
                    (currentFiles.get(splitFile) != givenFiles.get(splitFile))) {
                conflict = true;
                writeContents(splitFile, "<<<<<<< HEAD\n", Blob.readBlob(currentFiles.getOrDefault(splitFile, "")), "======\n",
                        Blob.readBlob(givenFiles.getOrDefault(splitFile, "")), ">>>>>>>");
                add(splitFile.getName());
            }
        }
        //if file only present in given
        for (File givenFile : givenFiles.keySet()) {
            if (!splitFiles.containsKey(givenFile) && !currentFiles.containsKey(givenFile)) {
                checkout(givenFile.getName(), given.getHash());
                add(givenFile.getName());
            }
        }
        commit("Merged " + branchName + " into " + readContentsAsString(CURR_BRANCH));
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    public static Commit getHead() {
        String headHash = readContentsAsString(HEAD);
        return readObject(join(COMMITS_DIR, headHash), Commit.class);
    }
}