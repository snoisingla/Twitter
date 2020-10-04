package com.twitterclone.service;

import com.twitterclone.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet,Integer> {
}
