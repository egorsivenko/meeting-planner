package org.example.planner.user;

import org.example.planner.auth.exception.EmailAlreadyTakenException;
import org.example.planner.user.form.ChangeEmailForm;
import org.example.planner.user.form.ChangeFullNameForm;
import org.example.planner.user.form.ChangePasswordForm;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByEmail(String email) {
        return userDao.getByEmail(email).orElseThrow();
    }

    public User getCurrentUser() {
        return getByEmail(getCurrentUserEmail());
    }

    public void changeUserFullName(ChangeFullNameForm changeFullNameForm) {
        User user = getCurrentUser();
        user.setFirstName(changeFullNameForm.getFirstName());
        user.setLastName(changeFullNameForm.getLastName());
        userDao.update(user);
    }

    public void changeUserEmail(ChangeEmailForm changeEmailForm) {
        String newEmail = changeEmailForm.getNewEmail();
        User user = getCurrentUser();

        if (user.getEmail().equals(newEmail)) {
            throw new IllegalArgumentException("This is your current email address");
        }
        if (userDao.getByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyTakenException(newEmail);
        }
        user.setEmail(newEmail);
        userDao.update(user);
    }

    public void changeUserPassword(ChangePasswordForm changePasswordForm) {
        User user = getCurrentUser();
        user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
        userDao.update(user);
    }

    public void deleteCurrentUser() {
        userDao.delete(getCurrentUser().getId());
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
