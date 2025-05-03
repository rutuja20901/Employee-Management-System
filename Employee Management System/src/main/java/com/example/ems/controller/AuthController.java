package com.example.ems.controller;

import com.example.ems.model.RoleModel;
import com.example.ems.model.UserModel;
import com.example.ems.repository.UserRepository;
import com.example.ems.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔐 Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        // Authentication authentication = authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(request.getUsername(),
        // request.getPassword()));

        // UserDetails user = (UserDetails) authentication.getPrincipal();
        // return jwtUtil.generateToken(user.getUsername());

        try {

            if (request.getUsername() == null || request.getPassword() == null) {

                return ResponseEntity.badRequest().body("Username or password is missing.");
            }
            Authentication authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user.getUsername());

            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        // } catch (AuthenticationException e) {
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username
        // or password");
        // }
    }

    // 🧾 Register Endpoint
    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        if (userRepository.existsById(request.getUsername())) {
            return "User already exists!";
        }

        UserModel user = new UserModel();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleModel.ROLE_USER); // Default role

        userRepository.save(user);
        return "User registered successfully!";
    }
}
