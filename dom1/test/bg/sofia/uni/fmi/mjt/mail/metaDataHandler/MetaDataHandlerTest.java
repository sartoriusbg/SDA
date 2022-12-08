package bg.sofia.uni.fmi.mjt.mail.metaDataHandler;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class MetaDataHandlerTest {

    @Test
    void getToSendSenderTest() {
        MetaDataHandler test = new MetaDataHandler(new Account("pesho@gmail.com", "pesho"),
                "sender: jane.doe@gmail.com" + System.lineSeparator()
                        + "subject: Test email" + System.lineSeparator()
                        + "recipients: john.doe@gmail.com, bob.smith@gmail.com", "This is a test email.", AccType.SENDER);
        HashSet<String> rec = new HashSet<>();
        rec.add("john.doe@gmail.com");
        rec.add("bob.smith@gmail.com");
        assertEquals( new Mail(new Account("pesho@gmail.com", "pesho"),rec, "Test email",
                "This is a test email.", LocalDateTime.now()), test.getToSend(), "Mails should be the same");
    }

    @Test
    void getToSendRecipientTest() {
        MetaDataHandler test = new MetaDataHandler(new Account("pesho@gmail.com", "pesho"),
                "sender: pesho@gmail.com" + System.lineSeparator()
                        + "subject: Test email" + System.lineSeparator()
                        + "recipients: john.doe@gmail.com, bob.smith@gmail.com", "This is a test email.", AccType.RECIPIENT);
        HashSet<String> rec = new HashSet<>();
        rec.add("john.doe@gmail.com");
        rec.add("bob.smith@gmail.com");
        assertEquals( new Mail(new Account("pesho@gmail.com", "pesho"),rec, "Test email",
                "This is a test email.", LocalDateTime.now()), test.getToSend(), "Mails should be the same");
    }

    @Test
    void getMetaDataSenderTest() {
        MetaDataHandler test = new MetaDataHandler(new Account("pesho@gmail.com", "pesho"),
                "sender: jane.doe@gmail.com" + System.lineSeparator()
                        + "subject: Test email" + System.lineSeparator()
                        + "recipients: john.doe@gmail.com, bob.smith@gmail.com", "This is a test email.", AccType.SENDER);
        String res = test.getMetaData();
        String[] sstr = res.split(System.lineSeparator());
        res = "";
        for(int i =0; i < sstr.length - 1; i++) {
            if(i == sstr.length - 2){
                res += sstr[i];
            }
            else {
                res += sstr[i] + System.lineSeparator();
            }
        }
        assertEquals("sender: pesho@gmail.com" + System.lineSeparator() + "subject: Test email" + System.lineSeparator() + "recipients: john.doe@gmail.com, bob.smith@gmail.com",
                res , "Should be the same");

    }

    @Test
    void getMetaDataRecipientTest() {
        MetaDataHandler test = new MetaDataHandler(new Account("pesho@gmail.com", "pesho"),
                "received: 2022-12-08 14:14" + System.lineSeparator()
                        + "sender: pesho@gmail.com" + System.lineSeparator()
                        + "subject: Test email" + System.lineSeparator()
                        + "recipients: john.doe@gmail.com, bob.smith@gmail.com", "This is a test email.", AccType.RECIPIENT);
        assertEquals("received: 2022-12-08 14:14" + System.lineSeparator() + "sender: pesho@gmail.com" + System.lineSeparator() +
                "subject: Test email" + System.lineSeparator() +
                "recipients: john.doe@gmail.com, bob.smith@gmail.com"
                , test.getMetaData(), "Should be the same");
    }

}
