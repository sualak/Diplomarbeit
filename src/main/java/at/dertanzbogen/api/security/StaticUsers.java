//package at.dertanzbogen.api.security;
//
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.List;
//
//public class StaticUsers
//{
//    private static final UserDetails admin = User
//            .withUsername("admin-berni@static.user")
//            .password("{bcrypt}$2a$10$HLl5TyTSWoGINKJA6aAlp.BYNWMxn1wVstx.Zdw1Km5tRzRPT7MUq")
//            .authorities("ADMIN")
//            .build();
//
//    public static List<UserDetails> users = List.of(admin);
//}
//
////TODO Username passwort authetication filter
////TODO Basic authetication filter