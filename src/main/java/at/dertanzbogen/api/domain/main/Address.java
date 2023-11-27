package at.dertanzbogen.api.domain.main;

import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.*;

import static at.dertanzbogen.api.domain.validation.Ensure.ensureNonNullNonBlankValid;
import static at.dertanzbogen.api.domain.validation.Ensure.isPositiv;

@Getter
@NoArgsConstructor
public class Address {
    private String street;
    private int houseNumber;
    private int zip;
    private String city;
    private String country;

    public Address(String street, int houseNumber, int zip, String city, String country) {
        this.street = ensureNonNullNonBlankValid(street,"Street");
        this.houseNumber = isPositiv(houseNumber,"HouseNumber");
        this.zip = isPositiv(zip, "Zip");
        this.city = ensureNonNullNonBlankValid(city, "City");
        this.country = ensureNonNullNonBlankValid(country, "Country");
    }

    public void setStreet(String street) {
        this.street = ensureNonNullNonBlankValid(street,"Street");
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = isPositiv(houseNumber,"HouseNumber");
    }

    public void setZip(int zip) {
        this.zip = isPositiv(zip, "Zip");
    }

    public void setCity(String city) {
        this.city = ensureNonNullNonBlankValid(city, "City");
    }

    public void setCountry(String country) {
        this.country = ensureNonNullNonBlankValid(country, "Country");
    }

    public static class AddressBuilder
    {
        private Address address;

        public AddressBuilder()
        {
            address = new Address();
        }

        public AddressBuilder setStreet(String street)
        {
            address.setStreet(street);
            return this;
        }
        public AddressBuilder setHouseNumber(int houseNumber)
        {
            address.setHouseNumber(houseNumber);
            return this;
        }
        public AddressBuilder setZip(int zip)
        {
            address.setZip(zip);
            return this;
        }
        public AddressBuilder setCity(String city)
        {
            address.setCity(city);
            return this;
        }
        public AddressBuilder setCountry(String country)
        {
            address.setCountry(country);
            return this;
        }

        public Address build()
        {
            return address;
        }
    }
}
