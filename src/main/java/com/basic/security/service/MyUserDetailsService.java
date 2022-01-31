package com.basic.security.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.basic.security.model.MyUserDetails;
import com.basic.security.model.User;
import com.basic.security.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> user = null;
		try {
			System.out.println("inside loadUserByUsername : " + userName );
			
			user = userRepository.findByUsername( userName );
			
			user.orElseThrow( () -> new UsernameNotFoundException( "Not Found : " + userName ) );
			return user.map( MyUserDetails :: new ).get();
			
		}catch( Exception e ) {
			logger.error("loadUserByUsername : " + e.getStackTrace());
			throw e;
		}
		
	}
}
