package base.api.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Trả về file /resources/templates/index.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Trả về file /resources/templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Trang dashboard sau khi đăng nhập
    }
}