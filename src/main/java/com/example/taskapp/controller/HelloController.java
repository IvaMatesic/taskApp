package com.example.taskapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/private/hello")
    public String sayHelloPrivate() {
        return "Hello private!";
    }

    @GetMapping("/public/hello")
    public String sayHelloPublic() {
        return "Hello public!";
    }
}
