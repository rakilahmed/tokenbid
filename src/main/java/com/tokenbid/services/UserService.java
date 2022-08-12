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

    public void addTokenToUserAccount() {
        
    }

    public void tesMethod() {
        System.out.println("tesMethod");
    }
}
