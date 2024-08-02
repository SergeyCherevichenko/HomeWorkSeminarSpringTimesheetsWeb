package uz.cherevichenko.Timesheet.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.cherevichenko.Timesheet.myexception.ResourceNotFoundException;

@RestControllerAdvice(basePackageClasses = ExceptionController.class)
public class ExceptionController {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);


    }

}
