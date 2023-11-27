//package at.dertanzbogen.api.security.verificationToken;
//
//
//import java.time.Duration;
//import java.time.Instant;
//
//
//// What is a verification token?
//// -----------------------------
//// A verification token is a token that is used to verify a user's email address.
//
//// Why do we hash the validator before storing it in the database?
//// --------------------------------------------------------------
//// The validator is hashed before stored it in the database to prevent the validator
//// from being read from the database by an attacker.
//
//// How to verify an email address using a verification token
//// ------------------------------------------------------------
//// 1. Create a new EmailVerificationToken with a random validator
//// 2. Hashed validator is stored in the database (with Keccak-256)
//// 2. Clear validator is sent to the user via email
//// 3. User clicks on the link in the email and the validator is sent to the server
//// 4. The server verifies the validator and the token
//// 5. If the token is valid, the user is verified
///**
// * A verification token for email addresses.
// */
//public class EmailVerificationToken extends VerificationToken
//{
//    // Return Types -----------------------------------------------------------
//
//    // The result of the factory method is a token and a validator
//    /**
//     * The result of the factory method is a token and a validator.
//     * The token is stored in the database and the validator is sent to the user.
//     * The user can verify his email address by sending the validator to the server.
//     * The server can verify the validator and the token.
//     */
//    public record EmailTokenWithValidator(EmailVerificationToken token, String validator) {}
//
//
//    // Attributes -------------------------------------------------------------
//
//    // The email address to verify
//    private final String email;
//
//
//    // Ctor -------------------------------------------------------------------
//
//    private EmailVerificationToken(String email, Instant expiresAt, String validatorHashed)
//    {
//        super(expiresAt, validatorHashed);
//        this.email = email;
//    }
//
//    // Factory method to create a new EmailVerificationToken
//    /**
//     * Creates a new EmailVerificationToken.
//     *
//     * @param email the email address to verify
//     * @param duration the duration until the token expires
//     * @return the token and the validator
//     *
//     * @throws IllegalArgumentException if the email is null or empty
//     * @throws IllegalArgumentException if the duration is negative
//     * @throws IllegalArgumentException if the duration is zero
//     *
//     */
//    public static EmailTokenWithValidator of(String email, Duration duration)
//    {
//        var result = generateValidator();
//        var expiresAt = Instant.now().plus(duration);
//
//        EmailVerificationToken token = new EmailVerificationToken(
//                email, expiresAt, result.validatorHashed());
//
//        return new EmailTokenWithValidator(token, result.validator());
//    }
//
//
//    // Methods ----------------------------------------------------------------
//
//    public String getEmail()
//    {
//        return email;
//    }
//}
