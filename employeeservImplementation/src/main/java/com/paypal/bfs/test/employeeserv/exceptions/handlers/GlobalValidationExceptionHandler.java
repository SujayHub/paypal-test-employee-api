package com.paypal.bfs.test.employeeserv.exceptions.handlers;

import java.util.AbstractMap.SimpleImmutableEntry;
import org.everit.json.schema.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalValidationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {ValidationException.class})
  protected ResponseEntity<Object> handleConflict(ValidationException ex, WebRequest request) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    return handleExceptionInternal(
        ex,
        new SimpleImmutableEntry<>("errors", ex.getAllMessages()),
        httpHeaders,
        HttpStatus.BAD_REQUEST,
        request);
  }
}
