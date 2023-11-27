package at.dertanzbogen.api.presentation.annotations;

import java.lang.annotation.*;

// This annotation is used to mark the parameter which should be injected with the authenticated user
// The AuthenticatedUserResolver class is used to inject the user into the controller method
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthenticationUser
{
}

