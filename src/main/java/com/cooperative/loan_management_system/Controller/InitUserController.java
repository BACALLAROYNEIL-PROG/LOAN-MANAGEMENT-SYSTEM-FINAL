
package com.cooperative.loan_management_system.Controller;

import com.cooperative.loan_management_system.entity.User;
import com.cooperative.loan_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InitUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/init-users")
    public String initUsers() {

        if (userRepository.count() > 0) {
            return "Users already exist";
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@email.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");

        User staff = new User();
        staff.setUsername("staff");
        staff.setEmail("staff@email.com"); 
        staff.setPassword(passwordEncoder.encode("staff123"));
        staff.setRole("STAFF");

        userRepository.save(admin);
        userRepository.save(staff);

        return "Users created successfully";
    }
}
