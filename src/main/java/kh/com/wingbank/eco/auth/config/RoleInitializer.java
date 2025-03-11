package kh.com.wingbank.eco.auth.config;

import jakarta.annotation.PostConstruct;
import kh.com.wingbank.eco.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleService roleService;

    @PostConstruct
    public void init() {
        // Create default roles with permissions
        roleService.createRole("ADMIN", Arrays.asList(
                "CREATE_USER", "READ_USERS", "UPDATE_USER", "DELETE_USER",
                "CREATE_ROLE", "READ_ROLES", "UPDATE_ROLE", "DELETE_ROLE",
                "READ", "WRITE", "DELETE"
        ));

        roleService.createRole("USER", Arrays.asList("READ"));

        roleService.createRole("MANAGER", Arrays.asList(
                "READ_USERS", "READ", "WRITE"
        ));
    }
}
