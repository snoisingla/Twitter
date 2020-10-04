package com.twitterclone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tweets")
public class Tweet {

    @Id
    @GeneratedValue
    private Integer id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postedByUserId")
    private User postedByUser;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "tweetId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    @JsonIgnore
    private Set<User> likedByUsers = new HashSet<>();

    private Timestamp postedAt;

    public Tweet() {
    }

    public Tweet(String content, User postedByUser, Timestamp postedAt) {
        this.content = content;
        this.postedByUser = postedByUser;
        this.postedAt = postedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getPostedByUser() {
        return postedByUser;
    }

    public void setPostedByUser(User postedByUser) {
        this.postedByUser = postedByUser;
    }

    public Timestamp getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Timestamp postedAt) {
        this.postedAt = postedAt;
    }

    public Set<User> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Set<User> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }
}
