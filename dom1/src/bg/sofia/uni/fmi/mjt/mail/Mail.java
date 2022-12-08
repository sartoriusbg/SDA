package bg.sofia.uni.fmi.mjt.mail;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public record Mail(Account sender, Set<String> recipients, String subject, String body, LocalDateTime received) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(sender, mail.sender) && Objects.equals(recipients, mail.recipients) && Objects.equals(subject, mail.subject)
                && Objects.equals(body, mail.body);
    }

}
