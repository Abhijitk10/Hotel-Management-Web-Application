package com.avin.HotelBookingApplication.security;

import com.avin.HotelBookingApplication.model.Role;
import com.avin.HotelBookingApplication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        for (String roleName : roles) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Initialized role: " + roleName);
            }
        }
    }
}