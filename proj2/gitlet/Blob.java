package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    private File source;
    private byte[] contents;
    private String hash;

    public Blob(File file) {
        this.source = file;
        this.contents = readContents(file);
        this.hash = sha1(contents);
    }

    public File getFile() {
        return this.source;
    }

    //returns if file exists
    public boolean exists() {
        return source.exists();
    }

    public void saveBlob() {
        String prefix = hash.substring(0, 2);
        String suffix = hash.substring(2);
        File saveDirectory = join(Repository.OBJECTS_DIR, prefix);
        if (!saveDirectory.exists()) {
            saveDirectory.mkdir();
        }
        File saveFile = join(saveDirectory, suffix);
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeContents(saveFile, contents);
    }

    public static void copyBlob(String hash, File destination) {
        String prefix = hash.substring(0, 2);
        String suffix = hash.substring(2);
        File saveDirectory = join(Repository.OBJECTS_DIR, prefix);
        File saveFile = join(saveDirectory, suffix);
        writeContents(destination, readContentsAsString(saveFile));
    }

    public static String readBlob(String hash) {
        String prefix = hash.substring(0, 2);
        String suffix = hash.substring(2);
        File saveDirectory = join(Repository.OBJECTS_DIR, prefix);
        File saveFile = join(saveDirectory, suffix);
        return readContentsAsString(saveFile);
    }

    public String getHash() {
        return this.hash;
    }
}