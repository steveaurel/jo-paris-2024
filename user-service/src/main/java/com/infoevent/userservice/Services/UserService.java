package com.infoevent.userservice.Services;

import com.infoevent.userservice.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the User service.
 * Defines operations for managing users.
 */
public interface UserService {


    /**
     * Creates a new user.
     *
     * @param user the user to create.
     * @return the created user.
     */
    User createUser(User user);

    /**
     * Updates an existing user.
     *
     * @param userId the ID of the user to update.
     * @param user the user with updated information.
     * @return the updated user.
     */
    User updateUser(Long userId, User user);

    /**
     * Deletes a user by ID.
     *
     * @param userId the ID of the user to delete.
     */
    void deleteUser(Long userId);

    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    List<User> getAllUsers();

    /**
     * Retrieves a user by ID.
     *
     * @param userId the ID of the user to be retrieved.
     * @return an Optional containing the user if found, or an empty Optional if not found.
     */
    Optional<User> getUserById(Long userId);
    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user to be retrieved.
     * @return the user with the given email.
     */
    User getByEmail(String email);
}
