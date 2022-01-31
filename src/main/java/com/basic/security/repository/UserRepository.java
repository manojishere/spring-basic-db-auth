package com.basic.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basic.security.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	Optional<User> findByUsername( String username );

}
