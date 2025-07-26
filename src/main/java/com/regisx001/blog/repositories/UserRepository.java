package com.regisx001.blog.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
        Optional<User> findByEmail(String email);

        Optional<User> findByUsername(String username);

        @Query("SELECT u FROM User u JOIN u.roles r WHERE " +
                        "(:role IS NULL OR r.name = :role) AND " +
                        "(:enabled IS NULL OR u.enabled = :enabled)")
        Page<User> findAllByRoleAndEnabled(@Param("role") RoleType role,
                        @Param("enabled") Boolean enabled,
                        Pageable pageable);

}
