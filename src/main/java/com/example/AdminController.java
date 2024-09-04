package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private ComplaintService complaintService;

    private final PasswordEncoder passwordEncoder;
    
    @Lazy
    public AdminController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/admin_register")
    public String showAdminRegistrationForm() {
        return "admin_register";
    }

    @PostMapping("/admin/admin_register")
    public ModelAndView registerAdmin(@RequestParam("adminName") String name,
                                      @RequestParam("adminEmail") String email,
                                      @RequestParam("password") String password) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        adminService.saveAdmin(admin);

        return new ModelAndView("redirect:/admin/admin_login");
    }

    @GetMapping("/admin/admin_login")
    public String showAdminLoginForm() {
        return "admin_login";
    }
    
    @GetMapping("/admin/admin_dashboard")
    public String showAdminDashboard(Model model,Authentication authentication) {
        return "admin_dashboard";  
    }
    @GetMapping("/admin/allComplaints")
    public String viewAllComplaints(Model model, Authentication authentication) {
        return "all_complaints"; // This should be the name of your HTML file
    }
    @GetMapping("/admin/pendingComplaints")
    public String viewPendingComplaints(Model model) {
        model.addAttribute("complaints", complaintService.getComplaintsByStatus("Pending"));
        return "pending_complaints"; // Create a Thymeleaf template for this
    }
    @PostMapping("/admin/updateComplaintStatus")
    public ModelAndView updateComplaintStatus(@RequestParam("id") String id,
                                               @RequestParam("status") String status) {
        complaintService.updateComplaintStatus(id, status);
        return new ModelAndView("redirect:/admin/pendingComplaints");
    }
}
