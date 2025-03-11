package kh.com.wingbank.eco.auth.repository;

import kh.com.wingbank.eco.auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
