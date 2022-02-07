package com.basic.security.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.basic.security.model.MyUserDetails;
import com.basic.security.model.User;
import com.basic.security.repository.UserRepository;

@Service
public class MyUserDetailsContextMapper implements org.springframework.security.ldap.userdetails.UserDetailsContextMapper{

	@Autowired
	UserRepository userRepository;
	
	private Logger logger = LoggerFactory.getLogger( MyUserDetailsContextMapper.class );
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		Optional<User> user = null;
		logger.debug("MyUserDetailsContextMapper mapUserFromContext Start");
		logger.debug("MyUserDetailsContextMapper mapUserFromContext uid : " + ctx.getStringAttribute("uid"));
		
		final MyUserDetails myUserDetails = new MyUserDetails( 
				ctx.getStringAttribute("uid"), "fake", Collections.EMPTY_LIST );
		
		user = userRepository.findByUsername( ctx.getStringAttribute("uid") );
		if( user.isPresent() ) {
			logger.debug("MyUserDetailsContextMapper mapUserFromContext isActive : " + user.get().isActive());
			myUserDetails.setActive( user.get().isActive() );
		}
		
		return myUserDetails;
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		// TODO Auto-generated method stub
		
	}

}
