package at.dertanzbogen.api.presentation;

import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class StripeWebhookController {

    private Logger logger = Logger.getLogger(StripeWebhookController.class.getName());

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

//    Stripe.apiKey = "sk_test_51Nkkc5JDmw69Skfp7qIaKXW2rG9pNgv55vVSZ4E8uQTIBHvsvExA3fs5cb1m4idq50pZHXmkhkw14wMt1fZhjcV100nawcoY7R";
    // Replace this endpoint secret with your endpoint's unique secret
    // If you are testing with the CLI, find the secret by running 'stripe listen'
    // If you are using an endpoint defined with the API or dashboard, look in your webhook settings
    // at https://dashboard.stripe.com/webhooks

    @PostMapping("/stripe-webhook")
    public String handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload) {
        Event event;
        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            logger.info("⚠️  Webhook error while parsing basic request.");
//            respons.status(200);
            return "";
        }
        if(endpointSecret != null && sigHeader != null) {
            // Only verify the event if you have an endpoint secret defined.
            // Otherwise use the basic event deserialized with GSON.
            try {
                event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecret
                );
            } catch (SignatureVerificationException e) {
                // Invalid signature
                logger.info("⚠️  Webhook error while validating signature.");
//                response.status(400);
                return "";
            }
        }
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                logger.info("Payment for " + paymentIntent.getAmount() + " succeeded.");
                // Then define and call a method to handle the successful payment intent.
                // handlePaymentIntentSucceeded(paymentIntent);
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                break;
            default:
                logger.info("Unhandled event type: " + event.getType());
                break;
        }
//        response.status(200);
        return "";
}
}
