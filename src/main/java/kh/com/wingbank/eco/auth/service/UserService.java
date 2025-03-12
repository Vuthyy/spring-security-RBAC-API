package kh.com.wingbank.eco.auth.service;

import kh.com.wingbank.eco.auth.dto.UserRegistrationRequest;
import kh.com.wingbank.eco.auth.model.Role;
import kh.com.wingbank.eco.auth.model.UserEntity;
import kh.com.wingbank.eco.auth.repository.RoleRepository;
import kh.com.wingbank.eco.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String roleName = Optional.ofNullable(request.getRole()).orElse("USER");
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Invalid role specified!");
        }

        user.setRoles(Collections.singleton(role));
        return userRepository.save(user);
    }
}
