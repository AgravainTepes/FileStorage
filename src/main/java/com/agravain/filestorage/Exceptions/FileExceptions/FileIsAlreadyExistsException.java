package com.agravain.filestorage.Exceptions.FileExceptions;

public class FileIsAlreadyExistsException extends RuntimeException {
    public FileIsAlreadyExistsException(String message) {

        super(message);
    }
}
