package ru.kata.spring.boot_security.demo.exception;

public class UserExceptionIncorrectData {

    private String information;

    public UserExceptionIncorrectData() {
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
