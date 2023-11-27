package at.dertanzbogen.api.domain.main.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordTest {

    private Password p;


    //Passwort muss zwischen 8 und 20 zeichen haben
    //Passwort muss mindesten einen buchstaben und eine Zahl haben
    @Test
    void setPassword()
    {
        p = new Password("Warcraft12");
        p = new Password("Warcraft12Warcraft12");
        p = new Password("A1234567");
    }

//    @Test
//    void setPasswordFails()
//    {
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> p = new Password(""));
//        assertEquals(" ist kein valides Passwort",e.getMessage());
//        e = assertThrows(IllegalArgumentException.class, ()-> p = new Password("Warcraft12Warcraft123"));
//        assertEquals("Warcraft12Warcraft123 ist kein valides Passwort",e.getMessage());
//        e = assertThrows(IllegalArgumentException.class, ()-> p = new Password("A123456"));
//        assertEquals("A123456 ist kein valides Passwort",e.getMessage());
//    }
}
