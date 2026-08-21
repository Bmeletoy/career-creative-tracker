package com.github.bmeletoy.tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> handleFailure(ProjectNotFoundException projectNotFoundException){
        String message = projectNotFoundException.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(StageNotFoundException.class)
    public ResponseEntity<String> handleStageFailure(StageNotFoundException stageNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(stageNotFoundException.getMessage());
    }

    @ExceptionHandler(ApplicationDetailsNotFoundException.class)
    public ResponseEntity<String> handleFailure(ApplicationDetailsNotFoundException applicationDetailsNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(applicationDetailsNotFoundException.getMessage());
    }

    @ExceptionHandler(DuplicateApplicationDetailsException.class)
    public ResponseEntity<String> handleFailure(DuplicateApplicationDetailsException duplicateApplicationDetails){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(duplicateApplicationDetails.getMessage());
    }
    
}
