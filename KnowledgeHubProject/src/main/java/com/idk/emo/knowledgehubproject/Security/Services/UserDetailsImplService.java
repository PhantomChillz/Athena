package com.idk.emo.knowledgehubproject.Security.Services;

import com.idk.emo.knowledgehubproject.model.User;
import com.idk.emo.knowledgehubproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserDetailsImplService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User curr= userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Username not found with username: "+username));
        return UserDetailsImpl.build(curr);
    }
}
