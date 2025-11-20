package ru.job4j.repository;

import org.springframework.stereotype.Component;
import ru.job4j.domain.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStore {
    private final ConcurrentHashMap<String, Person> users = new ConcurrentHashMap<>();

    public void save(Person person) {
        if (person == null || person.getLogin() == null) {
            throw new IllegalArgumentException("Person or login cannot be null");
        }
        users.put(person.getLogin(), person);
    }

    public Person findByUsername(String login) {
        if (login == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return users.get(login);
    }

    public List<Person> findAll() {
        return new ArrayList<>(users.values());
    }
}