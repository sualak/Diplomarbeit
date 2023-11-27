package at.dertanzbogen.api.security_token_auth;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Read more about password hashing !
// https://auth0.com/blog/hashing-passwords-one-way-road-to-security/

// Cryptographic hash functions
// Bcrypt, Argon2, PBKDF2, Scrypt

// A cryptographic hash function is a hash function that is designed to be
//   computationally infeasible to reverse.

// A cryptographic hash function is a mathematical algorithm that maps
//   data of arbitrary size to a bit string of a fixed size.

// The values returned by a cryptographic hash function are called
//   hash values, hash codes, digests, or simply hashes.

// Not all hash functions are cryptographic hash functions.
// A cryptographic hash function is designed to be slow to compute.

@Service
public class PasswordService
{
    public static final int PASSWORD_STRENGTH = 4;

    private final PasswordEncoder passwordEncoder;
    private final Zxcvbn zxcvbn;

    public PasswordService(PasswordEncoder passwordEncoder)
    {
        this.passwordEncoder = passwordEncoder;
        zxcvbn = new Zxcvbn();
    }

    public EncodedPassword encode(String password)
    {
        // Check password strength
        Strength strength = zxcvbn.measure(password);

        // Password is too weak
        if (strength.getScore() < PASSWORD_STRENGTH)
            throw new IllegalArgumentException("Password is too weak");

        // Password is strong enough
        String encodePassword = passwordEncoder.encode(password);

        // Return hardened password which only can be instantiated by this class
        return new EncodedPassword(encodePassword);
    }

    public static final class EncodedPassword
    {
        private final String password;

        // Only the outer class can instantiate this class
        // Outer classes can access private members of inner classes and vice versa
        private EncodedPassword(String password)
        {
            this.password = password;
        }

        public String getPassword()
        {
            return password;
        }
    }
}
