package ru.job4j.repository;

import org.springframework.stereotype.Component;
import ru.job4j.domain.Person;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStore {

    private final PersonRepository personRepository;

    public UserStore(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public void save(Person person) {
        if (person == null || person.getLogin() == null) {
            throw new IllegalArgumentException("Person or login cannot be null");
        }
        personRepository.save(person);
    }

    @Transactional
    public Person findByUsername(String login) {
        if (login == null) {
            return null;
        }
        Iterable<Person> allPersons = personRepository.findAll();
        for (Person p : allPersons) {
            if (login.equals(p.getLogin())) {
                return p;
            }
        }
        return null;
    }

    @Transactional
    public List<Person> findAll() {
        return (List<Person>) personRepository.findAll();
    }
}