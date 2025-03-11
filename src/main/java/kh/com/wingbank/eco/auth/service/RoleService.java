package kh.com.wingbank.eco.auth.service;

import kh.com.wingbank.eco.auth.model.Permission;
import kh.com.wingbank.eco.auth.model.Role;
import kh.com.wingbank.eco.auth.repository.PermissionRepository;
import kh.com.wingbank.eco.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public Role createRole(String roleName, List<String> permissionNames) {
        Role role = new Role();
        role.setName(roleName);

        Set<Permission> permissions = new HashSet<>();
        for (String permName : permissionNames) {
            Permission permission = permissionRepository.findByName(permName);
            if (permission == null) {
                permission = new Permission();
                permission.setName(permName);
                permissionRepository.save(permission);
            }
            permissions.add(permission);
        }

        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
