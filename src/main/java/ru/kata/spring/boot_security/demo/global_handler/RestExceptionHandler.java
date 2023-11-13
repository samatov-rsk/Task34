package ru.kata.spring.boot_security.demo.global_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kata.spring.boot_security.demo.exception.UserExceptionIncorrectData;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserExceptionIncorrectData> handleException(UserNotFoundException exception) {
        UserExceptionIncorrectData data = new UserExceptionIncorrectData();
        data.setInformation(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserExceptionIncorrectData> handleException(Exception exception) {
        UserExceptionIncorrectData data = new UserExceptionIncorrectData();
        data.setInformation(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

}
