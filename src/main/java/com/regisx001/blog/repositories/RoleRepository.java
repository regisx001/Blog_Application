package com.regisx001.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.regisx001.blog.domain.entities.Role;
import com.regisx001.blog.domain.entities.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType Type);
}
