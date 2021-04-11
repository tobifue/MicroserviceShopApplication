package com.tobi.user.repository;

import com.tobi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);
}
