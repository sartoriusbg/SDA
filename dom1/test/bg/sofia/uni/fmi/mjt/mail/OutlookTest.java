package bg.sofia.uni.fmi.mjt.mail;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;
import bg.sofia.uni.fmi.mjt.mail.folders.FolderStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OutlookTest {

    @Mock
    private FolderStructure filesMock;

    @InjectMocks
    private Outlook test;

    @Test
    void addAccountInvalidNameTest() {
        assertThrows(IllegalArgumentException.class, ()-> test.addNewAccount(null, "Pesho@gmail.com"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()-> test.addNewAccount("", "Pesho@gmail.com"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()-> test.addNewAccount("   ", "Pesho@gmail.com"),
                "Should throw IllegalArgumentException for account name");
    }

    @Test
    void addAccountInvalidMailTest() {
        assertThrows(IllegalArgumentException.class, ()-> test.addNewAccount("pesho", null),
                "Should throw IllegalArgumentException for email");
        assertThrows(IllegalArgumentException.class, ()-> test.addNewAccount("pesho", ""),
                "Should throw IllegalArgumentException for email");
        assertThrows(IllegalArgumentException.class, ()-> test.addNewAccount("pesho", "  "),
                "Should throw IllegalArgumentException for email");
    }

    @Test
    void addAccountTest() {
        test.addNewAccount("pesho", "pesho@gmail.com");
        assertTrue(test.accountsByName.containsKey("pesho") && test.accountsByName.get("pesho").equals(new Account("pesho@gmail.com","pesho")),
                "The acc should exist in accounts");
        assertTrue(test.accountsByEmail.containsKey("pesho@gmail.com") && test.accountsByEmail.get("pesho@gmail.com").equals(new Account("pesho@gmail.com","pesho")),
                "The acc should exist in accounts");
        assertTrue(test.rules.containsKey("pesho"), "Account should be in the rules hashmap");
        verify(filesMock).addAcc("pesho");
    }

    @Test
    void addFolderIllegalNameTest() {
        assertThrows(IllegalArgumentException.class, ()->test.createFolder(null, "/inbox/test"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()->test.createFolder("", "/inbox/test"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()->test.createFolder("  ", "/inbox/test"),
                "Should throw IllegalArgumentException for account name");
    }

    @Test
    void addFolderIllegalPathTest() {
        assertThrows(IllegalArgumentException.class, ()->test.createFolder("pesho",null),
                "Should throw IllegalArgumentException for path");
        assertThrows(IllegalArgumentException.class, ()->test.createFolder("pesho",""),
                "Should throw IllegalArgumentException for path");
        assertThrows(IllegalArgumentException.class, ()->test.createFolder("pesho","  "),
                "Should throw IllegalArgumentException for path");
    }
    @Test
    void addFolderNonexistantNameTest() {
        assertThrows(AccountNotFoundException.class, ()-> test.createFolder("gosho", "/inbox/test"),
                "Should throw AccountNotFoundException for account name");
    }

    @Test
    void addFolderTest() {
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.createFolder("pesho", "/inbox/test");
        verify(filesMock).addFolder("pesho", "/inbox/test");
    }

    /*
     test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7);*/
    @Test
    void addRuleInvalidNameTest() {
        assertThrows(IllegalArgumentException.class, ()->test.addRule(null, "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw IllegalArgumentException for name");
        assertThrows(IllegalArgumentException.class, ()->test.addRule("", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw IllegalArgumentException for name");
        assertThrows(IllegalArgumentException.class, ()->test.addRule("  ", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw IllegalArgumentException for name");
    }

    @Test
    void addRuleInvalidPath() {
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", null, "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw IllegalArgumentException for path");
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", "", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw IllegalArgumentException for path");
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", "  ", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw IllegalArgumentException for path");
    }

    @Test
    void addRuleInvalidDef() {
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", "/inbox/test", null, 7) ,
                "Should throw IllegalArgumentException for rule def");
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", "/inbox/test", "", 7) ,
                "Should throw IllegalArgumentException for rule def");
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", "/inbox/test", "  ", 7) ,
                "Should throw IllegalArgumentException for rule def");
    }

    @Test
    void addRuleInvalidPrio() {
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", -7) , "Should throw IllegalArgumentException for priority");
        assertThrows(IllegalArgumentException.class, ()->test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 17) , "Should throw IllegalArgumentException for priority");
    }

    @Test
    void addRuleForNonexistantAcc() {
        assertThrows(AccountNotFoundException.class, ()->test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw AccountNotFoundException");
    }

    @Test
    void addRuleRuleExistsTest() {
        when(filesMock.folderExists("pesho", "/inbox/test")).thenReturn(true);
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7);
        assertThrows(RuleAlreadyDefinedException.class, ()->test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw FolderAlreadyExistsException");
    }

    @Test
    void addRuleWithNonexistantPathTest() {
        when(filesMock.folderExists(anyString(), anyString())).thenReturn(false);
        test.addNewAccount("pesho", "pesho@gmail.com");
        assertThrows(FolderNotFoundException.class, ()->test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7) , "Should throw FolderAlreadyExistsException");
    }

    @Test
    void addRuleTest() {
        when(filesMock.folderExists(anyString(), anyString())).thenReturn(true);
        LinkedList<Mail> res = new LinkedList<>();
        Mail t = new Mail(new Account("gosho@gmail.com", "gosho"), new HashSet<>(), "Test email", "email", LocalDateTime.now());
        res.add(t);
        when(filesMock.getFolder("pesho", "/inbox")).thenReturn(res);
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.createFolder("pesho", "/inbox/test");
        test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7);
        verify(filesMock).removeMail("pesho", t, "/inbox");
        verify(filesMock).addMail("pesho", "/inbox/test", t);
    }

    @Test
    void getMailsFromFolderIllegalAccountTest() {
        assertThrows(IllegalArgumentException.class, ()->test.getMailsFromFolder(null, "/inbox"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()->test.getMailsFromFolder("", "/inbox"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()->test.getMailsFromFolder("  ", "/inbox"),
                "Should throw IllegalArgumentException for account name");
    }

    @Test
    void getMailsFromFolderIllegalPathTest() {
        assertThrows(IllegalArgumentException.class, ()->test.getMailsFromFolder("pesho", null),
                "Should throw IllegalArgumentException for path");
        assertThrows(IllegalArgumentException.class, ()->test.getMailsFromFolder("pesho", ""),
                "Should throw IllegalArgumentException for path");
        assertThrows(IllegalArgumentException.class, ()->test.getMailsFromFolder("pesho", "  "),
                "Should throw IllegalArgumentException for path");
    }

    @Test
    void getMailsFromFolderNonexistantAccountTest() {
        assertThrows(AccountNotFoundException.class, ()->test.getMailsFromFolder("pesho", "/inbox"),
                "Should throw AccountNotFoundException");
    }

    @Test
    void getMailsFromFolderFolderNotFoundTest() {
        when(filesMock.folderExists(anyString(), anyString())).thenReturn(false);
        test.addNewAccount("pesho", "pesho@gmail.com");
        assertThrows(FolderNotFoundException.class, ()->test.getMailsFromFolder("pesho", "/inbox"),
                "Should throw AccountNotFoundException");
    }

    @Test
    void getMailsFromFolderTest() {
        when(filesMock.folderExists("pesho", "/inbox")).thenReturn(true);
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.getMailsFromFolder("pesho", "/inbox");
        verify(filesMock).getFolder("pesho", "/inbox");
    }


    @Test
    void sendMailTestInvalidAccountTest() {
        assertThrows(IllegalArgumentException.class, ()->test.sendMail(null, "a", "b"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("", "a", "b"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("  ", "a", "b"),
                "Should throw IllegalArgumentException for account name");
    }

    @Test
    void sendMailTestInvalidMetadataTest() {
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("pesho", null, "b"),
                "Should throw IllegalArgumentException for metadata");
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("pesho", "", "b"),
                "Should throw IllegalArgumentException for metadata");
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("pesho", "  ", "b"),
                "Should throw IllegalArgumentException for metadata");
    }

    @Test
    void sendMailTestInvalidMailContentTest() {
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("pesho", "a", null),
                "Should throw IllegalArgumentException for mail content");
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("pesho", "a", ""),
                "Should throw IllegalArgumentException for mail content");
        assertThrows(IllegalArgumentException.class, ()->test.sendMail("pesho", "a", "  "),
                "Should throw IllegalArgumentException for mail content");
    }

    @Test
    void sendMailTest() {
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.addNewAccount("gosho", "gosho@gmail.com");
        test.sendMail("gosho", "sender: jane.doe@gmail.com" + System.lineSeparator()
                + "subject: Test email" + System.lineSeparator()
                + "recipients: pesho@gmail.com", "This is a test email.");
        HashSet<String> rec = new HashSet<>();
        rec.add("pesho@gmail.com");
        verify(filesMock).addMail(eq("gosho"), eq("/sent"), any());
    }

    @Test
    void receiveMailInvalidAccountTest() {
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail(null, "a", "b"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("", "a", "b"),
                "Should throw IllegalArgumentException for account name");
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("  ", "a", "b"),
                "Should throw IllegalArgumentException for account name");
    }

    @Test
    void receiveMailInvalidMetadataTest() {
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("pesho", null, "b"),
                "Should throw IllegalArgumentException for metadata");
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("pesho", "", "b"),
                "Should throw IllegalArgumentException for metadata");
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("pesho", "  ", "b"),
                "Should throw IllegalArgumentException for metadata");
    }

    @Test
    void receiveMailInvalidMailContentTest() {
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("pesho", "a", null),
                "Should throw IllegalArgumentException for mail content");
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("pesho", "a", ""),
                "Should throw IllegalArgumentException for mail content");
        assertThrows(IllegalArgumentException.class, ()-> test.receiveMail("pesho", "a", "  "),
                "Should throw IllegalArgumentException for mail content");
    }

    @Test
    void receiveMailNonexistantAccountTest() {
        assertThrows(AccountNotFoundException.class, ()-> test.receiveMail("pesho", "a", "b"),
                "Should throw AccountNotFoundException for receiver");
    }

    @Test
    void receiveMailNonexistantSender() {
        test.addNewAccount("pesho", "pesho@gmail.com");
        assertThrows(AccountNotFoundException.class, ()-> test.receiveMail("pesho", "sender: gosho@gmail.com", "b"),
                "Should throw AccountNotFoundException for sender");
    }

    @Test
    void receiveMailWithoutRules() {
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.addNewAccount("gosho", "gosho@gmail.com");
        test.receiveMail("pesho", "sender: gosho@gmail.com", "b");
        verify(filesMock).addMail(eq("pesho"), eq("/inbox"), any());
    }

    @Test
    void receiveMailWithRules() {
        when(filesMock.folderExists("pesho", "/inbox/test")).thenReturn(true);
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.addNewAccount("gosho", "gosho@gmail.com");
        test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7);
        test.receiveMail("pesho", "sender: gosho@gmail.com" + System.lineSeparator()
                + "subject: Test email" + System.lineSeparator()
                + "recipients: bojo@gmail.com, pesho@gmail.com", "This is a test email.");
        verify(filesMock).addMail(eq("pesho"), eq("/inbox/test"), any());
    }
}
