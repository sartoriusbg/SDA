package bg.sofia.uni.fmi.mjt.mail.rulehandler;

import bg.sofia.uni.fmi.mjt.mail.Mail;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class RuleHandler {
    final int priority;
    final String folderPath;
    final HashSet<String> subject;
    final HashSet<String> body;
    final HashSet<String> recipients;
    final HashSet<String> from;

    public String getFolderPath() {
        return folderPath;
    }

    public RuleHandler(String rule, int priority, String folderPath) {
        this.priority = priority;
        this.subject = new HashSet<>();
        this.recipients = new HashSet<>();
        this.body = new HashSet<>();
        this.from = new HashSet<>();
        this.folderPath = folderPath;
        readString(rule);
    }

    private void readString(String rule) {
        for (String line : rule.split(System.lineSeparator())) {
            if (line.startsWith("subject-includes:")) {
                String toAdd = line.replace("subject-includes:", "");
                toAdd = toAdd.replaceAll(" ", "");
                String[] sstr = toAdd.split(",");
                LinkedList<String> l = new LinkedList<String>();
                Collections.addAll(l, sstr);
                for (String add : l) {
                    subject.add(add);
                }

            }
            if (line.startsWith("subject-or-body-includes:")) {
                String toAdd = line.replace("subject-or-body-includes:", "");
                toAdd = toAdd.replaceAll(" ", "");
                String[] sstr = toAdd.split(",");
                LinkedList<String> l = new LinkedList<String>();
                Collections.addAll(l, sstr);
                for (String add : l) {
                    subject.add(add);
                    body.add(add);
                }
            }
            if (line.startsWith("recipients-includes:")) {
                String toAdd = line.replace("recipients-includes:", "");
                toAdd = toAdd.replaceAll(" ", "");
                String[] sstr = toAdd.split(",");
                LinkedList<String> l = new LinkedList<String>();
                Collections.addAll(l, sstr);
                for (String add : l) {
                    recipients.add(add);
                }
            }
            if (line.startsWith("from:")) {
                String toAdd = line.replace("from:", "");
                toAdd = toAdd.replaceAll(" ", "");
                String[] sstr = toAdd.split(",");
                LinkedList<String> l = new LinkedList<String>();
                Collections.addAll(l, sstr);
                for (String add : l) {
                    from.add(add);
                }
            }
        }
    }

    public boolean contradicts(RuleHandler other) {
        return (priority == other.priority && subject.equals(other.subject) && body.equals(other.body) &&
                recipients.equals(other.recipients) && from.equals(other.from) && !folderPath.equals(other.folderPath));
    }

    public boolean complies(Mail mail) {
        for (String sub : subject) {
            if (!mail.subject().contains(sub)) {
                return false;
            }
        }
        for (String b : body) {
            if (!mail.body().contains(b)) {
                return false;
            }
        }
        for (String r : recipients) {
            if (!mail.recipients().contains(r)) {
                return false;
            }
        }
        return from.contains(mail.sender().emailAddress());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleHandler that = (RuleHandler) o;
        return priority == that.priority && Objects.equals(folderPath, that.folderPath) && Objects.equals(subject, that.subject) && Objects.equals(body, that.body) && Objects.equals(recipients, that.recipients) && Objects.equals(from, that.from);
    }

}
