package ru.ourapp.birthdayapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.ourapp.birthdayapp.model.User;
import ru.ourapp.birthdayapp.repository.UserRepository;
import ru.ourapp.birthdayapp.security.JwtUtils;
import ru.ourapp.birthdayapp.security.UserDetailsImpl;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("Получен запрос на регистрацию пользователя: {}", user.getUsername());
        
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.warn("Пользователь {} уже существует", user.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Пользователь с таким именем уже существует");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singletonList("USER"));
            User savedUser = userRepository.save(user);
            logger.info("Пользователь {} успешно зарегистрирован", user.getUsername());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("Ошибка при регистрации пользователя {}: {}", user.getUsername(), e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Ошибка при регистрации пользователя");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        logger.info("Получен запрос на вход от пользователя: {}", loginRequest.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            logger.info("Пользователь {} успешно вошел в систему", userDetails.getUsername());
            
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername()
            ));
        } catch (Exception e) {
            logger.error("Ошибка при входе пользователя {}: {}", loginRequest.getUsername(), e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Неверное имя пользователя или пароль");
            return ResponseEntity.badRequest().body(response);
        }
    }
}

class JwtResponse {
    private String token;
    private Long id;
    private String username;

    public JwtResponse(String token, Long id, String username) {
        this.token = token;
        this.id = id;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
} 