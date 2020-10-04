package com.twitterclone.controllers;

import com.twitterclone.models.*;
import com.twitterclone.service.AuthTokenRepository;
import com.twitterclone.service.TweetRepository;
import com.twitterclone.service.UserRepository;
import com.twitterclone.utils.AuthTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private AuthTokenUtil authTokenUtil;

    @Autowired
    private TweetRepository tweetRepository;

    @PostMapping(path = "/sign-up")
    public ResponseEntity signUpUser(@RequestBody UserSignUpDTO userSignUpDetails){
        String email = userSignUpDetails.getEmail();
        if(userRepository.existsById(email)){
            return new ResponseEntity<>("Email already exists.", HttpStatus.CONFLICT);
        }

        if(userRepository.existsByUsername(userSignUpDetails.getUsername())){
            return new ResponseEntity<>("Username already exists.", HttpStatus.CONFLICT);
        }

        User user = new User(userSignUpDetails.getEmail(), userSignUpDetails.getName(), userSignUpDetails.getUsername(), userSignUpDetails.getPassword());
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity loginUser(@RequestBody UserLoginDTO userLoginDetails){
        String email = userLoginDetails.getEmail();
        Optional<User> user = userRepository.findById(email);
        if(!user.isPresent() || !userLoginDetails.getPassword().equals(user.get().getPassword())){
            return new ResponseEntity<>("Incorrect Credentials", HttpStatus.CONFLICT);
        }

        AuthToken authToken = authTokenUtil.addAndReturnToken(email);
        return new ResponseEntity<>(authToken,HttpStatus.OK);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logoutUser(@RequestHeader String authToken){
        boolean isTokenValid = authTokenUtil.isTokenValid(authToken);
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String email = authTokenRepository.findByAuthToken(authToken).getEmail();
        authTokenRepository.deleteById(email);
        return new ResponseEntity<>("Logged Out",HttpStatus.OK);
    }

    @PostMapping(path = "/follow-user")
    public ResponseEntity followUser(@RequestBody String userIdToFollow, @RequestHeader String authToken){
        boolean isTokenValid = authTokenUtil.isTokenValid(authToken);
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean isFriendExists = userRepository.existsById(userIdToFollow);
        if(!isFriendExists){
            return new ResponseEntity<>("User with id {} doesn't exists "+userIdToFollow, HttpStatus.BAD_REQUEST);
        }

        String followerEmail = authTokenRepository.findByAuthToken(authToken).getEmail();
        User follower = userRepository.findById(followerEmail).get();
        User userToFollow = userRepository.findById(userIdToFollow).get();
        userToFollow.getFollowers().add(follower);
        userRepository.save(userToFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "search-user/{query}")
    public ResponseEntity searchUser(@PathVariable String query, @RequestHeader String authToken) {
        boolean isTokenValid = authTokenUtil.isTokenValid(authToken);
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<User> matchingUsers = userRepository.searchUser(query);
        return new ResponseEntity<>(matchingUsers, HttpStatus.OK);
    }
}
