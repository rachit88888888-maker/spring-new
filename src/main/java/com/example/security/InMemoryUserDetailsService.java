package com.example.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Maps external usernames from Azure AD to local users.
 */
public class InMemoryUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users = new HashMap<String, UserDetails>();

    public InMemoryUserDetailsService() {
        GrantedAuthority userRole = new SimpleGrantedAuthority("ROLE_USER");
        users.put("alice@example.com", new User("alice@example.com", "N/A", Collections.singleton(userRole)));
        users.put("bob@example.com", new User("bob@example.com", "N/A", Collections.singleton(userRole)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails details = users.get(username);
        if (details == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return details;
    }
}
