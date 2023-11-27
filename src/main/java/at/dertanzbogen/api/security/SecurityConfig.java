//package at.dertanzbogen.api.security;
//
//
//import at.dertanzbogen.api.Fakers.UserFaker;
//import at.dertanzbogen.api.persistent.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.Arrays;
///**
// * Security Configuration
// */
//@Configuration
//@EnableMethodSecurity
//@AllArgsConstructor
//public class SecurityConfig
//{
//    private final UserRepository userRepository;
//
//    @Bean
//    public PasswordEncoder passwordEncoder()
//    {
//        return PasswordEncoderFactories
//                .createDelegatingPasswordEncoder();
//    }
//
//    public DaoAuthenticationProvider dbAuthenticationProvider()
//    {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(new DaoUserDetailsService(userRepository));
//        return provider;
//    }
//
//    public DaoAuthenticationProvider inMemoryAuthenticationProvider()
//    {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(new InMemoryUserDetailsManager(StaticUsers.users));
//        return provider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManagerBean()
//    {
//        return new ProviderManager(
//                Arrays.asList(
//                        inMemoryAuthenticationProvider(),
//                        dbAuthenticationProvider()
//                )
//        );
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//    {
//        // - BASIC AUTH
//        // Authentication
//        http.httpBasic(Customizer.withDefaults())
//                .authorizeHttpRequests((authorize) -> {
//                    authorize
//                            .requestMatchers("/api/user/registration/**").permitAll()
//                            .requestMatchers("/api/user/verify/**").permitAll()
//                            .requestMatchers("/api/user/login/**").permitAll()
//                            .requestMatchers("/api/admin/registration/**").hasAuthority("ADMIN")
//                            .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN","TEACHER")
//                            .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT")
//                            .requestMatchers("/api/media/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT")
//                            .anyRequest().authenticated();
//                });
//
//        // - FORM BASED AUTH
//        // Disable cookie-based authentication
//        http.formLogin().disable();
//
//        // - CSRF
//        // Disable cross-site request forgery (CSRF)
//        http.csrf().disable();
//
//        // - CORS
//        // Disable cross site resource sharing (CORS)
//        // Browser has Same Origin Policy (SOP)
//        http.cors().disable();
//
//        // - SESSIONS
//        http.sessionManagement()
//                .sessionCreationPolicy(
//                        SessionCreationPolicy.STATELESS);
//
//        return http.build();
//    }
//}
