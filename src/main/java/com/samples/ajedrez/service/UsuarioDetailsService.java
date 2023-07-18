package com.samples.ajedrez.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.samples.ajedrez.user.Authorities;
import com.samples.ajedrez.user.UserService;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    var user = userService.findUser(username);

    if (user != null) {
      User.UserBuilder userBuilder = User.withUsername(username);
      String password = user.get().getPassword();
      userBuilder.password(password).roles(getRoles(username));
      return userBuilder.build();
    } else {
      throw new UsernameNotFoundException(username);
    }

  }

  private String[] getRoles(String username) {
    var user = userService.findUser(username);
    List<Authorities> authorities = user.get().getAuthorities();

    if (authorities != null) {
      String[] array = new String[authorities.size()];

      for (int i = 0; i < authorities.size(); i++) {
        array[i] = authorities.get(i).getAuthority();
      }

      return array;

    } else {
      String[] array = new String[1];
      array[0] = "player";

      return array;
    }

  }

}
