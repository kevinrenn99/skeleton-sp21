package gitlet;

import java.io.File;
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
    /** Blobs directory */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** Staging directory */
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /** Staged Blobs*/
    public static final File STAGED_BLOBS = join(STAGING_DIR, "blobs");
    /** Current Head*/
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** Blob Map*/
    private static HashMap<String, String> blobMap = new HashMap<>();



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
        BLOBS_DIR.mkdir();
        writeObject(join(BLOBS_DIR, "blobMap"), blobMap);
        STAGING_DIR.mkdir();
        STAGED_BLOBS.mkdir();
        HEAD.mkdir();
        Commit initialCommit = new Commit();
        String hash = sha1(serialize(initialCommit));
        File initialCommitFile = join(COMMITS_DIR, hash);
        writeObject(initialCommitFile, initialCommit);
        File headFile = join(HEAD, hash);
        writeObject(headFile, initialCommit);
    }

    public static void add(String fileName) {
        File blob = join(CWD, fileName);
        if (!blob.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        String hash = sha1(serialize(blob));
        Commit head = readObject(new File(Utils.plainFilenamesIn(HEAD).getFirst()), Commit.class);
        if (head.getBlobs().contains(hash)) {
            return;
        }
        File blobHash = join(STAGED_BLOBS, hash);
        blobHash.mkdir();
        writeContents(join(blobHash, fileName), readContents(blob));

    }
}
