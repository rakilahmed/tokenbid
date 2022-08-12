package com.tokenbid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.User;
import com.tokenbid.repositories.UserRepository;

@Service
public class UserService implements IService<User> {
    private UserRepository userRepository;
    private EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public int add(User user) {
        User newUser = userRepository.save(user);
        String activationLink = "http://localhost:8080/users/" + newUser.getUserId() + "/verify";
        String message = buildRegistrationEmail(newUser.getFirstName(), newUser.getLastName(), activationLink);
        emailService.sendHTMLEmail(newUser.getEmail(), "Welcome to TokenBid!", message);
        return newUser.getUserId();
    }

    @Override
    public void update(User user) {
        if (userRepository.findById(user.getUserId()).isPresent()) {
            userRepository.save(user);
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
     * If the user is found, and not verified already, it verifies the email and add
     * 250 free tokens to the user's account.
     * 
     * @param userId The user's id.
     * @return True if the user was found and verified, false otherwise.
     */
    public boolean verifyUserAndAddFreeTokens(int userId) {
        if (userRepository.findById(userId).isPresent() &&
                !userRepository.findById(userId).get().isEmailVerified()) {
            User user = getById(userId);
            user.setEmailVerified(true);
            user.setTokens(250);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private String buildRegistrationEmail(String firstName, String lastName, String activationLink) {
        return "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em;\">" +
                    "<p style=\"margin: 0; padding: 0; text-align: center;\">Confirm your email</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0;\">" +
                    "<p style=\"margin-top: 1.5em;\">Hi " + firstName + " " + lastName + ",</p>" +
                    "<p style=\"margin-top: 1.5em;\">Thank you for registering with TokenBid. Please click on the link below to activate your account:</p>" +
                    "<p style=\"margin-top: 1.5em; margin-left: 2em; background-color: #ddd; padding: 0.5em;\"><a href=" + activationLink + ">" + activationLink + "</a></p>" +
                "</div>";
    }
}
