package bg.sofia.uni.fmi.mjt.mail.metaDataHandler;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;

public class MetaDataHandler {
    private Mail toSend;
    private String metaData;

    public Mail getToSend() {
        return toSend;
    }

    public String getMetaData() {
        return metaData;
    }

    public MetaDataHandler(Account account, String mailMetadata, String mailContent, AccType type) {
        if (type == AccType.SENDER) {
            String correctMetadata = "sender: " + account.emailAddress() + System.lineSeparator();
            String subject = "";
            String lineRec = "";
            HashSet<String> recipients = new HashSet<>();
            for (String line : mailMetadata.split(System.lineSeparator())) {
                if (line.startsWith("subject:")) {
                    subject = line.replace("subject: ", "");
                }
                if (line.startsWith("recipients:")) {
                    lineRec = line;
                    String toAdd = line.replace("recipients: ", "");
                    toAdd = toAdd.replaceAll(" ", "");
                    String[] sstr = toAdd.split(",");
                    Collections.addAll(recipients, sstr);
                }

            }
            correctMetadata += "subject: " + subject + System.lineSeparator() + lineRec;
            LocalDateTime now = LocalDateTime.now();
            this.toSend = new Mail(account, recipients, subject, mailContent, now);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            correctMetadata += System.lineSeparator() + "received: " + now.format(formatter);
            this.metaData = correctMetadata;
        }
        if (type == AccType.RECIPIENT) {
            String sender = "";
            String subject = "";
            HashSet<String> recipients = new HashSet<>();
            String t = "";
            LocalDateTime time = LocalDateTime.now();
            for (String line : mailMetadata.split(System.lineSeparator())) {
                if (line.startsWith("sender:")) {
                    sender = line.replace("sender: ", "");
                }
                if (line.startsWith("subject:")) {
                    subject = line.replace("subject: ", "");
                }
                if (line.startsWith("recipients:")) {
                    String toAdd = line.replace("recipients: ", "");
                    toAdd = toAdd.replaceAll(" ", "");
                    String[] sstr = toAdd.split(",");
                    Collections.addAll(recipients, sstr);
                }
                if (line.startsWith("received:")) {
                    t = line.replace("received: ", "");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    time = LocalDateTime.parse(t, formatter);
                }

            }
            this.metaData = mailMetadata;
            this.toSend = new Mail(account, recipients, subject, mailContent, time);
        }
    }
}
