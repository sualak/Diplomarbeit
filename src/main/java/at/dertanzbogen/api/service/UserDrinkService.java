package at.dertanzbogen.api.service;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.domain.main.error.DrinkNotFoundException;
import at.dertanzbogen.api.persistent.DrinkRepository;
import at.dertanzbogen.api.persistent.UserRepository;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.mappers.DrinkViewMapper;
import at.dertanzbogen.api.presentation.mappers.UserViewMapper;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UserDrinkService {

    private final Logger LOGGER = Logger.getLogger(UserDrinkService.class.getName());
    private final DrinkRepository drinkRepository;
    private final DrinkViewMapper drinkViewMapper = DrinkViewMapper.INSTANCE;
    private final UserRepository userRepository;
    private final UserViewMapper userViewMapper = UserViewMapper.INSTANCE;
    private static final PageMapper mapperPage = new PageMapper() {};


    public BigDecimal getPrice(String id, int amount) {
        LOGGER.info("Getting price for Drink with id: " + id);
        return drinkRepository.findById(id).orElseThrow(() -> new DrinkNotFoundException("Drink not found")).getPrice().multiply(BigDecimal.valueOf(amount));
    }

    public Views.PageDomainXtoPageDTO<Views.DrinkView> getAllDrinks(Pageable pageable) {
        LOGGER.info("Getting all drinks");
        return mapperPage.convert(drinkRepository.findAll(pageable), drinkViewMapper);
    }

    public Views.UserView addDrinks(User user, String id, int amount) {
        drinkRepository.findById(id).orElseThrow(() -> new DrinkNotFoundException("Drink not found"));
        return userViewMapper.convert(userRepository.save(user.addDrink(id, amount)));
    }

    public Views.UserView removeDrinks(User user, String id, int amount) {
        drinkRepository.findById(id).orElseThrow(() -> new DrinkNotFoundException("Drink not found"));
        return userViewMapper.convert(userRepository.save(user.removeDrink(id, amount)));
    }
}
