package com.basic.security.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = null;
		try {
			logger.debug("inMyUserDetailsServiceside loadUserByUsername Start: " + username );
			
			userOptional = userRepository.findByUsername( username );
			
			logger.debug("MyUserDetailsService loadUserByUsername from DB : " + userOptional.get().toString() );
			
			userOptional.orElseThrow( () -> new UsernameNotFoundException( "Not Found : " + username ) );

		    User user = null;
			if( !userOptional.isPresent() ) {
				throw new UsernameNotFoundException( "UserName Not Found : " + username );
			}
			user = userOptional.get();
			
			List<GrantedAuthority> userRolesFromDB = 
					AuthorityUtils.commaSeparatedStringToAuthorityList( user.getRoles());
			
			final MyUserDetails myUserDetails = new MyUserDetails( 
					username, "fake", userRolesFromDB );			
			myUserDetails.setActive( user.isActive() );			
			
			return myUserDetails;
			
		}catch( Exception e ) {
			logger.error("loadUserByUsername : " + e.getStackTrace());
			throw e;
		}
		
	}
}
