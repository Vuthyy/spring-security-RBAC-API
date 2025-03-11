package kh.com.wingbank.eco.auth.model;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;
    private String password;
}
