package kh.com.wingbank.eco.auth.repository;

import kh.com.wingbank.eco.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
