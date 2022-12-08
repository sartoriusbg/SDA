import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Outlook;
import bg.sofia.uni.fmi.mjt.mail.metaDataHandler.AccType;
import bg.sofia.uni.fmi.mjt.mail.metaDataHandler.MetaDataHandler;
import bg.sofia.uni.fmi.mjt.mail.rulehandler.RuleHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
/* subject-includes: <list-of-keywords>
 * subject-or-body-includes: <list-of-keywords>
 * recipients-includes: <list-of-recipient-emails>
 * from: <sender-email>
 subject-includes: mjt, izpit, 2022
     * subject-or-body-includes: izpit, sex
     * from: stoyo@fmi.bg
     * */
public class Main {
    public static void main(String[] args) {
        Outlook test = new Outlook();
        test.addNewAccount("gosho", "gosho@gmail.com");
        test.addNewAccount("pesho", "pesho@gmail.com");
        test.createFolder("pesho", "/inbox/test");
        test.createFolder("gosho", "/inbox/gtest");
        test.sendMail("gosho", "sender: jane.doe@gmail.com" + System.lineSeparator()
                + "subject: Test email" + System.lineSeparator()
                + "recipients: john.doe@gmail.com, pesho@gmail.com", "This is a test email.");
        test.addRule("pesho", "/inbox/test", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7);
        test.addNewAccount("bojo", "bojo@gmail.com");
        test.createFolder("bojo", "/inbox/btest1");
        test.createFolder("bojo", "/inbox/btest2");
        test.addRule("bojo", "/inbox/btest1", "subject-includes: Test" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 7);
        test.addRule("bojo", "/inbox/btest2", "subject-includes: Test, bojo" + System.lineSeparator() +
                "subject-or-body-includes: email" + System.lineSeparator() +
                "from: gosho@gmail.com", 6);
        test.sendMail("gosho", "sender: jane.doe@gmail.com" + System.lineSeparator()
                + "subject: Test email" + System.lineSeparator()
                + "recipients: bojo@gmail.com, pesho@gmail.com", "This is a test email.");
        test.sendMail("gosho", "sender: jane.doe@gmail.com" + System.lineSeparator()
                + "subject: Test email bojo" + System.lineSeparator()
                + "recipients: bojo@gmail.com, pesho@gmail.com", "This is a test email.");
        test.addNewAccount("dari", "dari@gmail.com");
    }
}
