package at.dertanzbogen.api.domain.main.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonalTest {

    private Personal p;

    @BeforeEach
    void beforEach()
    {
        p = new Personal("bernhard", "piffel");
    }

    //namen dürfen nicht null und nicht blank sein und müssen mindestens 2 zeichen haben
    @Test
    void setFirstName()
    {
        assertEquals("bernhard",p.getFirstName());
        p.setFirstName("Be");
        assertEquals("Be",p.getFirstName());
    }

    @Test
    void setFirstNameFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> p.setFirstName("a"));
        assertEquals("a ist kein valider Name",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> p.setFirstName("1"));
        assertEquals("1 ist kein valider Name",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> p.setFirstName("!"));
        assertEquals("! ist kein valider Name",e.getMessage());
    }

    //namen dürfen nicht null und nicht blank sein und müssen mindestens 2 zeichen haben
    @Test
    void setLastName()
    {
        assertEquals("piffel",p.getLastName());
        p.setLastName("Pi");
        assertEquals("Pi",p.getLastName());
    }

    @Test
    void setLastNameFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> p.setLastName("a"));
        assertEquals("a ist kein valider Name",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> p.setLastName("1"));
        assertEquals("1 ist kein valider Name",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> p.setLastName("!"));
        assertEquals("! ist kein valider Name",e.getMessage());
    }

    @Test
    void setProfilPicture()
    {

    }
}
