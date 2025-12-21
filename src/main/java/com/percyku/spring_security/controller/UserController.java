package com.percyku.spring_security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @PostMapping("/loginUser")
    public ResponseEntity<String> login(Authentication authentication,HttpServletRequest request, HttpServletResponse response){
        String username = authentication.getName();
        System.out.println("username:"+username);
        return ResponseEntity.status(HttpStatus.OK).body("login Success!");
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> hi(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body("welcome");
    }



    @PostMapping("/registerUser")
    public ResponseEntity<String> register(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body("register ok");
    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body("update ok");
    }






}
