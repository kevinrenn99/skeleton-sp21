package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

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

    private static StagingArea staging = new StagingArea();



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
        try {
            HEAD.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Commit initialCommit = new Commit();
        String hash = sha1(serialize(initialCommit));
        File initialCommitFile = join(COMMITS_DIR, hash);
        writeObject(initialCommitFile, initialCommit);
        writeContents(HEAD, hash);
        staging.createFile();
    }

    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Blob blob = new Blob(file);
        blob.saveBlob();
        staging.add(file, blob.getHash());
        staging.printAdded();
    }

    public static void commit() {

    }

    public static void remove(String fileName) {
        File file = join(CWD, fileName);
        staging.remove(file);
    }

    public static Commit getHead() {
        String headHash = readContentsAsString(HEAD);
        return readObject(join(COMMITS_DIR, headHash), Commit.class);
    }

}
