package com.gstinvoice.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gstinvoice.Repository.UserRepository;
import com.gstinvoice.entity.User;
import com.gstinvoice.model.request.LoginRequest;
import com.gstinvoice.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
	 @Autowired
	 private UserRepository userRepository;

	 @PostMapping("/login")
	    public ResponseEntity<?> login(@Valid  @RequestBody LoginRequest request) {
	        Optional<User> userOpt = userRepository.findByEmailAndPasswordAndRole(
	            request.getEmail(), request.getPassword(), request.getRole()
	        );
	        
	        System.out.println("Login attempt: " + request.getEmail() + ", " 
	        + request.getPassword() + ", " + request.getRole());

	        if (userOpt.isPresent()) {
	            return ResponseEntity.ok("Login successful");
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	        }
	        
	    }
}
