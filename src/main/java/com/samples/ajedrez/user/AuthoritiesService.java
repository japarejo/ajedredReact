package com.samples.ajedrez.user;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthoritiesService {

    private final AuthoritiesRepository authoritiesRepository;
    private final UserService userService;

    @Autowired
    public AuthoritiesService(AuthoritiesRepository authoritiesRepository, UserService userService) {
        this.authoritiesRepository = authoritiesRepository;
        this.userService = userService;
    }

    @Transactional
    public void saveAuthorities(Authorities authorities) throws DataAccessException {
        authoritiesRepository.save(authorities);
    }

    @Transactional
    public void saveAuthorities(String username, String role) throws DataAccessException {
        Authorities authority = new Authorities();
        Optional<User> user = userService.findUser(username);
        if (user.isPresent()) {
            authority.setUser(user.get());
            authority.setAuthority(role);
            // user.get().getAuthorities().add(authority);
            authoritiesRepository.save(authority);
        } else {
            throw new DataAccessException("User '" + username + "' not found!") {
            };
        }
    }

    public void deleteAuthorities(List<Authorities> authorities) throws DataAccessException {
        authoritiesRepository.deleteAll(authorities);
    }

}
