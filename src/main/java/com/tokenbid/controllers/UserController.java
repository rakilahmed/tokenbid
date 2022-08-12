package com.tokenbid.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.tokenbid.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tokenbid.models.User;
import com.tokenbid.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController implements IController<User> {
    private UserService userService;
    private EmailService emailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<Boolean> add(@RequestBody User user) throws URISyntaxException {
        return ResponseEntity.created(new URI("/users/" + userService.add(user))).build();
    }

    @Override
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> update(@PathVariable("id") int id, @RequestBody User updatedUser) {
        if (userService.getById(id) != null) {
            updatedUser.setUserId(id);
            userService.update(updatedUser);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") int id) {
        if (userService.getById(id) != null) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<User> get(@PathVariable("id") int id) {
        User user = userService.getById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(path = "/testEmail")
    public ResponseEntity<Boolean> sendEmail() {
        emailService.sendMessage();
    }
}
