package ru.kata.spring.boot_security.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotUniqueException extends RuntimeException {

    public EmailNotUniqueException(String message){
        super(message);
    }
}
