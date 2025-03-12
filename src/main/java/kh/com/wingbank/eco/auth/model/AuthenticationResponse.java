package kh.com.wingbank.eco.auth.model;

import lombok.Data;

import java.util.Set;

@Data
public class AuthenticationResponse {

    private String token;
    private Set<String> roles;

    public AuthenticationResponse(String token, Set<String> roles) {
        this.token = token;
        this.roles = roles;
    }
}
