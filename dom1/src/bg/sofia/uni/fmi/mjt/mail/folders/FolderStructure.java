package bg.sofia.uni.fmi.mjt.mail.folders;

import bg.sofia.uni.fmi.mjt.mail.Mail;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FolderStructure {
    private final HashMap<String, HashMap<String, LinkedList<Mail>>> files;

    public FolderStructure() {
        this.files = new HashMap<>();
    }

    public void addAcc(String accName) {
        HashMap<String, LinkedList<Mail>> f = new HashMap<>();
        f.put("/inbox", new LinkedList<>());
        f.put("/sent", new LinkedList<>());
        files.put(accName, f);
    }

    public boolean accExists(String accName) {
        return files.containsKey(accName);
    }

    public void addFolder(String accName, String path) {
        if (!path.startsWith("/inbox")) {
            throw new InvalidPathException("Path doesnt start from /inbox");
        }
        if (files.get(accName).containsKey(path)) {
            throw new FolderAlreadyExistsException("Folder already in the system");
        }
        if (!files.get(accName).containsKey(getPrevFolder(path))) {
            throw new InvalidPathException("The path to the new folder doesnt exist");
        }
        files.get(accName).put(path, new LinkedList<>());
    }

    private String getPrevFolder(String path) {
        String prevFolder = "";
        String[] p = path.split("/");
        for (int i = 0; i < p.length - 1; i++) {
            if (i == p.length - 2) {
                prevFolder += p[i];
            } else {
                prevFolder += p[i] + "/";
            }
        }
        return prevFolder;
    }

    public boolean folderExists(String accName, String path) {
        return files.getOrDefault(accName, new HashMap<>()).containsKey(path);
    }

    public List<Mail> getFolder(String accName, String path) {
        return files.get(accName).get(path);
    }

    public void removeMail(String accName, Mail mail, String path) {
        files.get(accName).get(path).remove(mail);
    }

    public void addMail(String accName, String path, Mail toAdd) {
        files.get(accName).get(path).add(toAdd);
    }

    HashMap<String, HashMap<String, LinkedList<Mail>>> getFiles() {
        return files;
    }
}