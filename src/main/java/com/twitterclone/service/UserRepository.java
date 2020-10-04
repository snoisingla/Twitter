package com.twitterclone.service;
import com.twitterclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> , CustomizedUserRepository{

    boolean existsByUsername(String username);

}
