//package at.dertanzbogen.api.security;
//
//import at.dertanzbogen.api.persistent.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//@AllArgsConstructor
//public class DaoUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByEmailEmail(username)
//                .map(SecurityUser::new)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//}
