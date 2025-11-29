package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.exception.InvalidPersonCredentialsException;
import ru.job4j.exception.InvalidPersonIdException;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    public PersonController(PersonService personService, ObjectMapper objectMapper) {
        this.personService = personService;
        this.objectMapper = objectMapper;
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
        if (person.getLogin() == null || person.getLogin().isEmpty()) {
            throw new InvalidPersonCredentialsException("Логин не может быть пустым.");
        }
        if (person.getPassword() == null || person.getPassword().length() < 8) {
            throw new InvalidPersonCredentialsException("Пароль должен содержать не менее 8 символов.");
        }
        Person created = this.personService.create(person);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody Person person) {
        if (person == null) {
            throw new InvalidPersonCredentialsException("Объект Person не может быть null.");
        }
        if (person.getLogin() == null || person.getLogin().isEmpty()) {
            throw new InvalidPersonCredentialsException("Логин не может быть пустым.");
        }
        if (person.getPassword() == null || person.getPassword().isEmpty()) {
            throw new InvalidPersonCredentialsException("Пароль не может быть пустым.");
        }
        if (person.getPassword().length() < 8) {
            throw new InvalidPersonCredentialsException("Пароль должен содержать не менее 8 символов.");
        }
        if (this.personService.update(person)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Информация о пользователе успешно обновлена.");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new InvalidPersonIdException("Некорректный ID пользователя (должен быть положительным).");
        }
        if (this.personService.findById(id).isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Пользователь с ID " + id + " не найден.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            this.personService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Пользователь с ID " + id + " успешно удален.");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @ExceptionHandler(value = {InvalidPersonCredentialsException.class})
    public void handleInvalidPersonCredentialsException(InvalidPersonCredentialsException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", ex.getMessage());
                put("type", ex.getClass().getName());
            }
        }));
        LOGGER.error(ex.getLocalizedMessage());
    }
}
