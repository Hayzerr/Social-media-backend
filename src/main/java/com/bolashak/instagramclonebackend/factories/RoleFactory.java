package com.bolashak.instagramclonebackend.factories;

import com.bolashak.instagramclonebackend.exception.RoleNotFoundException;
import com.bolashak.instagramclonebackend.model.ERole;
import com.bolashak.instagramclonebackend.model.Role;
import com.bolashak.instagramclonebackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {
    @Autowired
    RoleRepository roleRepository;

    public Role getInstance(String role) throws RoleNotFoundException {
        switch (role) {
            case "user" -> {
                return roleRepository.findByName(ERole.ROLE_USER);
            }
            case "service" -> {
                return roleRepository.findByName(ERole.ROLE_SERVICE);
            }
            default -> throw  new RoleNotFoundException("No role found for " +  role);
        }
    }
}
