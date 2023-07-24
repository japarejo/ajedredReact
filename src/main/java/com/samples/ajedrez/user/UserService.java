package com.samples.ajedrez.user;

import com.samples.ajedrez.plan.Plan;
import com.samples.ajedrez.plan.PlanRepository;
import com.samples.ajedrez.plan.PlanType;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    private final PlanRepository planRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder, PlanRepository planRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.planRepository = planRepository;
    }

    @Transactional
    public void saveUser(User user) {
        Plan plan = this.planRepository.findPlanByType(PlanType.BASIC);
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setEnabled(true);
        user.setPlan(plan);
        userRepository.save(user);
    }

    public Optional<User> findUser(String username) {
        return userRepository.findById(username);
    }
    
    public boolean checkUsernameExists(String username) {
        return findUser(username).isPresent();
    }

    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void updateUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void deleteUser(User user) throws DataAccessException {
        userRepository.delete(user);
    }

}
