package com.samples.ajedrez.user;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samples.ajedrez.plan.Plan;
import com.samples.request.RegisterRequest;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<User> findUser(String username) {
        return userRepository.findById(username);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean checkUsernameExists(String username) {
        return findUser(username).isPresent();
    }

    @Transactional
    public void saveUser(User user) {
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) throws DataAccessException {
        userRepository.delete(user);
    }

    public User mapRegisterToUserEntity(RegisterRequest register, Plan plan) {
        String username = register.getUsername();
        String password = register.getPassword();

        User user = new User(username, password);
        user.setPlan(plan);

        return user;
    }

    @Transactional(readOnly = true)
    public User findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            throw new ResourceNotFoundException("Nobody authenticated!");

        return userRepository.findById(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
