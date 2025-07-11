package com.idk.emo.knowledgehubproject.util;

import com.idk.emo.knowledgehubproject.model.User;
import com.idk.emo.knowledgehubproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    public String getLoggedInUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return username;
    }

    public Long getLoggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long id = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username : "+username )).getUserId();
        return id;
    }

    public String getLoggedInUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String Email = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username)).getEmail();
        return Email;
    }

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
        return user;
    }

    public String getLoggedInUserRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username)).getRoles().iterator().next().getRole().name();
        return role;
    }

    public boolean isLoggedIn(){
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

}
