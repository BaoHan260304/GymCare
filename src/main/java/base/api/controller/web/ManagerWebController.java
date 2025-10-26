package base.api.controller.web;

import base.api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")

public class ManagerWebController {

    @GetMapping("/dashboard")
    public String managerDashboard(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("username", currentUser.getUsername());
        return "manager/dashboard"; // Renders src/main/resources/templates/customer/dashboard.html
    }


}