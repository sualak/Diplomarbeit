package at.dertanzbogen.api.domain.main.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DrinkTest {

    Drink d;
    @BeforeEach
    void beforeEach()
    {
        d =  new Drink.DrinkBuilder()
                .setName("test")
                .setPrice(BigDecimal.valueOf(3.50))
                .build();
    }

    //name darf nicht null und nicht blank sein
    @Test
    void setName()
    {
        assertEquals("test", d.getName());
        d.setName("test2");
        assertEquals("test2", d.getName());
    }

    @Test
    void setNameFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> d.setName(""));
        assertEquals("Name darf nicht leer sein",e.getMessage());
        NullPointerException nl = assertThrows(NullPointerException.class, ()-> d.setName(null));
        assertEquals("Name darf nicht null sein",nl.getMessage());
    }

    //Preis muss zwischen 0 und 10 sein
    @Test
    void setPrice()
    {
        assertEquals(BigDecimal.valueOf(3.50), d.getPrice());
        d.setPrice(BigDecimal.valueOf(9.99));
        assertEquals(BigDecimal.valueOf(9.99), d.getPrice());
        d.setPrice(BigDecimal.valueOf(0.01));
        assertEquals(BigDecimal.valueOf(0.01), d.getPrice());
    }

    @Test
    void setPriceFails()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, ()-> d.setPrice(BigDecimal.valueOf(-0.01)));
        assertEquals("Price muss zwischen 0,00 und 10,00 sein.",e.getMessage());
        e = assertThrows(IllegalArgumentException.class, ()-> d.setPrice(BigDecimal.valueOf(10.01)));
        assertEquals("Price muss zwischen 0,00 und 10,00 sein.",e.getMessage());
    }
}
