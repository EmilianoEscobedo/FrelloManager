package com.laingard.FrelloManager.security.jwt;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthCredentials {
    private String username;
    private String password;
}
