package com.twitterclone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;

@Entity
public class AuthToken {

    @Id
    private String email;
    private String authToken;
    @JsonIgnore
    private Timestamp expiry;

    public AuthToken() {
    }

    public AuthToken(String email, String authToken, Timestamp expiry) {
        this.email = email;
        this.authToken = authToken;
        this.expiry = expiry;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Timestamp getExpiry() {
        return expiry;
    }

    public void setExpiry(Timestamp expiry) {
        this.expiry = expiry;
    }

}
