package uz.cherevichenko.Timesheet.page;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Controller
@ControllerAdvice(basePackageClasses = PageExceptionController.class)
public class PageExceptionController {

    @GetMapping("/oops")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String oopsPage(){
        return "oops.html";
    }
    @ExceptionHandler(NoSuchElementException.class)
    public String handlerResourceNotFoundException(NoSuchElementException e){
        System.out.println(e.getMessage());
        System.err.println(e.getStackTrace());
        return "redirect:/oops";
    }

    @ExceptionHandler(Exception.class)
    public String handlerResourceNotFoundException(Exception e){
        System.out.println(e.getMessage());
        System.err.println(e.getStackTrace());
        return "oops.html";
    }
}
