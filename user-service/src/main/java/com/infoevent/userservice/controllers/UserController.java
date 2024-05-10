package com.infoevent.userservice.controllers;

import com.infoevent.userservice.Services.UserService;
import com.infoevent.userservice.clients.KeyGeneratorRestClient;
import com.infoevent.userservice.entities.Role;
import com.infoevent.userservice.entities.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final KeyGeneratorRestClient keyGeneratorRestClient;


    /**
     * Get a user by ID.
     *
     * @param userId the ID of the user to fetch.
     * @return the ResponseEntity containing the user, if found.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.info("API call to fetch user by id: {}", userId);
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all users.
     *
     * @return the ResponseEntity containing a list of all users.
     */
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("API call to fetch all users");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    /**
     * Create a new user.
     *
     * @param user the user information to create.
     * @return the ResponseEntity containing the created user.
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("API call to create a new user");

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        String key = keyGeneratorRestClient.getKeyGenerator();
        user.setKey(key);

        User createdUser = userService.createUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Update an existing user.
     *
     * @param userId the ID of the user to update.
     * @param user the updated user information.
     * @return the ResponseEntity containing the updated user.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {
        log.info("API call to update user with id: {}", userId);
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete a user by ID.
     *
     * @param userId the ID of the user to delete.
     * @return the ResponseEntity with HTTP status indicating the outcome.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("API call to delete user with id: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a user by email.
     *
     * @param email the email of the user to fetch.
     * @return the ResponseEntity with the user.
     */
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        log.info("API call to fetch user by email: {}", email);
        User user = userService.getByEmail(email);
        return ResponseEntity.ok(user);
    }
}
