package com.basic.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;


public class MyUserDetails extends org.springframework.security.core.userdetails.User {
	


	private boolean active;

	public MyUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
	
    public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
