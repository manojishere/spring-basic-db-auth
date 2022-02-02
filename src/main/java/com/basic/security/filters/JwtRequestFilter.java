package com.basic.security.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.basic.security.service.MyUserDetailsService;
import com.basic.security.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//public class JwtRequestFilter{}


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private Logger logger = LoggerFactory.getLogger( JwtRequestFilter.class );

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            logger.debug("doFilterInternal jwt : " + jwt);
            username = jwtUtil.extractUsername(jwt);
            logger.debug("doFilterInternal username : " + username);
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	logger.debug("doFilterInternal Authentication is null : " + jwt);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            logger.debug("doFilterInternal userDetails : " + userDetails);

            if (jwtUtil.validateToken(jwt, userDetails)) {
            	logger.debug("doFilterInternal jwtUtil is valid");

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}