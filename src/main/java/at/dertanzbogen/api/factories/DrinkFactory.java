package at.dertanzbogen.api.factories;

import at.dertanzbogen.api.domain.main.User.Drink;
import at.dertanzbogen.api.presentation.DTOs.Commands;

public class DrinkFactory {
    public static Drink of(Commands.DrinkCreationCommand drinkCreationCommand) {
        return new Drink.DrinkBuilder()
                .setName(drinkCreationCommand.name())
                .setPrice(drinkCreationCommand.price())
                .build();
    }
}
