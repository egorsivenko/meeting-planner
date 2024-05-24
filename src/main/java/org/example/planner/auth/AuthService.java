package org.example.planner.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.planner.auth.exception.EmailAlreadyTakenException;
import org.example.planner.auth.form.LoginForm;
import org.example.planner.auth.form.RegisterForm;
import org.example.planner.security.jwt.JwtProvider;
import org.example.planner.user.User;
import org.example.planner.user.UserDao;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    private static final int COOKIE_MAX_AGE = 30 * 60;

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                       UserDao userDao, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void loginUser(LoginForm loginForm, HttpServletResponse response) {
        String token = generateTokenFromCreds(loginForm.getEmail(), loginForm.getPassword());
        response.addCookie(buildAuthCookie(token));
    }

    public void registerUser(RegisterForm registerForm, HttpServletResponse response) {
        String email = registerForm.getEmail();
        String password = registerForm.getPassword();

        if (userDao.getByEmail(email).isPresent()) {
            throw new EmailAlreadyTakenException(email);
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(registerForm.getFirstName())
                .lastName(registerForm.getLastName())
                .registrationDate(LocalDate.now())
                .build();
        userDao.create(user);

        String token = generateTokenFromCreds(email, password);
        response.addCookie(buildAuthCookie(token));
    }

    public void logoutUser(HttpServletResponse response) {
        response.addCookie(buildAuthCookie(null, 0));
    }

    private String generateTokenFromCreds(String email, String password) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        return jwtProvider.generateToken(authentication);
    }

    private Cookie buildAuthCookie(String token) {
        return buildAuthCookie(token, COOKIE_MAX_AGE);
    }

    private Cookie buildAuthCookie(String token, int maxAge) {
        Cookie cookie = new Cookie("Token", token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }
}
