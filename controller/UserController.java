package controller;

import model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

import javax.validation.Valid;

@RestController
public class UserController {
    private final UserService us;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService us, BCryptPasswordEncoder passwordEncoder) {
        this.us = us;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> postUser(@Valid @RequestBody User u) {
        try {
            User user = new User();
            user.setEmail(u.getEmail());
            user.setPassword(passwordEncoder.encode(u.getPassword()));
            us.save(user);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("User already exists.");
        }
        return ResponseEntity.ok("User registered succesfully");
    }
}
