package ru.job4j.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.UserStore;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserStore userStore;

    public UserDetailsServiceImpl(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Person user = userStore.findByUsername(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + login);
        }
        return new User(user.getLogin(), user.getPassword(), Collections.emptyList());
    }
}
