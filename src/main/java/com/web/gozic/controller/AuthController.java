package com.web.gozic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.web.gozic.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/login-success")
    public String loginSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginSuccess", true);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password/verify")
    public String verifyCredentials(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            HttpSession session,
            Model model) {
        if (userService.verifyUserCredentials(username, email)) {
            session.setAttribute("reset_username", username);
            return "auth/reset-password";
        }
        model.addAttribute("error", "Invalid username or email");
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password/reset")
    public String resetPassword(
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("reset_username");
        if (username == null) {
            return "redirect:/forgot-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/reset-password";
        }

        userService.updatePassword(username, password);
        session.removeAttribute("reset_username");
        redirectAttributes.addFlashAttribute("passwordReset", true);
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp");
            return "auth/register";
        }

        Map<String, String> errors = userService.registerUser(username, email, password);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("registrationSuccess", true);
        return "redirect:/login";
    }
}