package com.twitterclone.service;

import com.twitterclone.models.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {

    AuthToken findByAuthToken(String authToken);

}
