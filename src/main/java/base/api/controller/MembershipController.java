package base.api.controller;

import base.api.model.Membership;
import base.api.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manager/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public String listMemberships(Model model) {
        model.addAttribute("memberships", membershipService.findAll());
        return "manager/memberships";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("membership", new Membership());
        model.addAttribute("pageTitle", "Tạo Gói Tập Mới");
        return "manager/membership-form";
    }

    @PostMapping("/save")
    public String saveMembership(@ModelAttribute("membership") Membership membership, RedirectAttributes ra) {
        membershipService.save(membership);
        ra.addFlashAttribute("successMessage", "Gói tập đã được lưu thành công!");
        return "redirect:/manager/memberships";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        return membershipService.findById(id)
                .map(membership -> {
                    model.addAttribute("membership", membership);
                    model.addAttribute("pageTitle", "Chỉnh Sửa Gói Tập (ID: " + id + ")");
                    return "manager/membership-form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("errorMessage", "Không tìm thấy gói tập với ID: " + id);
                    return "redirect:/manager/memberships";
                });
    }
}