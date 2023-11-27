package at.dertanzbogen.api.config;


import at.dertanzbogen.api.persistent.UserRepository;
//import at.dertanzbogen.api.security.DaoUserDetailsService;
//import at.dertanzbogen.api.security.SecurityConfig;
import at.dertanzbogen.api.security_token_auth.DatabaseUserDetailsService;
import at.dertanzbogen.api.security_token_auth.SecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;

// This is necessary to enable @WithUserDetails
@TestConfiguration
@Import(SecurityConfig.class)
public class TestSecurityConfig
{
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
//        return new DaoUserDetailsService(userRepository);
        return new DatabaseUserDetailsService(userRepository);
    }

}
