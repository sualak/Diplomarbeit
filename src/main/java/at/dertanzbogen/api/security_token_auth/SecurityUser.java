package at.dertanzbogen.api.security_token_auth;


import at.dertanzbogen.api.domain.main.User.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails
{

    private User user;

    public SecurityUser(User user)
    {
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of(new SimpleGrantedAuthority(user.getUserGroup().toString()));
        // return granted authorities as a collection
//        return Arrays.stream(User.userGroup.values())
//                .map(userGroup -> new SimpleGrantedAuthority(userGroup.toString()))
//                .toList();
    }

    @Override
    public String getPassword()
    {
        return user.getPassword().getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getEmail().getEmail();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        // Status Code 403 error if user is not verified
        return this.user.getAccount().isEnabled();
    }
}
