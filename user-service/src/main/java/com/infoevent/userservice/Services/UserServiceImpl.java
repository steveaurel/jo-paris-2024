package com.infoevent.userservice.Services;

import com.infoevent.userservice.clients.NotificationRestClient;
import com.infoevent.userservice.entities.User;
import com.infoevent.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional()
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());

        return userRepository.save(user);
    }

    @Override
    @Transactional()
    public User updateUser(Long userId, User userUpdate) {
        log.info("Updating user with id: {}", userId);
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(userUpdate.getFirstName());
            user.setLastName(userUpdate.getLastName());
            user.setEmail(userUpdate.getEmail());
            user.setPassword(userUpdate.getPassword());
            user.setRole(userUpdate.getRole());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + userId));
    }

    @Override
    @Transactional()
    public void deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAllByOrderByFirstName();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long userId) {
        log.info("Fetching user by id: {}", userId);
        return userRepository.findById(userId);
    }
}
