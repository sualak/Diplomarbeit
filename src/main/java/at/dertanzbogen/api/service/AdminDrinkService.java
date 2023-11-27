package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.Course.Course;
import at.dertanzbogen.api.domain.main.Course.CourseBaseEntity;
import at.dertanzbogen.api.domain.main.User.Drink;
import at.dertanzbogen.api.domain.main.error.CourseNotFoundException;
import at.dertanzbogen.api.factories.DrinkFactory;
import at.dertanzbogen.api.persistent.DrinkRepository;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.DrinkViewMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AdminDrinkService {

    private final DrinkRepository drinkRepository;
    private final DrinkViewMapper drinkViewMapper = DrinkViewMapper.INSTANCE;

    private final Logger LOGGER = Logger.getLogger(AdminDrinkService.class.getName());

    public Views.DrinkView createDrink(Commands.DrinkCreationCommand drinkCreationCommand) {
        return drinkViewMapper.convert(drinkRepository.save(DrinkFactory.of(drinkCreationCommand)));
    }

    public Views.DrinkView updateDrink(Commands.DrinkUpdateCommand drinkUpdateCommand) {
        LOGGER.info("Updating Course with id: " + drinkUpdateCommand.id());
        Drink drink = drinkRepository.findById(drinkUpdateCommand.id()).orElseThrow(() -> new CourseNotFoundException(drinkUpdateCommand.id()));
        boolean isCourseChanged = false;
        for (var field : drinkUpdateCommand.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(drinkUpdateCommand) != null && !field.get(drinkUpdateCommand).toString().isEmpty()) {
                    switch (field.getName()) {
                        case "name" -> drink.setName(drinkUpdateCommand.name());
                        case "price" -> drink.setPrice(drinkUpdateCommand.price());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return drinkViewMapper.convert(drinkRepository.save(drink));
    }

    public void deleteDrink(String id) {
        LOGGER.info("Deleting Drink with id: " + id);
        drinkRepository.deleteById(id);
    }
}
