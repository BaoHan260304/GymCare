package base.api.controller.web;

import base.api.dto.request.RegisterRequest;
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
        return "login"; // Renders src/main/resources/templates/login.html
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register"; // Renders src/main/resources/templates/register.html
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute RegisterRequest registerRequest, Model model) {
        try {
            authService.registerCustomer(
                    registerRequest.getName(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getMobile(),
                    registerRequest.getBirthday().toString(),
                    registerRequest.getIdentityCard(),
                    registerRequest.getLicenceNumber(),
                    registerRequest.getLicenceDate().toString()
            );
            return "redirect:/login?registered=true"; // Redirect to login page with success message
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // Stay on register page with error
        }
    }
}