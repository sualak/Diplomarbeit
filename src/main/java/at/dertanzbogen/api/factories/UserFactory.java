package at.dertanzbogen.api.factories;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.User.User.userGroup;
import at.dertanzbogen.api.presentation.DTOs.Commands.UserRegistrationCommand;
//import at.dertanzbogen.api.security.PasswordService;
import at.dertanzbogen.api.security_token_auth.PasswordService;
import at.dertanzbogen.api.security_token_auth.PasswordService.EncodedPassword;

import java.util.Objects;
/**
 * Factory for creating users
 * This class is a builder that creates a user and a token.
 * The token is used for verifying the user.
 *
 */
public class UserFactory {

    // Return Types -----------------------------------------------------------

    // In the case of unverified users, the result of the builder is a user and a token
    // In the case of verified users, the result of the builder is just a user and this class is not needed.
    public record UserWithToken(User user, String token) { }


    // Step Interfaces --------------------------------------------------------

//    public interface UserGroupStep
//    {
//        PasswordEncoderStep userGroup(userGroup userGroups);
//    }

    public interface PasswordEncoderStep
    {
        UserBuildStep encoder(PasswordService passwordService);
    }

    public interface UserBuildStep
    {
        // The last step in the builder is polymorphic.
        // We have to decide if we want a verified user or an unverified user.
        User verified();
        UserWithToken needsVerification();

        UserWithToken needsVerificationAsAdmin();
    }


    // Builder Implementation -------------------------------------------------

    // 1. The first step is the one that takes the command is a static factory method
    public static PasswordEncoderStep of(UserRegistrationCommand userRegistrationCommand)
    {
        return new UserBuilderImpl(userRegistrationCommand);
    }

    // Inner class that implements the steps of the builder
    private static class UserBuilderImpl implements PasswordEncoderStep, UserBuildStep
    {
        private final UserRegistrationCommand userRegistrationCommand;
        private PasswordService passwordService;

        private UserBuilderImpl(UserRegistrationCommand userRegistrationCommand)
        {
            Objects.requireNonNull(userRegistrationCommand, "Registration command cannot be null");

            this.userRegistrationCommand = userRegistrationCommand;
        }

        // 2. The second step is the one that takes the roles
//        @Override
//        public PasswordEncoderStep userGroup(userGroup userGroup)
//        {
//            this.userGroup = userGroup;
//            return this;
//        }

        // 3. The third step is the one that takes the password encoder
        @Override
        public UserBuildStep encoder(PasswordService passwordService)
        {
            Objects.requireNonNull(passwordService, "Password service cannot be null");

            this.passwordService = passwordService;
            return this;
        }

        // 4. The fourth step is polymorphic.
        // Calling this method means that the user is verified and this account does not need verification
        @Override
        public User verified()
        {
            return buildVerified();
        }

        // 4. The fourth step is polymorphic.
        // Calling this method means that the user is not verified and this account needs verification.
        @Override
        public UserWithToken needsVerification()
        {
            return buildNeedsVerification();
        }

        @Override
        public UserWithToken needsVerificationAsAdmin()
        {
            return buildNeedsVerificationAsAdmin();
        }

        private UserWithToken buildNeedsVerification()
        {
            // We create the user with the given roles and password
            PasswordService.EncodedPassword encodedPassword = passwordService.encode(userRegistrationCommand.password());
            User user = createUser(userRegistrationCommand, encodedPassword);

            // We disable the account and generate a verification token
            user.getAccount().setEnabled(false);
            String validator = user.getAccount().generateVerificationToken(user.getEmail().getEmail());

            // We return the user and the token and the caller will send the token to the user
            return new UserWithToken(user, validator);
        }

        private UserWithToken buildNeedsVerificationAsAdmin()
        {
            // We create the user with the given roles and password
            PasswordService.EncodedPassword encodedPassword = passwordService.encode(userRegistrationCommand.password());
            User user = createUserAsAdmin(userRegistrationCommand, encodedPassword);

            // We disable the account and generate a verification token
            user.getAccount().setEnabled(false);
            String validator = user.getAccount().generateVerificationToken(user.getEmail().getEmail());

            // We return the user and the token and the caller will send the token to the user
            return new UserWithToken(user, validator);
        }

        private User buildVerified()
        {
            // We create the user with the given roles and password
            PasswordService.EncodedPassword encodedPassword = passwordService.encode(userRegistrationCommand.password());
            User user = createUser(userRegistrationCommand, encodedPassword);

            // We enable the account
            user.getAccount().setEnabled(true);

            // We just return the user because user account is already verified
            return user;
        }
    }

    public static User createUser(UserRegistrationCommand userRegistrationCommand, PasswordService.EncodedPassword encodedPassword) {
        return new User.UserBuilder()
                .setPersonal(userRegistrationCommand.personal())
                .setPassword(encodedPassword.getPassword())
                .setEmail(userRegistrationCommand.email())
                .setLeader(userRegistrationCommand.isLeader())
                .setHelper(userRegistrationCommand.isHelper())
                //TODO: userGroup row change
//                .setUserGroup(userGroup.STUDENT)
                .setUserGroup(userGroup.valueOf(userRegistrationCommand.userGroup()))
                .build();
    }

    public static User createUserAsAdmin(UserRegistrationCommand userRegistrationCommand, PasswordService.EncodedPassword encodedPassword) {
        return new User.UserBuilder()
                .setPersonal(userRegistrationCommand.personal())
                .setPassword(encodedPassword.getPassword())
                .setEmail(userRegistrationCommand.email())
                .setLeader(userRegistrationCommand.isLeader())
                .setHelper(userRegistrationCommand.isHelper())
                .setUserGroup(userGroup.valueOf(userRegistrationCommand.userGroup()))
                .build();
    }
}
