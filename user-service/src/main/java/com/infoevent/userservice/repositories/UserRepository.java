package com.infoevent.userservice.repositories;

import com.infoevent.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link User} entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user to be retrieved.
     * @return the user with the given email.
     */
    User findByEmail(String email);

    List<User> findAllByOrderByFirstName();
}
