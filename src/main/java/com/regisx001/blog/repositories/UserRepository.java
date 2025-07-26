package com.regisx001.blog.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.domain.entities.RoleType;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
        Optional<User> findByEmail(String email);

        Optional<User> findByUsername(String username);

        // @Query(value = "SELECT DISTINCT u.* FROM users u " +
        // "JOIN user_roles ur ON u.id = ur.user_id " +
        // "JOIN roles r ON r.id = ur.role_id " +
        // "WHERE (:searchTerm IS NULL OR u.username ILIKE CONCAT('%', CAST(:searchTerm
        // AS text), '%')) " +
        // "AND (:role IS NULL OR r.name = CAST(:role AS text)) " +
        // "AND (:enabled IS NULL OR u.enabled = :enabled)", nativeQuery = true)
        // Page<User> findAllBySearchAndRoleAndEnabled(
        // @Param("searchTerm") String searchTerm,
        // @Param("role") String role,
        // @Param("enabled") Boolean enabled,
        // Pageable pageable);

        @Query("SELECT DISTINCT u FROM User u " +
                        "JOIN u.roles r " +
                        "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) "
                        +
                        "AND (:role IS NULL OR r.name = :role) " +
                        "AND (:enabled IS NULL OR u.enabled = :enabled)")
        Page<User> findAllBySearchAndRoleAndEnabled(
                        @Param("searchTerm") String searchTerm,
                        @Param("role") RoleType role,
                        @Param("enabled") Boolean enabled,
                        Pageable pageable);

}
