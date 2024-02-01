package com.example.api.DTO;

import com.example.api.Entitys.Role;

public class SignInResponse {
    private String token;
    private Role role;
    private Long id;

    public SignInResponse() {

    }

    public SignInResponse(String token, Role role, Long id) {
        this.token = token;
        this.role = role;
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public Role getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }
}
