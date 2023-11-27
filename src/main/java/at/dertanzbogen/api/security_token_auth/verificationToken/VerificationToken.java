package at.dertanzbogen.api.security_token_auth.verificationToken;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.time.Instant;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;

// The base class for all verification tokens
// EmailVerificationToken, PasswordResetToken, etc.
public abstract class VerificationToken
{
    // Configuration ------------------------------------------------------------

    static {
        // BouncyCastle is a Java cryptography API provider that adds support
        // for a wide range of ciphers and algorithms

        // Add the BouncyCastleProvider to the JVM
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
            Security.addProvider(new BouncyCastleProvider());
    }

    // https://cryptobook.nakov.com/cryptographic-hash-functions/secure-hash-algorithms
    // The hashing algorithm to use
    public static final String HASH_ALGO = "Keccak-256";

    // https://www.bouncycastle.org/
    // The name of the Crypto provider to use
    public static final String PROVIDER_NAME = BouncyCastleProvider.PROVIDER_NAME;


    // Return Types -------------------------------------------------------------

    protected record GeneratedValidator(String validator, String validatorHashed) {}


    // Attributes ---------------------------------------------------------------

    public static final int VALIDATOR_CLEAR_LENGTH = 36;

    // The time at which the token expires
    private final Instant expiresAt;

    // The hashed validator, the clear validator is sent to the user via email
    private final String validatorHashed;


    // Ctor ---------------------------------------------------------------------

    public VerificationToken(Instant expiresAt, String validatorHashed)
    {
        this.validatorHashed = validatorHashed;
        this.expiresAt = expiresAt;
    }



    // Methods ------------------------------------------------------------------

    protected static GeneratedValidator generateValidator()
    {
        // Generate a random validator using UUID v4 which uses a secure random number generator
        String validator = randomUUID().toString();

        // Hash and base64 encode the validator
        String validatorHashed = Base64.getEncoder().encodeToString(hashValidator(validator));

        // Return the validator and the hashed validator
        return new GeneratedValidator(validator, validatorHashed);
    }


    // Verify the validator against the hashed validator
    public boolean verify(String validator)
    {
        return isValidatorNonExpired() && isValidatorValid(validator);
    }

    // Check if the validator has not expired
    private boolean isValidatorNonExpired()
    {
        return Instant.now().isBefore(expiresAt);
    }

    // Check if the validator is valid
    private boolean isValidatorValid(String validator)
    {
        byte[] suppliedValidatorHashBytes = hashValidator(validator);
        byte[] savedValidatorHashBytes = Base64.getDecoder().decode(validatorHashed);

        // Compare in constant time to prevent timing attacks
        return MessageDigest.isEqual(suppliedValidatorHashBytes, savedValidatorHashBytes);
    }

    // Hash the validator using the hashing algorithm and provider
    protected static byte[] hashValidator(String validator)
    {
        try {
            // Get an instance of the hash algorithm
            MessageDigest md = MessageDigest.getInstance(HASH_ALGO, PROVIDER_NAME);

            // Hash the validator as a byte array
            return md.digest(validator.getBytes(UTF_8));

            // We don't expect these exceptions to be thrown
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found", e);

        } catch (NoSuchProviderException e) {
            throw new RuntimeException("provider not found", e);
        }
    }
}
