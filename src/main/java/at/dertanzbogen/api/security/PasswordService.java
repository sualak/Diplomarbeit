//package at.dertanzbogen.api.security;
//
//import com.nulabinc.zxcvbn.Strength;
//import com.nulabinc.zxcvbn.Zxcvbn;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
///**
// * Service for encoding and matching passwords
// */
//@Service
//public class PasswordService {
//    private final PasswordEncoder passwordEncoder;
//    private final Zxcvbn zxcvbn;
//    private static final int PASSWORD_STRENGTH = 4;
//
//    public PasswordService(PasswordEncoder passwordEncoder)
//    {
//        this.passwordEncoder = passwordEncoder;
//        zxcvbn = new Zxcvbn();
//    }
//
//    public EncodedPassword encode(String password)
//    {
//        Strength strength = zxcvbn.measure(password);
//        if (strength.getScore() < PASSWORD_STRENGTH)
//            throw new IllegalArgumentException("Password is too weak");
//        return new EncodedPassword(passwordEncoder.encode(password));
//    }
//
//    public boolean matches(String rawPassword, String encodedPassword)
//    {
//        return passwordEncoder.matches(rawPassword, encodedPassword);
//    }
//
//    public static final class EncodedPassword
//    {
//        private final String password;
//        private EncodedPassword(String password)
//        {
//            this.password = password;
//        }
//        public String getPassword()
//        {
//            return password;
//        }
//    }
//}
