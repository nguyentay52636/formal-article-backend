package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Create Roles if not exist
            Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
                Role role = new Role();
                role.setName("USER");
                role.setDescription("User Role");
                return roleRepository.save(role);
            });
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                Role role = new Role();
                role.setName("ADMIN");
                role.setDescription("Admin Role");
                return roleRepository.save(role);
            });

            // Create Admin (ID 1)
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setFullName("Test Admin");
            admin.setPhone("0987654321");
            admin.setRole(adminRole);
            admin.setActive(true);
            userRepository.save(admin);

            // Create User (ID 2)
            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("password"));
            user.setFullName("Test User");
            user.setPhone("0123456789");
            user.setRole(userRole);
            user.setActive(true);
            userRepository.save(user);
            
            System.out.println("Seeded default users: Admin (ID 1), User (ID 2)");
        }
    }
}
