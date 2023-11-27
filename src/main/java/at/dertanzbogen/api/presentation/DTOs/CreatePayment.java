package at.dertanzbogen.api.presentation.DTOs;

import at.dertanzbogen.api.domain.main.error.CourseNotFoundException;
import at.dertanzbogen.api.persistent.CourseRepository;
import at.dertanzbogen.api.service.CourseUserService;
import at.dertanzbogen.api.service.UserDrinkService;
import com.google.gson.annotations.SerializedName;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatePayment {

    @SerializedName("drinks")
    Commands.buyDrinkCommand[] drinks;

    @SerializedName("courses")
    Commands.buyCourseCommand[] courses;

    //TODO: vl mal 100 für cent
    public static Long calculateAmount(CreatePayment createPayment, CourseUserService courseUserService, UserDrinkService userDrinkService) {
//        Long amount = 0L;
//        for (Commands.buyDrinkCommand drink : createPayment.drinks) {
//            amount += userDrinkService.getPrice(drink.drinkId(), drink.amount()).longValue();
//        }
//        for (Commands.buyCourseCommand course : createPayment.courses) {
//            amount += courseUserService.getPrice(course.courseId()).longValue();
//        }
//        return amount;
        return 100L;
    }

    public Commands.buyDrinkCommand[] getDrinks() {
        return drinks;
    }

    public Commands.buyCourseCommand[] getCourses() {
        return courses;
    }

}
