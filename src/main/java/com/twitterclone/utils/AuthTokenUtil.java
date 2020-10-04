package com.twitterclone.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.twitterclone.models.AuthToken;
import com.twitterclone.service.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;

@Component
public class AuthTokenUtil {

    @Autowired
    private AuthTokenRepository authTokenRepository;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static int ExpiryDurationInDays = 7;


    public AuthToken addAndReturnToken(String email){
        AuthToken authToken = new AuthToken(email, generateAuthToken(), getTokenExpiryTime());
        authTokenRepository.save(authToken);
        return authToken;
    }

    public String generateAuthToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public Timestamp getTokenExpiryTime(){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.DAY_OF_WEEK, ExpiryDurationInDays);
        return new Timestamp(cal.getTime().getTime());
    }

    public boolean isTokenValid(String userAuthToken){
        AuthToken authToken = authTokenRepository.findByAuthToken(userAuthToken);
        return authToken != null && isAuthTokenValid(authToken); //check expiry time
    }

    public boolean isAuthTokenValid(AuthToken authToken){
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return (authToken.getExpiry().after(currentTime)); //expired > current
    }
}
