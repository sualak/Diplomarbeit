package at.dertanzbogen.api.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class HomeController {

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @GetMapping("/admin-panel")
    public String adminPanel() {
        // Das index.html von angular
        return "index.html";
    }

    @GetMapping("/user-panel")
    public String userPanel() {
        // Das index.html von angular
        return "index.html";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("stripePublicKey", stripePublicKey);
        return "checkout.html";
    }
}