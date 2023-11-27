package at.dertanzbogen.api.security_token_auth;


import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.security_token_auth.authHandlers.AuthenticationHandler;
import at.dertanzbogen.api.security_token_auth.persistentToken.MongoPersistentTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;



/*
    FORM BASED AUTH CONSIDERATIONS:


    What is a Session
    -----------------
    A web session, often referred to as just "session", is a mechanism used to maintain the state of a user's
    interaction with a web application over the HTTP protocol, which is stateless by nature.
    When a user logs into a web application, a session is created on the server side,
    and a unique session ID is assigned to this session. This session ID is then sent back to the client
    and usually stored in a cookie. With each subsequent request, the client sends back the session ID,
    allowing the server to look up and maintain a consistent user-specific context,
    enabling the user's actions and data to be remembered across multiple requests.
    However, when a user logs out, the session is invalidated on the server side,
    meaning all the data associated with the session is discarded and the session can no longer be referenced in future requests.
    This process helps maintain the security of the web application by preventing session hijacking,
    where an attacker reuses a user's session.
    On the client side, the cookie storing the session ID is also typically discarded upon logout,
    further securing the session from potential misuse.


    Spring HttpSession:
    -------------------
    A Spring HttpSession is created as soon as a user is authenticated using their credentials.
    This session is used to keep the user authenticated throughout their browsing session.
    The HttpSession ID is stored in a JSESSIONID cookie which is sent back and forth between the client and server.
    By default, the Spring HttpSession is stored in the server's memory.

    When the server is restarted, all session information is lost.
    In a distributed environment or for load balancing, this can be problematic as the session data is not shared among all servers.
    To address this, we can choose to persist the session data to a database or use a distributed storage system like Redis.
    We can use Spring Session to handle this for us. Spring Session provides API and implementations for
    managing a user’s session information in a distributed environment and supports
    JDBC, Redis, Hazelcast, etc., for the backend session storage.


    Persistent Token Repository:
    ----------------------------
    The Persistent Token Repository comes into play when the user checks the "Remember Me" option during login.
    The repository stores the token that Spring Security will generate.
    This token is then sent to the user's browser as a cookie, separate from the JSESSIONID cookie.


    RememberMe:
    -----------
    If the user closes their browser, the HttpSession is invalidated and the user would have to log in again,
    unless the session ID has been persisted somewhere (like a database), which is typically not done.

    However, with "Remember Me" functionality enabled, the user's authentication can be remembered even after the browser is closed.
    This is because the "Remember Me" token (stored in the PersistentTokenRepository) is still valid,
    and the cookie containing this token is sent with the request when the user re-opens their browser and accesses the site again.

    loginPage(String loginPage)
    ---------------------------
    This method is used to specify the path to the login page.
    If this is not specified, Spring Security provides a default login page.
    If we have a custom login page (for example, a login page we have designed ourselves),
    you can specify the path to that page using this method.

    However, with a single page application (SPA) like Angular,
    we will typically handle the rendering of the login form in your SPA,
    and then POST the login data to a login processing URL.


    loginProcessingUrl(String loginProcessingUrl)
    ----------------------------------------------
    This method is used to specify the URL that the login form is posted to.
    This is the URL that your Spring Security application listens on for login requests.
    When a POST request is made to this URL with login credentials (usually a username and password),
    Spring Security will attempt to authenticate the user.

    HTTP POST /login
    ContentType: application/x-www-form-urlencoded
    Body: username=USERNAME&password=PASSWORD
*/



