package base.api.controller.web;

import base.api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trainer")
@PreAuthorize("hasRole('TRAINER')")
public class TrainerWebController {

    @GetMapping("/dashboard")
    public String trainerDashboard(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("username", currentUser.getUsername());
        return "trainer/dashboard"; // Renders src/main/resources/templates/customer/dashboard.html
    }
}

