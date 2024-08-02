package uz.cherevichenko.Timesheet.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String helloPage(@RequestParam String username){
        return "<h2>Hello, " + username +"</h2>";
    }

    @GetMapping("/hello/{username}")
    public String helloPagePathVariable(@PathVariable String username){
        return "<h1>Hello, " + username + "!</h1>";
    }
}
