package bg.sofia.uni.fmi.mjt.mail.folders;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FolderStructureTest {
    private FolderStructure files;

    @BeforeEach
    void setup() {
        this.files = new FolderStructure();
    }

    @Test
    void addAccTest() {
        files.addAcc("pesho");
        HashMap<String, HashMap<String, List<Mail>>> test = new HashMap<>();
        HashMap<String, List<Mail>> f = new HashMap<>();
        f.put("/inbox" , new LinkedList<>());
        f.put("/sent", new LinkedList<>());
        test.put("pesho", f);
        assertTrue(files.getFiles().equals(test), "File trees should be the same");
    }

    @Test
    void addFolderTest() {
        files.addAcc("pesho");
        files.addFolder("pesho", "/inbox/test");
        HashMap<String, HashMap<String, List<Mail>>> test = new HashMap<>();
        HashMap<String, List<Mail>> f = new HashMap<>();
        f.put("/inbox" , new LinkedList<>());
        f.put("/sent", new LinkedList<>());
        f.put("/inbox/test" , new LinkedList<>());
        test.put("pesho", f);
        assertTrue(files.getFiles().equals(test), "File trees should be the same");
    }

    @Test
    void addFolderAlreadyExistsTest() {
        files.addAcc("pesho");
        assertThrows(FolderAlreadyExistsException.class, () -> files.addFolder("pesho", "/inbox"),
                "Should throw exception for folder already existing");
    }

    @Test
    void addFolderWrongRootTest() {
        files.addAcc("pesho");
        assertThrows(InvalidPathException.class, () -> files.addFolder("pesho", "/sent/test"),
                "Should throw invalid path exception for wrong root");
    }

    @Test
    void addFolderWrongPathTest() {
        files.addAcc("pesho");
        assertThrows(InvalidPathException.class, () -> files.addFolder("pesho", "/inbox/test/test2"),
                "Should throw invalid path exception for non-existing middle folder");
    }

    @Test
    void folderExistsTrueTest() {
        files.addAcc("pesho");
        assertTrue(files.folderExists("pesho", "/inbox"),"Should be true");
    }
    @Test
    void folderExistsTrueFalse() {
        files.addAcc("pesho");
        assertFalse(files.folderExists("pesho", "/inbox/test"), "Should be false");
    }

    @Test
    void getFolderTestAndAddMail() {
        files.addAcc("pesho");
        files.addMail("pesho", "/inbox",
                new Mail(new Account("gosho@gmail.com", "gosho"), new HashSet<>(), "subj", "body", LocalDateTime.now()));
        List<Mail> test= new LinkedList<>();
        test.add(new Mail(new Account("gosho@gmail.com", "gosho"), new HashSet<>(), "subj", "body", LocalDateTime.now()));
        assertIterableEquals(files.getFolder("pesho", "/inbox"), test, "Should be the same");
    }

    @Test
    void removeMailTest() {
        files.addAcc("pesho");
        files.addMail("pesho", "/inbox",
                new Mail(new Account("gosho@gmail.com", "gosho"), new HashSet<>(), "subj", "body", LocalDateTime.now()));
        files.removeMail("pesho",
                new Mail(new Account("gosho@gmail.com", "gosho"), new HashSet<>(), "subj", "body", LocalDateTime.now()), "/inbox");
        assertIterableEquals(files.getFolder("pesho", "/inbox"), new LinkedList<>(), "Should be the same");
    }

    @Test
    void getPrevFolderTest() {
    }

}
