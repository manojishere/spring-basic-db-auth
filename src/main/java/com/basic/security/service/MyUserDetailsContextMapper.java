package com.basic.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.basic.security.model.MyUserDetails;
import com.basic.security.model.User;
import com.basic.security.repository.UserRepository;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

@Service
public class MyUserDetailsContextMapper implements org.springframework.security.ldap.userdetails.UserDetailsContextMapper{

	@Autowired
	UserRepository userRepository;
	
	private Logger logger = LoggerFactory.getLogger( MyUserDetailsContextMapper.class );
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		Optional<User> userOptional = null;
		logger.debug("MyUserDetailsContextMapper mapUserFromContext Start username : " + username);
		logger.debug("MyUserDetailsContextMapper mapUserFromContext uid : " + ctx.getStringAttribute("uid"));
		logger.debug("MyUserDetailsContextMapper mapUserFromContext cn : " + ctx.getStringAttribute("cn"));
		logger.debug("MyUserDetailsContextMapper mapUserFromContext sn : " + ctx.getStringAttribute("sn"));

		
		logger.debug("MyUserDetailsContextMapper mapUserFromContext : " + ctx.getDn());
		logger.debug("MyUserDetailsContextMapper mapUserFromContext : " + ctx.getNameInNamespace());
		
		List<Object> list = new ArrayList<>();
	    Attributes attributes = ctx.getAttributes();
	    NamingEnumeration it = attributes.getAll();
	    try {
	      while (it.hasMore()){
	        list.add(it.next());
	        logger.debug("MyUserDetailsContextMapper it.next() : " + it.next());
	      }
	    } catch (javax.naming.NamingException e){
	      logger.error("query ldap entry attributes fail", e.getCause());
	    }
		

		
		
		//user = userRepository.findByUsername( ctx.getStringAttribute("uid") );
	    userOptional = userRepository.findByUsername( username );
	    User user = null;
		if( userOptional.isPresent() ) {
			// throw new Exception( "User not present in Database");
			user = userOptional.get();
		
		}
		
		
		List<GrantedAuthority> userRolesFromDB = 
				AuthorityUtils.commaSeparatedStringToAuthorityList( user.getRoles());
		
		final MyUserDetails myUserDetails = new MyUserDetails( 
				username, "fake", userRolesFromDB );			
		myUserDetails.setActive( user.isActive() );
		
		
		return myUserDetails;
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		// TODO Auto-generated method stub
		
	}

}
