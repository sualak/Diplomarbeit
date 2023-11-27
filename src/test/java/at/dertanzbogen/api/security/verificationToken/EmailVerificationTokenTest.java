package at.dertanzbogen.api.security.verificationToken;

import at.dertanzbogen.api.security_token_auth.verificationToken.EmailVerificationToken;
import org.junit.jupiter.api.Test;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.*;

class EmailVerificationTokenTest {

    @Test
    void ensure_EmailVerificationToken_isValid()
    {
        // GIVEN
        var email = "test@example.com";

        // WHEN
        var result = EmailVerificationToken.of(email, ofMinutes(1));
        var validator = result.validator();
        var token = result.token();

        // THEN
        assertNotNull(token);
        assertEquals(email, token.getEmail());
        assertTrue(token.verify(validator));
    }

    @Test
    void ensure_EmailVerificationToken_isInvalid()
    {
        // GIVEN
        var email = "test@example.com";

        // WHEN
        var result = EmailVerificationToken.of(email, ofMinutes(1));
        var validator = result.validator();
        var token = result.token();

        // THEN
        assertNotNull(token);
        assertEquals(email, token.getEmail());
        assertFalse(token.verify(validator + "invalid"));
    }

    @Test
    void ensure_EmailVerificationToken_isExpired() throws InterruptedException
    {
        // GIVEN
        var email = "test@example.com";

        // WHEN
        var result = EmailVerificationToken.of(email, ofMillis(1));
        var validator = result.validator();
        var token = result.token();
        // Wait for the token to expire
        Thread.sleep(1);

        // THEN
        assertNotNull(token);
        assertEquals(email, token.getEmail());
        assertFalse(token.verify(validator));
    }
}