package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;
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

    /** The message of this Commit. */
    private String message;

    /** Timestamp of this Commit. */
    private Date timestamp;

    /** Parent of this Commit. */
    private String parent;

    /** List of Files Contained */
    private Map<File, String> files;

    private String hash;

    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = null;
        this.files = new HashMap<>();
    }

    public Commit(String message) {
        this.message = message;
        this.timestamp = new Date();
        this.parent = readContentsAsString(Repository.HEAD);
        this.files = Repository.getHead().getFiles();
    }

    public Map<File, String> getFiles() {
        return this.files;
    }

    public boolean containsFile(File file) {
        return files.containsKey(file);
    }

    public boolean containsHash(String hash) {
        return files.values().contains(hash);
    }

    public void addCommit(StagingArea staging) {
        Map<File, String> toAdd = staging.getAdded();
        Set<File> toRemove = staging.getRemoved();
        if (toAdd.isEmpty() && toRemove.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        for (File f : toAdd.keySet()) {
            this.files.put(f, toAdd.get(f));
        }
        for (File f : toRemove) {
            this.files.remove(f);
        }
        staging.clear();
        this.save();
    }

    public void save() {
        String branchName = readContentsAsString(Repository.CURR_BRANCH);
        File branchFile = join(Repository.BRANCHES_DIR, branchName);
        hash = sha1(serialize(this));
        writeContents(branchFile, hash);
        File commitFile = join(Repository.COMMITS_DIR, hash);
        writeObject(commitFile, this);
        writeContents(Repository.HEAD, hash);
    }

    public static Commit latestCommonAncestor(Commit second, Commit first) {
        Set<String> firstCommitSet = new HashSet<>();
        while (true) {
            firstCommitSet.add(first.hash);
            if (first.getParent() == null) {
                break;
            }
            first = readObject(join(Repository.COMMITS_DIR, first.getParent()), Commit.class);
        }
        while (true) {
            System.out.println(second.hash);
            if (firstCommitSet.contains(second.hash)) {
                return second;
            }
            if (second.getParent() == null) {
                return second;
            }
            second = readObject(join(Repository.COMMITS_DIR, second.getParent()), Commit.class);
        }
    }

    public String getParent() {
        return parent;
    }

    public String getMessage() {
        return message;
    }

    public String getHash() {
        return hash;
    }

    public static Commit getCommit(String hash) {
        File commitFile = join(Repository.COMMITS_DIR, hash);
        return readObject(commitFile, Commit.class);
    }

    @Override
    public String toString() {
        return String.format("===\ncommit %s\nDate: %ta %tb %te %tl:%tM:%tS %tY %tz\n%s\n", hash, timestamp, timestamp, timestamp, timestamp, timestamp, timestamp, timestamp, timestamp, message);
    }
}