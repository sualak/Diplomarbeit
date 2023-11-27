package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.presentation.mappers.generic.PageMapper;
import at.dertanzbogen.api.service.UserDrinkService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user/drink")
@AllArgsConstructor
public class UserDrinkController {
    private final UserDrinkService userDrinkService;
    private final Logger LOGGER = Logger.getLogger(UserDrinkController.class.getName());
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "50";



    @GetMapping("/price")
    public String getPrice(String id, int amount) {
        LOGGER.info("Getting price for Drink with id: " + id);
        return userDrinkService.getPrice(id, amount).toString();
    }

    @GetMapping("/getAllDrinks")
    public Views.PageDomainXtoPageDTO<Views.DrinkView> getAllDrinks(@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                                    @RequestParam(defaultValue = DEFAULT_SIZE) int size) {
        LOGGER.info("Getting all drinks");

        Pageable pageable = PageRequest.of(page, size);
        return userDrinkService.getAllDrinks(pageable);
    }

    @PostMapping("/addDrinks")
    public Views.UserView addDrinks(@AuthenticationUser User user, @RequestBody String id, int amount) {
        LOGGER.info("Adding " + amount + " drinks with id: " + id);
        return userDrinkService.addDrinks(user, id, amount);
    }

    @PostMapping("/removeDrinks")
    public Views.UserView removeDrinks(@AuthenticationUser User user, @RequestBody String id, int amount) {
        LOGGER.info("Removing " + amount + " drinks with id: " + id);
        return userDrinkService.removeDrinks(user, id, amount);
    }
}
