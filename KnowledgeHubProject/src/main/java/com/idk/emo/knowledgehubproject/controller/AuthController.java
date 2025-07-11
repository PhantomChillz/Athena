package com.idk.emo.knowledgehubproject.controller;

import com.idk.emo.knowledgehubproject.Security.Jwt.JwtUtils;
import com.idk.emo.knowledgehubproject.Security.Services.UserDetailsImpl;
import com.idk.emo.knowledgehubproject.Security.request.LoginRequest;
import com.idk.emo.knowledgehubproject.Security.request.SignupRequest;
import com.idk.emo.knowledgehubproject.Security.response.MessageResponse;
import com.idk.emo.knowledgehubproject.Security.response.UserInfoResponse;
import com.idk.emo.knowledgehubproject.model.AppRole;
import com.idk.emo.knowledgehubproject.model.Role;
import com.idk.emo.knowledgehubproject.model.User;
import com.idk.emo.knowledgehubproject.repository.RoleRepository;
import com.idk.emo.knowledgehubproject.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest LoginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(LoginRequest.getUsername(),LoginRequest.getPassword()));
        }
        catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie cookie =jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(), roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest){
    if(userRepository.existsByUsername(signupRequest.getUsername()))
        return ResponseEntity.badRequest().body(new MessageResponse("Username is already in use"));
    if(userRepository.existsByEmail(signupRequest.getEmail()))
        return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use"));

    User user = new User(signupRequest.getUsername(),signupRequest.getEmail(),encoder.encode(signupRequest.getPassword()));

    Set<String> strRoles = signupRequest.getRole();
    Set<Role>  roles = new HashSet<>();

        if(strRoles==null){
            Role userRole = roleRepository.findByRole(AppRole.ROLE_USER)
                    .orElseThrow(()-> new RuntimeException("Role is not found"));
            roles.add(userRole);
        } else{
//            admin -> ROLE_ADMIN
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Role adminRole = roleRepository.findByRole(AppRole.ROLE_ADMIN)
                                .orElseThrow(()-> new RuntimeException("Role is not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRole(AppRole.ROLE_USER)
                                .orElseThrow(()-> new RuntimeException("Role is not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        if(authentication == null) {
            return null;
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> currentUserDetails(Authentication authentication) {
        if(authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(), roles);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(Authentication authentication){
        ResponseCookie cookie = jwtUtils.deleteJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("User logged out successfully"));
    }
}
