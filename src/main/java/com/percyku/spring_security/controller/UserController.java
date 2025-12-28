package com.percyku.spring_security.controller;

import com.percyku.spring_security.dao.UserRepository;
import com.percyku.spring_security.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @PostMapping("/loginUser")
    public ResponseEntity<String> login(Authentication authentication,HttpServletRequest request, HttpServletResponse response){
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body("login Success!");
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> hi(Authentication authentication,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userEmail = "";
        String regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        Pattern pattern =Pattern.compile(regex);
        if(pattern.matcher(authentication.getName()).find()){
            userEmail= authentication.getName();
        }else{
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes =principal.getAttributes();
            userEmail =attributes.get("email").toString();
        }
        Optional<User> optionalUser =userRepository.findUserWithRoleByEmail(userEmail);
        if(!optionalUser.isPresent()){
            throw new Exception("Please check this user["+userEmail+"] is exist or not");
        }else{
            User existUser = optionalUser.get();
            System.out.println(existUser);
            return ResponseEntity.status(HttpStatus.OK).body(existUser.getUserName()+" welcome back!");
        }
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
