package at.dertanzbogen.api.presentation.annotations;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.security_token_auth.SecurityUser;
//import at.dertanzbogen.api.security.SecurityUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// This resolver is used to inject the authenticated user into the controller method
// This resolver is added in the WebMvcConfig class
public class AuthenticatedUserResolver implements HandlerMethodArgumentResolver
{
    // Whether the given method parameter is supported by this resolver.
    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        return parameter.hasParameterAnnotation(AuthenticationUser.class) &&
               parameter.getParameterType().equals(User.class);
    }

    // Resolves a method parameter into an argument value from a given request
    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    )
    {
        // Get the authentication from the SecurityContextHolder
        // (which is set by the AuthenticationFilter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return securityUser.getUser();
    }
}
