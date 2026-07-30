package com.gstinvoice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gstinvoice.Repository.UserRepository;
import com.gstinvoice.entity.Role;
import com.gstinvoice.entity.User;

@Service
public class UserService {
	@Autowired
	private final UserRepository userRepository;

	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	// Save a User

	public User saveUser(User user) {
		if (userRepository.existsByEmail(user.getEmail())) { throw new RuntimeException("Email already exists"); }
		return userRepository.save(user);
	}
	// Get all the User.

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	// Update a User

	public User updateUser(Long id, User updatedUser) {
		Optional<User> existingUser = userRepository.findById(id);
		if (existingUser.isPresent()) {
			User user = existingUser.get();
			user.setUsername(updatedUser.getUsername());
			user.setPassword(updatedUser.getPassword());
			user.setEmail(updatedUser.getEmail());
			user.setRole(updatedUser.getRole());
			user.setCreated_at(updatedUser.getCreated_at());
			return userRepository.save(user);
		} else {
			throw new RuntimeException("User not found");
		}
	}
	
	// find by role 
    public List<User> findUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
