package com.twitterclone.service;

import com.twitterclone.models.User;

import java.util.List;

public interface CustomizedUserRepository {
    List<User> searchUser(String name);
}
