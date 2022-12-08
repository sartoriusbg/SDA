package bg.sofia.uni.fmi.mjt.mail.rulehandler;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
public class RuleHandlerTest {

    @Test
    void readLineTest() {
        RuleHandler test = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 7, "/inbox/test");
        HashSet<String> subject = new HashSet<>();
        subject.add("mjt");
        subject.add("izpit");
        subject.add("2022");
        HashSet<String> body = new HashSet<>();
        body.add("izpit");
        HashSet<String> recipients = new HashSet<>();
        HashSet<String> from = new HashSet<>();
        from.add("stoyo@fmi.bg");
        assertEquals( "/inbox/test", test.folderPath, "Folder path should be the same");
        assertEquals(7, test.priority, "Priority should be the same");
        assertTrue(test.subject.equals(subject), "Subjects should be the same");
        assertTrue(test.body.equals(body), "Bodies should be the same");
        assertTrue(test.recipients.equals(recipients), "Recipients should be the same");
        assertTrue(test.from.equals(from), "Froms should be the same");
    }

    @Test
    void voidContradictsTest() {
        RuleHandler test1 = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 7, "/inbox/test");
        RuleHandler test2 = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 6, "/inbox");
        RuleHandler test3 = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 7, "/inbox");
        RuleHandler test4 = new RuleHandler("subject-includes: izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 7, "/inbox");
        assertTrue(test1.contradicts(test3) && test3.contradicts(test1), "The rules should contradict eachother");
        assertFalse(test1.contradicts(test2) && test2.contradicts(test1), "The rules shouldnt contradict eachother");
        assertFalse(test1.contradicts(test4) && test4.contradicts(test1), "The rules shouldnt contradict eachother");
    }

    @Test
    void equalsTest() {
        RuleHandler test1 = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 7, "/inbox/test");
        RuleHandler test2 = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 6, "/inbox");
        RuleHandler test3 = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 7, "/inbox/test");
        RuleHandler test4 = new RuleHandler("subject-includes: izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg", 7, "/inbox/test");
        assertTrue(test1.equals(test3), "Should be equal");
        assertFalse(test1.equals(test2), "Shouldnt be equal");
        assertFalse(test1.equals(test4), "Shouldnt be equal");
    }

    @Test
    void compliesTest() {
        RuleHandler test = new RuleHandler("subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
                "subject-or-body-includes: izpit" + System.lineSeparator() +
                "from: stoyo@fmi.bg, gosho@gmail.com", 7, "/inbox/test");
        HashSet<String> rec = new HashSet<>();
        Mail m1 = new Mail(new Account("stoyo@fmi.bg", "stoyo"),rec, "Test email izpit mjt 2022",
                "This is a test izpit email.", LocalDateTime.now());
        Mail m2 = new Mail(new Account("pesho@fmi.bg", "pesho"),rec, "Test email izpit mjt 2022",
                "This is a test izpit email.", LocalDateTime.now());
        Mail m3 = new Mail(new Account("stoyo@fmi.bg", "stoyo"),rec, "Test email mjt 2022",
                "This is a test izpit email.", LocalDateTime.now());
        Mail m4 = new Mail(new Account("gosho@gmail.com", "gosho"),rec, "Test email izpit mjt 2022",
                "This is a test email.", LocalDateTime.now());
        HashSet<String> rec2 = new HashSet<>();
        rec2.add("pesho@gmail.com");
        Mail m5 = new Mail(new Account("stoyo@fmi.bg", "stoyo"),rec2, "Test email izpit mjt 2022",
                "This is a test izpit email.", LocalDateTime.now());
        assertTrue(test.complies(m1), "Should comply");
        assertFalse(test.complies(m2), "Shouldnt comply");
        assertFalse(test.complies(m3), "Shouldnt comply");
        assertFalse(test.complies(m4), "Shouldnt comply");
        assertTrue(test.complies(m5), "Should comply");
    }
}
