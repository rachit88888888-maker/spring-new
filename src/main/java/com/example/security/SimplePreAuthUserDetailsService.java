package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

/**
 * Bridges the PreAuthenticatedAuthenticationProvider with our UserDetailsService.
 * We use PreAuthenticatedAuthenticationProvider because the ID token already proves
 * the external identity, and we only need to load local authorities.
 */
public class SimplePreAuthUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String username = (String) token.getPrincipal();
        return userDetailsService.loadUserByUsername(username);
    }
}
