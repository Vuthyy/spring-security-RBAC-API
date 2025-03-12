package kh.com.wingbank.eco.auth.controller;

import kh.com.wingbank.eco.auth.dto.UserRegistrationRequest;
import kh.com.wingbank.eco.auth.model.AuthenticationRequest;
import kh.com.wingbank.eco.auth.model.AuthenticationResponse;
import kh.com.wingbank.eco.auth.model.Role;
import kh.com.wingbank.eco.auth.model.UserEntity;
import kh.com.wingbank.eco.auth.repository.RoleRepository;
import kh.com.wingbank.eco.auth.repository.UserRepository;
import kh.com.wingbank.eco.auth.service.CustomUserDetailsService;
import kh.com.wingbank.eco.auth.service.RoleService;
import kh.com.wingbank.eco.auth.service.UserService;
import kh.com.wingbank.eco.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        UserEntity newUser = userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        UserEntity user = userRepository.findByUsername(authenticationRequest.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        final String jwt = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));

        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, roles));
    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Hello Admin!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userOnly() {
        return "Hello User!";
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('READ_USERS')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // Role management endpoints
    @PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRole(@RequestBody Map<String, Object> request) {
        String roleName = (String) request.get("name");
        @SuppressWarnings("unchecked")
        List<String> permissions = (List<String>) request.get("permissions");

        Role role = roleService.createRole(roleName, permissions);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @RequestBody Map<String, Object> request) {
        String roleName = (String) request.get("roleName");

        UserEntity user = userRepository.findById(userId).orElse(null);
        Role role = roleRepository.findByName(roleName);

        if (user == null || role == null) {
            return ResponseEntity.badRequest().body("User or Role not found");
        }

        user.getRoles().add(role);
        userRepository.save(user);

        return ResponseEntity.ok("Role assigned successfully");
    }
}
