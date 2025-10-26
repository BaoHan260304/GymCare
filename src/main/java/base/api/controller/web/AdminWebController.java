package base.api.controller.web;


import base.api.model.User;
import base.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWebController {

    private final UserService userService;

    // trang chủ cho admin
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        return "admin/dashboard"; // Renders src/main/resources/templates/admin/dashboard.html
    }

    // trang quản lý người dùng
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users"; // Renders src/main/resources/templates/admin/users.html
    }

    @GetMapping("/users/edit/{id}")
    public String showUpdateUsersForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("users", user);
        return "admin/user-edit"; // Renders src/main/resources/templates/admin/user-edit.html
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("users") User user) {
        // Chỉ cập nhật các trường cho phép, không cập nhật toàn bộ đối tượng để tránh lỗi bảo mật
        userService.updateUser(id, user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/inactive/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.inActiveUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer has been marked as inactive.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Could not find customer to inactive.");
        }
        return "redirect:/admin/users"; // Luôn chuyển hướng về trang danh sách
    }


}