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

    /* TODO: fill in the rest of this class. */
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
        hash = sha1(serialize(this));
        File commitFile = join(Repository.COMMITS_DIR, hash);
        writeObject(commitFile, this);
        writeContents(Repository.HEAD, hash);
    }

    public String getParent() {
        return parent;
    }

    public static Commit getCommit(String hash) {
        File commitFile = join(Repository.COMMITS_DIR, hash);
        return readObject(commitFile, Commit.class);
    }

    public String toString() {
        return ("===\n" +
                "commit " + hash + "\n" +
                "Date: " + timestamp + "\n" +
                message + "\n");
    }
}