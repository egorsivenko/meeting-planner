package org.example.planner.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.example.planner.auth.exception.EmailAlreadyTakenException;
import org.example.planner.auth.form.LoginForm;
import org.example.planner.auth.form.RegisterForm;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final String LOGIN_ERROR = "Invalid email or password";
    private static final String LOGOUT_MESSAGE = "Logged out successfully";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        ModelAndView result = new ModelAndView("auth/login");
        result.addObject("loginForm", new LoginForm());
        return result;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute LoginForm loginForm, HttpServletResponse response) {
        try {
            authService.loginUser(loginForm, response);
        } catch (AuthenticationException ex) {
            ModelAndView result = new ModelAndView("auth/login");
            result.addObject("loginError", LOGIN_ERROR);
            return result;
        }
        return new ModelAndView("redirect:/teams");
    }

    @GetMapping("/register")
    public ModelAndView registerForm() {
        ModelAndView result = new ModelAndView("auth/register");
        result.addObject("registerForm", new RegisterForm());
        return result;
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute RegisterForm registerForm, HttpServletResponse response) {
        try {
            authService.registerUser(registerForm, response);
        } catch (EmailAlreadyTakenException ex) {
            ModelAndView result = new ModelAndView("auth/register");
            result.addObject("registerError", ex.getMessage());
            return result;
        }
        return new ModelAndView("redirect:/teams");
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        authService.logoutUser(response);
        redirectAttributes.addFlashAttribute("message", LOGOUT_MESSAGE);
        return new ModelAndView("redirect:/auth/login");
    }
}
