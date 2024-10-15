package com.arwall.nosrecettes.rest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ApiSanityCheckController {

    @GetMapping("/")
    public String indexGreeting(){
        return "Hello world";
    }
}
