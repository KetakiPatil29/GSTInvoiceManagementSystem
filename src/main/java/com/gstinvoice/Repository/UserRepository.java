package com.gstinvoice.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gstinvoice.entity.Role;
import com.gstinvoice.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 Optional<User> findByEmailAndPasswordAndRole(String email, String password, String role);
	List<User> findByRole(Role role);

	boolean existsByEmail(String email);
}
