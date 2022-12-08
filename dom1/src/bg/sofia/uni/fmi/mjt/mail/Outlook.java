package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;
import bg.sofia.uni.fmi.mjt.mail.folders.FolderStructure;
import bg.sofia.uni.fmi.mjt.mail.metaDataHandler.AccType;
import bg.sofia.uni.fmi.mjt.mail.metaDataHandler.MetaDataHandler;
import bg.sofia.uni.fmi.mjt.mail.rulehandler.RuleHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Outlook implements MailClient {
    private static final int MIN_PRIO = 10;
    private static final int MAX_PRIO = 1;
    final HashMap<String, LinkedList<RuleHandler>[]> rules;
    private FolderStructure files;

    HashMap<String, Account> accountsByName;
    HashMap<String, Account> accountsByEmail;


    public Outlook() {
        this.files = new FolderStructure();
        this.rules = new HashMap<>();
        this.accountsByName = new HashMap<>();
        this.accountsByEmail = new HashMap<>();
    }

    private static void stringCheck(String toCheck, String message) {
        if (toCheck == null || toCheck.isEmpty() || toCheck.isBlank()) {
            throw new IllegalArgumentException(message + "is null, empty or blank!");
        }
    }

    @Override
    public Account addNewAccount(String accountName, String email) {
        stringCheck(accountName, "Account name");
        stringCheck(email, "Email");
        if (accountsByName.containsKey(accountName)) {
            throw new AccountAlreadyExistsException("Account name already exists");
        }
        Account acc = new Account(email, accountName);
        LinkedList<RuleHandler>[] toAdd = new LinkedList[MIN_PRIO];
        for (int i = MAX_PRIO - 1; i < MIN_PRIO; i++) {
            toAdd[i] = new LinkedList<>();
        }
        rules.put(accountName, toAdd);
        files.addAcc(accountName);
        accountsByName.put(accountName, acc);
        accountsByEmail.put(email, acc);
        return acc;
    }

    @Override
    public void createFolder(String accountName, String path) {
        stringCheck(accountName, "Account name");
        stringCheck(path, "Path");
        if (!accountsByName.containsKey(accountName)) {
            throw new AccountNotFoundException("No such account!");
        }
        files.addFolder(accountName, path);
    }

    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {
        stringCheck(accountName, "Account name");
        stringCheck(folderPath, "Folder path");
        stringCheck(ruleDefinition, "Rule definition");
        if (priority > MIN_PRIO || priority < MAX_PRIO) {
            throw new IllegalArgumentException("Priority is out of possible range");
        }
        if (!accountsByName.containsKey(accountName)) {
            throw new AccountNotFoundException("No such account name");
        }
        if (!files.folderExists(accountName, folderPath)) {
            throw new FolderNotFoundException("No such folder for this account");
        }
        RuleHandler toAdd = new RuleHandler(ruleDefinition, priority, folderPath);
        for (RuleHandler r : rules.get(accountName)[priority - 1]) {
            if (r.equals(toAdd)) {
                throw new RuleAlreadyDefinedException("This rule already exists");
            }
            if (r.contradicts(toAdd)) {
                return;
            }
        }
        rules.get(accountName)[priority - 1].add(toAdd);
        for (Mail mail : files.getFolder(accountName, "/inbox")) {
            if (toAdd.complies(mail)) {
                files.removeMail(accountName, mail, "/inbox");
                files.addMail(accountName, toAdd.getFolderPath(), mail);
            }
        }
    }

    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {
        stringCheck(accountName, "Account name");
        stringCheck(mailMetadata, "Metadata");
        stringCheck(mailContent, "Mail content");
        if (!accountsByName.containsKey(accountName)) {
            throw new AccountNotFoundException("No such account name exists for recipient");
        }
        String[] sstr = mailMetadata.split(System.lineSeparator());
        String sender = "";
        for(String line : sstr) {
            if(line.startsWith("sender: ")) {
                sender = line.replace("sender: ", "");
            }
        }
        if(!accountsByEmail.containsKey(sender)) {
            throw new AccountNotFoundException("No such account email exists for sender");
        }
        MetaDataHandler info = new MetaDataHandler(accountsByEmail.get(sender), mailMetadata, mailContent, AccType.RECIPIENT);
        Mail toAdd = info.getToSend();
        for (LinkedList<RuleHandler> l : rules.get(accountName)) {
            for (RuleHandler r : l) {
                if (r.complies(toAdd)) {
                    files.addMail(accountName, r.getFolderPath(), toAdd);
                    return;
                }
            }
        }
        files.addMail(accountName, "/inbox", toAdd);
    }

    @Override
    public Collection<Mail> getMailsFromFolder(String account, String folderPath) {
        stringCheck(account, "Account name");
        stringCheck(folderPath, "Folder path");
        if (!accountsByName.containsKey(account)) {
            throw new AccountNotFoundException("No such account!");
        }
        if (!files.folderExists(account, folderPath)) {
            throw new FolderNotFoundException("No such folder for the account");
        }
        return files.getFolder(account, folderPath);
    }

    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {
        stringCheck(accountName, "Account name");
        stringCheck(mailMetadata, "Metadata");
        stringCheck(mailContent, "Mail content");
        MetaDataHandler info = new MetaDataHandler(accountsByName.get(accountName), mailMetadata, mailContent, AccType.SENDER);
        files.addMail(accountName, "/sent", info.getToSend());
        for (String recipient : info.getToSend().recipients()) {
            if (accountsByEmail.containsKey(recipient)) {
                receiveMail(accountsByEmail.get(recipient).name(), info.getMetaData(), mailContent);
            }
        }
    }

}
