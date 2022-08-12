package com.tokenbid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.User;
import com.tokenbid.repositories.UserRepository;

@Service
public class UserService implements IService<User> {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int add(User item) {
        return userRepository.save(item).getUserId();
    }

    @Override
    public void update(User item) {
        if (userRepository.findById(item.getUserId()).isPresent()) {
            userRepository.save(item);
        }
    }

    @Override
    public void delete(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User getById(int id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Adds 250 tokens to user account upon successful registration of email
     * How : pass userID as method Input -> if userId exists in DB then get a user object from DB using userID -> set token 250 & email_verified as true -> update user
     * @param userId
     */
    public boolean addTokenToUserAccount(int userId) {

            if (userRepository.findById(userId).isPresent()) {
                User user = getById(userId);
                user.setTokens(250);
                user.setEmail_verified(true);
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
    }

    public void tesMethod() {
        System.out.println("tesMethod");
    }
}
