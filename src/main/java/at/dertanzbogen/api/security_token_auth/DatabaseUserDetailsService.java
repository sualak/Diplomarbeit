package at.dertanzbogen.api.security_token_auth;


import at.dertanzbogen.api.persistent.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// @Service
public class DatabaseUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {

        return userRepository.findByEmailEmail(username)
            .map(SecurityUser::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
