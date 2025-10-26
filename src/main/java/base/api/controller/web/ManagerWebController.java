package base.api.controller.web;

import base.api.dto.request.TrainerCreationRequest;
import base.api.model.User;
import base.api.service.StaffService;
import base.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ManagerWebController {

    private final StaffService staffService;
    private final UserService userService; // Inject UserService

    @GetMapping("/trainers")
    public String listTrainers(Model model) {
        List<User> trainers = userService.findAllByRole("TRAINER");
        model.addAttribute("trainers", trainers);
        return "manager/trainers"; // Renders src/main/resources/templates/manager/trainers.html
    }

    @GetMapping("/trainers/new")
    public String showCreateTrainerForm(Model model) {
        model.addAttribute("trainerRequest", new TrainerCreationRequest());
        return "manager/create-trainer"; // Renders src/main/resources/templates/manager/create-trainer.html
    }

    @PostMapping("/trainers")
    public String createTrainer(@ModelAttribute TrainerCreationRequest request, RedirectAttributes redirectAttributes) {
        staffService.createTrainer(request);
        redirectAttributes.addFlashAttribute("successMessage", "Trainer account created successfully!");
        return "redirect:/manager/trainers"; // Redirect to a list of trainers (to be created)
    }
}