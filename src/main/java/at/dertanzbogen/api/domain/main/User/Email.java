package at.dertanzbogen.api.domain.main.User;

import lombok.NoArgsConstructor;
import lombok.ToString;

import static at.dertanzbogen.api.domain.validation.Ensure.ensureValidMail;


@NoArgsConstructor
@ToString
public class Email
{

    private String email;

    public Email(String email) {
        this.email = ensureValidMail(email.trim().toLowerCase());
    }

    public void setEmail(String email) {
        this.email = ensureValidMail(email.trim().toLowerCase());
    }

    public String getEmail() {
        return email;
    }

    public boolean equals(Email other) {
        return this.email.equalsIgnoreCase(other.email);
    }
}
