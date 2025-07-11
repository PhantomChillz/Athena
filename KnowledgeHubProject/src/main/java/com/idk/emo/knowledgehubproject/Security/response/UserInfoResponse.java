package com.idk.emo.knowledgehubproject.Security.response;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {
    private Long id;
    private String jwtToken;

    private String username;
    private List<String> roles;

    public UserInfoResponse(Long id, String username, List<String> roles, String jwtToken) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
        this.id = id;
    }

    public UserInfoResponse(long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}