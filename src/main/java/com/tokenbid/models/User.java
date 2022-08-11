package com.tokenbid.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "tokens")
    private int tokens;

    @Column(name = "email_verified")
    private boolean email_verified = false;

    /**
     * Parameterized constructor for User class.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email of the user.
     */
    public User(String username, String password, String firstName, String lastName, String email, boolean email_verified) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.email_verified = email_verified;
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email of the user.
     * @param tokens    The tokens of the user.
     */
    public User(String username, String password, String firstName, String lastName, String email, int tokens, boolean email_verified) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tokens = tokens;
        this.email_verified = email_verified;
    }

}
