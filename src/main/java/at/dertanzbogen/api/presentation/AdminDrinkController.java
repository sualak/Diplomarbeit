package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.Commands;
import at.dertanzbogen.api.presentation.DTOs.Views;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.service.AdminDrinkService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.logging.Logger;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/admin/drink")
@AllArgsConstructor
public class AdminDrinkController {

    private final Logger LOGGER = Logger.getLogger(AdminDrinkController.class.getName());
    private final AdminDrinkService adminDrinkService;

    @PostMapping("/create")
    public Views.DrinkView createDrink(@Valid @RequestBody Commands.DrinkCreationCommand drinkCreationCommand) {
        LOGGER.info("Creating Drink with name: " + drinkCreationCommand.name());
        return adminDrinkService.createDrink(drinkCreationCommand);
    }

    @PostMapping("/update")
    public Views.DrinkView updateDrink(@Valid @RequestBody Commands.DrinkUpdateCommand drinkUpdateCommand) {
        LOGGER.info("Updating Drink with id: " + drinkUpdateCommand.id());
        return adminDrinkService.updateDrink(drinkUpdateCommand);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDrink(@PathVariable String id) {
        LOGGER.info("Deleting Drink with id: " + id);
        adminDrinkService.deleteDrink(id);
        return ResponseEntity.created(URI.create("delete/"+id)).body("Drink with "+id+" deleted");
    }
}
