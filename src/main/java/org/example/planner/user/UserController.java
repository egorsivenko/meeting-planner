package org.example.planner.user;

import jakarta.servlet.http.HttpServletResponse;
import org.example.planner.auth.AuthService;
import org.example.planner.auth.exception.EmailAlreadyTakenException;
import org.example.planner.user.form.ChangeEmailForm;
import org.example.planner.user.form.ChangeFullNameForm;
import org.example.planner.user.form.ChangePasswordForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final String DELETE_ACCOUNT_MESSAGE = "Account deleted successfully";
    private static final String CHANGE_EMAIL_MESSAGE = "Email changed successfully";
    private static final String CHANGE_PASSWORD_MESSAGE = "Password changed successfully";

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/changeFullName")
    public ModelAndView changeFullNameForm() {
        ModelAndView result = new ModelAndView("user/changeFullName");
        result.addObject("changeFullNameForm", new ChangeFullNameForm());
        return result;
    }

    @PostMapping("/changeFullName")
    public ModelAndView changeFullName(@ModelAttribute ChangeFullNameForm changeFullNameForm) {
        userService.changeUserFullName(changeFullNameForm);
        return new ModelAndView("redirect:/main");
    }

    @GetMapping("/changeEmail")
    public ModelAndView changeEmailForm() {
        ModelAndView result = new ModelAndView("user/changeEmail");
        result.addObject("changeEmailForm", new ChangeEmailForm());
        return result;
    }

    @PostMapping("/changeEmail")
    public ModelAndView changeEmail(@ModelAttribute ChangeEmailForm changeEmailForm, HttpServletResponse response,
                                    RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserEmail(changeEmailForm);
        } catch (EmailAlreadyTakenException | IllegalArgumentException ex) {
            ModelAndView result = new ModelAndView("user/changeEmail");
            result.addObject("error", ex.getMessage());
            return result;
        }
        authService.logoutUser(response);
        redirectAttributes.addFlashAttribute("message", CHANGE_EMAIL_MESSAGE);
        return new ModelAndView("redirect:/auth/login");
    }

    @GetMapping("/changePassword")
    public ModelAndView changePasswordForm() {
        ModelAndView result = new ModelAndView("user/changePassword");
        result.addObject("changePasswordForm", new ChangePasswordForm());
        return result;
    }

    @PostMapping("/changePassword")
    public ModelAndView changePassword(@ModelAttribute ChangePasswordForm changePasswordForm, HttpServletResponse response,
                                       RedirectAttributes redirectAttributes) {
        userService.changeUserPassword(changePasswordForm);
        authService.logoutUser(response);
        redirectAttributes.addFlashAttribute("message", CHANGE_PASSWORD_MESSAGE);
        return new ModelAndView("redirect:/auth/login");
    }

    @PostMapping("/deleteAccount")
    public ModelAndView deleteAccount(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        userService.deleteCurrentUser();
        authService.logoutUser(response);
        redirectAttributes.addFlashAttribute("message", DELETE_ACCOUNT_MESSAGE);
        return new ModelAndView("redirect:/auth/login");
    }
}
