package at.dertanzbogen.api.security_token_auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CombinedUserDetailsService implements UserDetailsService
{
    private final List<UserDetailsService> userDetailServices;

    public CombinedUserDetailsService(List<UserDetailsService> userDetailServices)
    {
        this.userDetailServices = userDetailServices;
    }

    public CombinedUserDetailsService(UserDetailsService... userDetailServices)
    {
        this(Arrays.asList(userDetailServices));
    }

    // We will try to fetch the user from each of the userDetailServices in order.
    // If we find the user in one of the services, we will return it.
    // If we don't find the user in any of the services, we will throw an exception.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return userDetailServices.stream()
            .map(service -> tryFetchUser(username, service))
            .filter(Optional::isPresent)
            .findFirst()
            .flatMap(Function.identity())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private Optional<UserDetails> tryFetchUser(String username, UserDetailsService service)
    {
        try {
            return Optional.of(service.loadUserByUsername(username));
        } catch (UsernameNotFoundException e) {
            return Optional.empty();
        }
    }
}