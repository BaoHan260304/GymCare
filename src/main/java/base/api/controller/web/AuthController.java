package base.api.controller.web;

import base.api.dto.request.RegistrationRequest;
import base.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") RegistrationRequest registrationRequest, RedirectAttributes redirectAttributes) {
        // Thêm logic validation ở đây nếu cần
        accountService.registerCustomer(registrationRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}