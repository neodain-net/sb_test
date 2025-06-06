package com.neodain.springbootbatchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@RestController
public class SpringBootBatchDemoApplication {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot Batch Demo!";
    }
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBootBatchDemoApplication.class, args);
    }

}
