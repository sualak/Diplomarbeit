package at.dertanzbogen.api.security_token_auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class StaticUsers
{
    private static final UserDetails admin = User
        .withUsername("admin-berni@gmx.at")
        .password("{bcrypt}$2a$10$HLl5TyTSWoGINKJA6aAlp.BYNWMxn1wVstx.Zdw1Km5tRzRPT7MUq")
            .authorities(at.dertanzbogen.api.domain.main.User.User.userGroup.ADMIN.toString())
          .build();


    public static List<UserDetails> users = List.of(admin);
}
