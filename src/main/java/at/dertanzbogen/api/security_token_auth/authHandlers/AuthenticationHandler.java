package at.dertanzbogen.api.security_token_auth.authHandlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHandler implements
    AuthenticationSuccessHandler,
    AuthenticationFailureHandler,
    LogoutSuccessHandler,
    AuthenticationEntryPoint
{
    // AuthenticationSuccessHandler
    // This is called when a user successfully authenticates.
    @Override
    public void onAuthenticationSuccess
    (
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    )
    {
        response.setStatus(HttpStatus.OK.value());
    }

    // AuthenticationFailureHandler
    // This is called when a user fails to authenticate.
    @Override
    public void onAuthenticationFailure
    (
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    )
    {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    // LogoutSuccessHandler
    // This is called when a user logs out.
    @Override
    public void onLogoutSuccess
    (
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    )
    {
        response.setStatus(HttpStatus.OK.value());
    }

    // AuthenticationEntrypoint
    // This is called when an unauthenticated user tries to access a secured resource.
    // This removes the WWW-Authenticate header from the response in case it is present.
    @Override
    public void commence
    (
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    )
    {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }


// Let's have a look at some of those Classes and their methods and the reason
// WHY we have provided our custom implementation here.

// e.g., BasicAuthenticationEntryPoint class from Spring does the following:

//    @Override
//    public void commence(
//        HttpServletRequest request,
//        HttpServletResponse response,
//        AuthenticationException authException
//     ) throws IOException
//    {

//        // WE DO NOT NEED THIS LINE !!!
//        response.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realmName + "\"");
//        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
//    }
}
