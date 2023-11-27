package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static at.dertanzbogen.api.domain.validation.Ensure.ensureValidPassword;

@NoArgsConstructor
@ToString
public class Password {
    private String password;
    public Password(String password) {
//        this.password = ensureValidPassword(password);
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public boolean equals(Password other) {
        return this.password.equals(other.password);
    }
}
