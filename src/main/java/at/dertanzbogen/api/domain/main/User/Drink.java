package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.main.BaseEntity;
import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@ToString
@NoArgsConstructor
public class Drink extends BaseEntity {
    private String name;
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Ensure.ensureNonNullNonBlankValid(name, "Name");
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = Ensure.drinkPriceValid(price, "Price");
    }

    public static class DrinkBuilder {
        private Drink drink;

        public DrinkBuilder() {
            drink = new Drink();
        }

        public DrinkBuilder setName(String name) {
            drink.setName(name);
            return this;
        }

        public DrinkBuilder setPrice(BigDecimal price) {
            drink.setPrice(price);
            return this;
        }

        public Drink build() {
            return drink;
        }
    }

}
