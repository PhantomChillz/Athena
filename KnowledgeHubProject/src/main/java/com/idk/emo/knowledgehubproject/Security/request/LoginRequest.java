package com.idk.emo.knowledgehubproject.Security.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;

    private String password;
}