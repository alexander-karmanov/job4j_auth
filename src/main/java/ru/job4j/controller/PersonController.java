package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.personService.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Person person) {
        try {
            Person created = this.personService.create(person);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Пользователь с таким логином уже существует");
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody Person person) {
        ResponseEntity<?> response;
        try {
            if (this.personService.update(person)) {
                response = ResponseEntity.ok().build();
            } else {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
            }
        } catch (RuntimeException e) {
            response = ResponseEntity.badRequest().body("Пользователь с таким логином уже существует");
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        ResponseEntity<Void> response;
        try {
            if (this.personService.findById(id).isEmpty()) {
                response = ResponseEntity.notFound().build();
            } else {
                this.personService.delete(id);
                response = ResponseEntity.ok().build();
            }
        } catch (RuntimeException e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }
}