@Configuration
@EnableMethodSecurity
@EnableScheduling
public class SecurityConfig
{

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories
            .createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository)
    {
        return new CombinedUserDetailsService(
            new InMemoryUserDetailsManager(StaticUsers.users),
            new DatabaseUserDetailsService(userRepository)
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        MongoPersistentTokenRepository tokenRepository,
        UserDetailsService userDetailsService,
        AuthenticationHandler authenticationHandler
    ) throws Exception {

        // Configuring form-based authentication
        // -------------------------------------
        http.formLogin(configurer -> { configurer
            .loginProcessingUrl("/api/user/login")  // Specifies the URL to submit the login form
            .usernameParameter("username")          // Specifies the request parameter to use as the username
            .passwordParameter("password")          // Specifies the request parameter to use as the password
            .successHandler(authenticationHandler)  // Defines custom logic on successful authentication
            .failureHandler(authenticationHandler); // Defines custom logic when authentication fails
        });
         // --- Config for Server Side Rendering ---
         // .loginPage() // Specifies the URL to send users to if login is required
         // .successForwardUrl() // Specifies the URL to forward to after successful authentication
         // .defaultSuccessUrl() // Specifies the default URL to redirect to after successful login
         // .failureForwardUrl() // Specifies the URL to forward to after authentication failure


        // Configuring logout behavior
        // ---------------------------
        // Removing of Sessions?
        // - If a user logs out
        // - If a session remains idle (i.e., no requests from the client) for a certain duration
        //   -> in `application.properties` configure `server.servlet.session.timeout=XXXm`
        // - If a session invalidated programmatically using the HttpSession.invalidate()
        http.logout(configurer -> { configurer
            .logoutUrl("/api/user/logout") // Specifies the URL that triggers logout
            .deleteCookies("JSESSIONID") // Deletes the specified cookies on logout
            .invalidateHttpSession(true) // Invalidates the HttpSession on logout
            .clearAuthentication(true) // Clears the Authentication at the time of logout
            .logoutSuccessHandler(authenticationHandler); // Defines custom logic after a successful logout
         // --- Config for Server Side Rendering ---
         // .logoutSuccessUrl() // Specifies the URL to redirect to after logout
        })

        // Configuring "remember-me" behavior
        // ---------------------------------
        // Removing of Tokens?
        // - If a user logs out
        // - If a user sends a valid series but invalid token in the remember-me cookie
        //   -> CookieTheftException
        .rememberMe(configurer -> { configurer
            .tokenRepository(tokenRepository) // Specifies the repository to store "remember-me" tokens
            .userDetailsService(userDetailsService) // Specifies the UserDetailsService to use when a remember-me token is submitted
            .rememberMeCookieName("remember-me") // Specifies the name of the cookie to create for remember-me
            .alwaysRemember(true) // Enables "always remember" functionality
            .useSecureCookie(true) // Sets the secure flag of the remember-me cookie
            .tokenValiditySeconds((int) Duration.ofDays(30).toSeconds()) // Sets the validity period of the remember-me token in seconds (here 1 month)
            .authenticationSuccessHandler(authenticationHandler); // Defines custom logic on successful "remember-me" authentication

            // By default, cookies are only accessible to the domain that created them.
            // In some situations, we might want a cookie to be accessible to more than one domain or subdomain.
            // For instance, if we have multiple subdomains (e.g., user1.myapp.com, user2.myapp.com, etc.),
            // and we want the "remember-me" cookie to be available to all of them,
            // we can use rememberMeCookieDomain(".myapp.com").
            // .rememberMeCookieDomain()

            //  This method sets the name of the request parameter that indicates a "remember-me" login.
            //  By default, Spring Security looks for a request parameter named "_spring_security_remember_me"
            //  to determine whether the user has requested "remember-me" authentication.
            //  In our setup we have enables "always remember" functionality, so we do not need to configure this.
           // .rememberMeParameter()
        })

        // Configuring access control for URL patterns
        .authorizeHttpRequests((authorize) -> { authorize
                .requestMatchers("/api/user/registration/**").permitAll()
                            .requestMatchers("/api/user/verify/**").permitAll()
                            .requestMatchers("/api/user/login/**").permitAll()
                            .requestMatchers("/api/admin/registration/**").hasAuthority("ADMIN")
                            .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN","TEACHER")
                            .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT")
                            .requestMatchers("/api/media/**").hasAnyAuthority("ADMIN", "TEACHER", "STUDENT")
                            .requestMatchers(
                                    "/**.js",
                                    "/**.ico",
                                    "/**.html",
                                    "/**.css",
                                    "/admin-panel",
                                    "/admin-panel/**",
                                    "/user-panel",
                                    "/user-panel/**",
                                    "/checkout",
                                    "/checkout/**")
                            .permitAll()


//                            .requestMatchers("/private/user/**").hasRole("USER")
//                            .requestMatchers("/private/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
        });

        // Configuring exception handling
        http.exceptionHandling(configurer -> {
            configurer.authenticationEntryPoint(authenticationHandler);
        });

        // Disabling HTTP Basic Authentication
        http.httpBasic().disable();

        // Disabling Cross-Site Request Forgery (CSRF) protection
        // TODO CSRF protection is disabled for now, but should be enabled soon
        http.csrf().disable();

        // Disabling Cross-Origin Resource Sharing (CORS) protection
        http.cors().disable();

        // Configuring Session Management
        // In application.properties we can control when the session times out
        // server.servlet.session.timeout=30m
        http.sessionManagement()
            .sessionCreationPolicy(
                SessionCreationPolicy.IF_REQUIRED); // Specifies that a new session will be created only when required

        return http.build();
    }


}
