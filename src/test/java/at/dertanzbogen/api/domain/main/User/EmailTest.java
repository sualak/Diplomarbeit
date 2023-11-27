package at.dertanzbogen.api.domain.main.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTest {

    private Email email;

    //validierung nach https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
    @Test
    void Email()
    {
        email = new Email("bernhard.piffel@gmx.at");
        email = new Email("bernhard@gmx.at");
        email = new Email("bernhard.piffel@gmx.de");
    }

    @Test
    void EmailFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> email = new Email(""));
        assertEquals(" ist keine valide Email",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> email = new Email("bernhard.piffelgmx.at"));
        assertEquals("bernhard.piffelgmx.at ist keine valide Email",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> email = new Email("bernhard.piffel@gmxat"));
        assertEquals("bernhard.piffel@gmxat ist keine valide Email",e.getMessage());
    }
}
