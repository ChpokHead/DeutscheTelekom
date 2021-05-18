package com.chpok.logiweb.controller.exception;

import com.chpok.logiweb.exception.InvalidEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice(basePackages = {"com.chpok.logiweb"})
public class ControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException() {
        return "employeePage";
    }

    @ExceptionHandler(InvalidEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidEntityException() {
        return "badRequestPage";
    }
}
