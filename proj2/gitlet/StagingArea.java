package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;

public class StagingArea implements Serializable {
    private File directory = Repository.STAGING_DIR;
    private File stagingFile = join(directory, "stagingObj");
    private Map<File, String> addedFiles;
    private Set<File> removedFiles;

    public StagingArea() {
        addedFiles = new HashMap<>();
        removedFiles = new HashSet<>();
    }

    public void add(File file, String hash) {
        Commit head = Repository.getHead();
        if (head.containsFile(file)) {
            if (addedFiles.containsKey(file)) {
                addedFiles.remove(file);
            }
            return;
        }
        addedFiles.put(file, hash);
        this.save();
    }

    public void createFile() {
        try {
            stagingFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating file: " + e);
        }
    }

    public void save() {
        writeObject(stagingFile, this);
    }

    public void remove(File file) {
        if (addedFiles.containsKey(file)) {
            addedFiles.remove(file);
            return;
        }
        Commit head = Repository.getHead();
        if (file.exists() && head.containsFile(file)) {
            removedFiles.add(file);
            file.delete();
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    public void printAdded() {
        for (File f : addedFiles.keySet()) {
            System.out.println(f.getPath());
        }

        for (File f : removedFiles) {
            System.out.println(f.getPath());
        }
    }
}
