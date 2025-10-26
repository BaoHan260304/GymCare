package base.api.controller.web;


import base.api.model.Customer;
import base.api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWebController {

    private final CustomerService customerService;

    // trang chủ cho admin
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        return "admin/dashboard"; // Renders src/main/resources/templates/admin/dashboard.html
    }

    // trang quản lý người dùng
    @GetMapping("/customers")
    public String manageCustomers(Model model) {
        List<Customer> customers = customerService.findAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customers"; // Renders src/main/resources/templates/admin/customers.html
    }

    @GetMapping("/customers/edit/{id}")
    public String showUpdateCustomerForm(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "admin/customer-edit"; // Renders src/main/resources/templates/admin/customer-edit.html
    }

    @PostMapping("/customers/update/{id}")
    public String updateCustomer(@PathVariable("id") Long id, @ModelAttribute("customer") Customer customer) {
        // Chỉ cập nhật các trường cho phép, không cập nhật toàn bộ đối tượng để tránh lỗi bảo mật
        customerService.updateCustomer(id, customer);
        return "redirect:/admin/customers";
    }

    @GetMapping("/customers/inactive/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.inActiveCustomer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer has been marked as inactive.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Could not find customer to inactive.");
        }
        return "redirect:/admin/customers"; // Luôn chuyển hướng về trang danh sách
    }


}