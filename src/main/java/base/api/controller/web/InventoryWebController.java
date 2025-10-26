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
@RequestMapping("/inventory")
@PreAuthorize("hasRole('INVENTORY')")

public class InventoryWebController {
    @GetMapping("/dashboard")
    public String inventoryDashboard(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("username", currentUser.getUsername());
        return "inventory/dashboard"; // Renders src/main/resources/templates/customer/dashboard.html
    }
}
