package at.dertanzbogen.api.domain.main;

import at.dertanzbogen.api.security_token_auth.verificationToken.EmailVerificationToken;
//import at.dertanzbogen.api.security.verificationToken.EmailVerificationToken;
import lombok.ToString;

import java.time.Duration;

@ToString
public class Account {

    public static final Duration TOKEN_VALIDITY_TIME = Duration.ofDays(1);
    private boolean enabled;
    private EmailVerificationToken verificationToken;

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean isEnabled)
    {
        this.enabled = isEnabled;
    }

    public EmailVerificationToken getVerificationToken()
    {
        return verificationToken;
    }

    public String generateVerificationToken(String emailToVerify)
    {
        var result = EmailVerificationToken.of(emailToVerify, TOKEN_VALIDITY_TIME);
        verificationToken = result.token();
        return result.validator();
    }

    public String verifyVerificationToken(String token)
    {
        if (verificationToken.verify(token))
        {
            String emailVerified = verificationToken.getEmail();
            verificationToken = null;
            return emailVerified;
        }
        else
            throw new IllegalArgumentException("Invalid verification token");
    }
}
