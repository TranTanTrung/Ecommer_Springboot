package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDto;

import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.AdminService;
import com.ecommerce.library.service.CustomerService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AdminService adminService;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }

    @GetMapping("/manageAdmin")
    public String manageAdmin(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Admin> admins = adminService.findAll();
        model.addAttribute("manageAdmin", admins);
        model.addAttribute("size", admins.size());
        return "manageAdmin";
    }

    @GetMapping("/manageCustomer")
    public String manageCustomer(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Customer> customerDtos = customerService.findAll();
        model.addAttribute("manageCustomer", customerDtos);
        model.addAttribute("size", customerDtos.size());
        return "manageCustomer";
    }

    //Profile
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("title", "Profile Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Admin admin = adminService.findByUsername(username);
        model.addAttribute("admin", admin);

        return "profile";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new AdminDto());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                              BindingResult result,
                              Model model) {

        try {

            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if (admin != null) {
                model.addAttribute("adminDto", adminDto);
                System.out.println("admin not null");
                model.addAttribute("emailError", "Your email has been registered!");
                return "register";
            }
            if (adminDto.getPassword().equals(adminDto.getRepeatPassword())) {
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                sendEmailWithImage(adminDto.getUsername(), "C:\\TIEULUANTOTNGHIEP\\Ecommerce-Springboot\\Admin\\src\\main\\resources\\static\\img\\image_sendmail_login.png");
                System.out.println("success");
                model.addAttribute("success", "Register successfully!");
                model.addAttribute("adminDto", adminDto);
            } else {
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError", "Your password maybe wrong! Check again!");
                System.out.println("password not same");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errors", "The server has been wrong!");
        }
        return "register";
    }

    public void sendEmailWithImage(String toEmail, String imagePath) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Thiết lập người nhận và chủ đề
        helper.setTo(toEmail);
        helper.setSubject("Chào mừng bạn đến với Denni Trần Shop");

        // Tạo phần văn bản của email
        helper.setText("Chúc mừng bạn đã trở thành một thành viên của đội quản trị!!!");

        // Thêm hình ảnh như là một phần nhúng
        FileSystemResource file = new FileSystemResource(new File(imagePath));
        helper.addInline("image", file);

        // Gửi email
        mailSender.send(message);
    }
}
