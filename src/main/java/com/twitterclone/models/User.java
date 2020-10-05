package com.twitterclone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Indexed
@Entity
@Table(name = "users")
public class User {

    @Id
    private String email;

    @Field
    private String name;

    @Field
    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(name = "followers",
            joinColumns = @JoinColumn(name = "followerId"),
            inverseJoinColumns = @JoinColumn(name = "followedId"))
    @JsonIgnore
    private Set<User> friends;

    @ManyToMany
    @JoinTable(name = "followers",
            joinColumns = @JoinColumn(name = "followedId"),
            inverseJoinColumns = @JoinColumn(name = "followerId"))
    @JsonIgnore
    private Set<User> followers;

    @OneToMany(targetEntity= Tweet.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "postedByUser")
    @JsonIgnore
    private List<Tweet> tweets;

    public User() {
    }

    public User(String email, String name, String username, String password) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
