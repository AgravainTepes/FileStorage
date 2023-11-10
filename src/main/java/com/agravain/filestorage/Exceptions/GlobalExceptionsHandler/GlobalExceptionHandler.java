package com.agravain.filestorage.Exceptions.GlobalExceptionsHandler;

import com.agravain.filestorage.Exceptions.FileExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<IncorrectFileDataException> handleException(
            IncorrectFileNameException exception) {
        IncorrectFileDataException data = new IncorrectFileDataException();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectFileDataException> handleException(
            IncorrectFileTypeException exception) {
        IncorrectFileDataException data = new IncorrectFileDataException();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<IncorrectFileDataException> handleException(
            IncorrectFileSizeException exception) {
        IncorrectFileDataException data = new IncorrectFileDataException();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler
    public ResponseEntity<IncorrectFileDataException> handleException(
            NoSuchFileException exception) {
        IncorrectFileDataException data = new IncorrectFileDataException();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }


}
