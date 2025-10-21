package base.api.config;

import base.api.enums.UserGender;
import base.api.enums.UserRole;
import base.api.model.user.UserModel;
import base.api.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        String adminEmail = "admin@gmail.com";

        if (!userService.existedByEmail(adminEmail)) {
            UserModel admin = new UserModel();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("123123"));
            admin.setUserName("admin");
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setRole(UserRole.ADMIN);
            admin.setActive(true);
            admin.setGender(UserGender.MALE);
            admin.setAvatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYHxo84-zFT5V8l697Qq0AwX-1QtzC2lHcfK12GbQ-rtsn-gLPtfpvnHjpzTR9sPs6obQ&usqp=CAU");
            admin.setBirthDate(LocalDateTime.of(1990, 6, 15, 0, 0));
            admin.setActive(true);
            userService.createUser(admin);
            System.out.println("Default admin created: " + adminEmail);
        } else {
            System.out.println("Admin already exists");
        }
    }


}
