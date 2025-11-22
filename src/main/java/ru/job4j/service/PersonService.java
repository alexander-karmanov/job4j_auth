package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            return repository.save(person);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
    }

    public boolean update(Person person) {
        Person existing = repository.findById(person.getId()).orElse(null);
        if (existing != null) {
            try {
                repository.save(person);
                return true;
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Не удалось обновить: нарушение целостности данных (возможно, логин уже занят): " + e.getMessage());
            }
        }
        return false;
    }

    public void delete(int id) {
        repository.deleteById(id);
    }
}
