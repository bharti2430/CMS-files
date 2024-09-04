package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isEmpty()) {
            throw new UsernameNotFoundException("Admin not found");
        }

        return admin.get();
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    public Admin findAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
    }
}
