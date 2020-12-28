package io.github.wesleyosantos91.handler;


import io.github.wesleyosantos91.exception.BandRequestException;
import io.github.wesleyosantos91.exception.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class InterceptorExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDetails> handleBadRequestException(NullPointerException ex) {

        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Check the Avro message")
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
