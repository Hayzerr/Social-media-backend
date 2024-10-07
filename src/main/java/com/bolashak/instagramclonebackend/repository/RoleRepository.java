package com.bolashak.instagramclonebackend.repository;

import com.bolashak.instagramclonebackend.model.ERole;
import com.bolashak.instagramclonebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
}
