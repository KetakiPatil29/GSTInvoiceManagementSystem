package com.gstinvoice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gstinvoice.entity.Role;
import com.gstinvoice.entity.User;
import com.gstinvoice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class UserController {
	private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new User.
    @Operation(
    		 summary = "Create new User",
    		 description = "")
    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
    	User newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }

    // Get all User.
     @Operation(
    		 summary = "Fetach all User",
    		 description = "")
    @GetMapping("/users")
    public List<User> getAllUser() {
        return userService.getAllUser();
        
    }
  // Update a User by ID.
     @Operation(
    		 summary = "Update User by ID",
    		 description = "")
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable Long id, @RequestBody User user) {
    	 User updatedUser= userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }
     
  //finding users by role
     @Operation(
    		 summary = "Fetach Users by role",
    		 description = "")
     @GetMapping("/users/byRole")
     public ResponseEntity<List<User>> findUsersByRole(@Valid @RequestParam("role") Role role) {
         List<User> users = userService.findUsersByRole(role);
         return ResponseEntity.ok(users);
     }
}
