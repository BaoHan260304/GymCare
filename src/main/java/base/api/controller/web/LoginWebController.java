package base.api.controller.web;

import base.api.dto.request.CustomerRegistrationRequest;
import base.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginWebController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        return "config/login"; // Renders src/main/resources/templates/login.html
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        // Use the specific DTO for customer registration
        model.addAttribute("customerRequest", new CustomerRegistrationRequest());
        return "config/register"; // Renders src/main/resources/templates/register.html
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("customerRequest") CustomerRegistrationRequest customerRequest, Model model) {
        try {
            // Call the updated service method that accepts the DTO
            authService.registerUser(customerRequest);
            return "redirect:/login?registered=true"; // Redirect to login page with success message
        } catch (IllegalStateException e) {
            // If email is already in use, show an error message
            model.addAttribute("errorMessage", e.getMessage());
            return "config/register"; // Stay on register page with error
        }
    }
}