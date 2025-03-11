package kh.com.wingbank.eco.auth.repository;

import kh.com.wingbank.eco.auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
