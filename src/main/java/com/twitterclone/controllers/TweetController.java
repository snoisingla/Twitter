package com.twitterclone.controllers;

import com.twitterclone.models.Tweet;
import com.twitterclone.models.User;
import com.twitterclone.service.AuthTokenRepository;
import com.twitterclone.service.TweetRepository;
import com.twitterclone.service.UserRepository;
import com.twitterclone.utils.AuthTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
public class TweetController {

    @Autowired
    private AuthTokenUtil authTokenUtil;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/create-post")
    public ResponseEntity createPost(@RequestBody String tweetContent, @RequestHeader String authToken) {
        boolean isTokenValid = authTokenUtil.isTokenValid(authToken);
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(tweetContent.length() > 150){
            return new ResponseEntity<>("Post length is exceeded.", HttpStatus.BAD_REQUEST);
        }

        String email = authTokenRepository.findByAuthToken(authToken).getEmail();
        User user = userRepository.findById(email).get();
        Tweet tweet = new Tweet(tweetContent, user, new Timestamp(System.currentTimeMillis()));
        tweetRepository.save(tweet);
        return new ResponseEntity<>(tweet, HttpStatus.CREATED);
    }

    @PostMapping(path = "/like-post")
    public ResponseEntity likePost(@RequestBody Integer postId, @RequestHeader String authToken){
        boolean isTokenValid = authTokenUtil.isTokenValid(authToken);
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Tweet> tweet = tweetRepository.findById(postId);
        if(!tweet.isPresent()){
            return new ResponseEntity<>("Post doesn't exists", HttpStatus.BAD_REQUEST);
        }

        String email = authTokenRepository.findByAuthToken(authToken).getEmail();
        User user = userRepository.findById(email).get();
        tweet.get().getLikedByUsers().add(user);
        tweetRepository.save(tweet.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/like-all-posts")
    public ResponseEntity likeAllPosts(@RequestBody String userId, @RequestHeader String authToken){
        boolean isTokenValid = authTokenUtil.isTokenValid(authToken);
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<User> userWhosePostsToLike = userRepository.findById(userId);
        if(!userWhosePostsToLike.isPresent()){
            return new ResponseEntity<>("User doesn't exists "+userId, HttpStatus.BAD_REQUEST);
        }
        String email = authTokenRepository.findByAuthToken(authToken).getEmail();
        User user = userRepository.findById(email).get();

        List<Tweet> tweetsToLike = userWhosePostsToLike.get().getTweets();

        tweetsToLike.forEach(tweet -> tweet.getLikedByUsers().add(user));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "feed")
    public ResponseEntity getFeed(@RequestHeader String authToken){
        boolean isTokenValid = authTokenUtil.isTokenValid(authToken);
        if(!isTokenValid){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = authTokenRepository.findByAuthToken(authToken).getEmail();
        User user = userRepository.findById(email).get();
        Set<User> friends = user.getFriends();
        List<Tweet> feed = new ArrayList<>();

        friends.forEach(friend -> {
            feed.addAll(friend.getTweets());
        });

        Comparator<Tweet> postedAtComparator = Comparator.comparing(Tweet::getPostedAt).reversed();
        Collections.sort(feed, postedAtComparator);
        return new ResponseEntity<>(feed, HttpStatus.OK);
    }
}
