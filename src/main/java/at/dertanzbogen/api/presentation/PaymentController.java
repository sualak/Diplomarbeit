package at.dertanzbogen.api.presentation;

import at.dertanzbogen.api.domain.main.User.User;
import at.dertanzbogen.api.presentation.DTOs.CreatePayment;
import at.dertanzbogen.api.presentation.DTOs.CreatePaymentItem;
import at.dertanzbogen.api.presentation.DTOs.CreatePaymentResponse;
import at.dertanzbogen.api.presentation.annotations.AuthenticationUser;
import at.dertanzbogen.api.service.CourseUserService;
import at.dertanzbogen.api.service.UserDrinkService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {

    @Autowired
    CourseUserService courseUserService;
    @Autowired
    UserDrinkService userDrinkService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<CreatePaymentResponse> createPaymentIntent(@AuthenticationUser User user, @RequestBody CreatePayment createPayment)throws StripeException {
            PaymentIntentCreateParams createParams = new
                    PaymentIntentCreateParams.Builder()
                    .setCurrency("eur")
                    .setReceiptEmail(user.getEmail().getEmail())
                    .setAmount(CreatePayment.calculateAmount(createPayment, courseUserService, userDrinkService))
//                    .setAmount(100L)
                    .build();

            PaymentIntent intent = PaymentIntent.create(createParams);
            return ResponseEntity.ok(new CreatePaymentResponse(intent.getClientSecret()));
        }
}


