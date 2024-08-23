package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/admin_login")
    public String showAdminLoginForm() {
        return "admin_login"; // You should have an admin login page similar to student login
    }
}
