package com.gatech.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/messages")
    public String getMessage() {
        return "Hello FHIR!";
    }

}
