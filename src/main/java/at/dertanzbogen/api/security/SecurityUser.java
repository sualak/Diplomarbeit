//package at.dertanzbogen.api.security;
//
//import at.dertanzbogen.api.domain.main.User.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//public class SecurityUser implements UserDetails {
//
//    private User user;
//    public SecurityUser(User user) {
//        this.user = user;
//    }
//    public User getUser() {
//        return user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(user.getUserGroup().toString()));
//    }
//    @Override
//    public String getPassword() {
//        return user.getPassword().getPassword();
//    }
//    @Override
//    public String getUsername() {
//        return user.getEmail().getEmail();
//    }
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//    @Override
//    public boolean isEnabled() {
//        return user.getAccount().isEnabled();
//    }
//
//    //    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return List.of(new SimpleGrantedAuthority(user.getUserGroup().toString()));
////    }
//}
