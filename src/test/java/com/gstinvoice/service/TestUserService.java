package com.gstinvoice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import com.gstinvoice.Repository.UserRepository;
import com.gstinvoice.entity.Role;
import com.gstinvoice.entity.User;


public class TestUserService {
	@Mock
	private UserRepository userRepository;
	
	 @InjectMocks
	 private UserService userservice;
	 
	 @Mock
	 private User customer;
	 
	 @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }
	 
	 @Test
		void TestsaveUser() {
			User user = new User();
			user.setId(1L);
			user.setCreated_at(LocalDateTime.parse("30 Oct"));
			user.setEmail("k@gmail.com");
			user.setPassword("12345678");
			user.setRole("Admin");
			user.setUserName("Ketaki");
			
			assertEquals(1,user.getId());
			 assertEquals("30 Oct",user.getCreated_at());
			 assertEquals("k@gmail.com",user.getEmail());
			 assertEquals("12345678",user.getPassword());
			 assertEquals("Admin",user.getRole());
			 assertEquals("Ketaki",user.getUserName());
		}
	 
	 @Test
	 void TestgetAllUser() {
		 User user = new User();
			user.setId(1L);
			user.setCreated_at(LocalDateTime.parse("30 Oct"));
			user.setEmail("k@gmail.com");
			user.setPassword("12345678");
			user.setRole("Admin");
			user.setUserName("Ketaki");

	        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
	        List<User> result = userservice.getAllUser();

	        assertEquals(1, result.size());
	        assertEquals("k@gmail.com", result.get(0).getEmail());
	 }
	 
	 @Test
	    void testUpdateUser_Success() {
	        Long userId = 1L;

	        User existingUser = new User();
	        existingUser.setId(userId);
	        existingUser.setUserName("Old Name");

	        User updatedUser = new User();
	        updatedUser.setCreated_at(LocalDateTime.parse("30 Oct"));
	        updatedUser.setEmail("k@gmail.com");
	        updatedUser.setPassword("12345678");
	        updatedUser.setEmail("k@gmail.com");
	        updatedUser.setRole("Admin");
	        updatedUser.setUserName("Ketaki");

	        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
	        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

	        User result = userservice.updateUser(userId, updatedUser);

	        assertEquals("Ketaki", result.getUserName());  
	    }

	    @Test
	    void testUpdateUser_NotFound() {
	        Long userId = 99L;
	        User updatedUser = new User();

	        when(userRepository.findById(userId)).thenReturn(Optional.empty());

	        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	            userservice.updateUser(userId, updatedUser);
	        });

	        assertEquals("User not found", exception.getMessage());
	    }
	    
	    @Test
	    void TestfindUsersByRole() {
	        User user1 = new User();
	        user1.setId(1L);
	        user1.setUserName("ketaki");
	        user1.setRole("ADMIN");

	        User user2 = new User();
	        user2.setId(2L);
	        user2.setUserName("admin2");
	        user2.setRole("ADMIN");

	        List<User> mockUsers = Arrays.asList(user1, user2);

	        when(userRepository.findByRole(Role.ADMIN)).thenReturn(mockUsers);

	        // Act
	        List<User> result = userservice.findUsersByRole(Role.ADMIN);

	        // Assert
	        assertEquals(2, result.size());
	        assertEquals("ADMIN", result.get(0).getRole());
	        assertEquals("ketaki", result.get(0).getUserName());
	    }
	 
	 
}
