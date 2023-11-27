package at.dertanzbogen.api.domain.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address address;

    @BeforeEach
    void beforeEach() {
        address = new Address.AddressBuilder()
                .setStreet("street")
                .setHouseNumber(1)
                .setZip(1234)
                .setCity("city")
                .setCountry("country")
                .build();
    }

    @Test
    void setStreet() {
        address.setStreet("street2");
        assertEquals("street2", address.getStreet());
    }

    @Test
    void setStreetFails() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> address.setStreet(""));
        assertEquals("Street darf nicht leer sein", e.getMessage());
        NullPointerException n = assertThrows(NullPointerException.class, () -> address.setStreet(null));
        assertEquals("Street darf nicht null sein", n.getMessage());
    }

    @Test
    void setHouseNumber() {
        address.setHouseNumber(2);
        assertEquals(2, address.getHouseNumber());
    }

    @Test
    void setHouseNumberFails() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> address.setHouseNumber(-1));
        assertEquals("HouseNumber muss positiv sein", e.getMessage());
    }

    @Test
    void setZip() {
        address.setZip(12345);
        assertEquals(12345, address.getZip());
    }

    @Test
    void setZipFails() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> address.setZip(-1));
        assertEquals("Zip muss positiv sein", e.getMessage());
    }

    @Test
    void setCity() {
        address.setCity("city2");
        assertEquals("city2", address.getCity());
    }

    @Test
    void setCityFails() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> address.setCity(""));
        assertEquals("City darf nicht leer sein", e.getMessage());
        NullPointerException n = assertThrows(NullPointerException.class, () -> address.setCity(null));
        assertEquals("City darf nicht null sein", n.getMessage());
    }

    @Test
    void setCountry() {
        address.setCountry("country2");
        assertEquals("country2", address.getCountry());
    }

    @Test
    void setCountryFails() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> address.setCountry(""));
        assertEquals("Country darf nicht leer sein", e.getMessage());
        NullPointerException n = assertThrows(NullPointerException.class, () -> address.setCountry(null));
        assertEquals("Country darf nicht null sein", n.getMessage());
    }
}