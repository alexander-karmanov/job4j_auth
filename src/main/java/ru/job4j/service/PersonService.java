package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public List<Person> findAll() {
        return (List<Person>) repository.findAll();
    }

    public Optional<Person> findById(int id) {
        return repository.findById(id);
    }

    public Person create(Person person) {
        return repository.save(person);
    }

    public Person update(Person person) {
        return repository.save(person);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

}
