package com.percyku.spring_security.controller;

import com.percyku.spring_security.dao.UserRepository;
import com.percyku.spring_security.dto.UserRegisterRequest;
import com.percyku.spring_security.dto.UserUpdateRequest;
import com.percyku.spring_security.model.User;
import com.percyku.spring_security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Validated
@RestController
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/loginUser")
    public ResponseEntity<String> login(Authentication authentication,HttpServletRequest request, HttpServletResponse response){
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body("login Success!");
    }

    @GetMapping("/checkUserStatus")
    public ResponseEntity<User> checkUserStatus(Authentication authentication,HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        Optional<User> optionalUser =userService.findUserWithRoleByEmail(userEmail);
        if(!optionalUser.isPresent()){
            throw new Exception("Please check this user["+userEmail+"] is exist or not");
        }else{
            User existUser = optionalUser.get();
            return ResponseEntity.status(HttpStatus.OK).body(existUser);
        }
    }



    @PostMapping("/registerUser")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest member)throws Exception{

        int res = userService.saveUser(member);

        if(res ==-1){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Please check this user["+member.getEmail()+"] is exist ");
        }
        return ResponseEntity.status(HttpStatus.OK).body("register ok");
    }

    @PostMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest,HttpServletRequest request, HttpServletResponse response) throws Exception{


        Optional<User> optionalUser= userService.updateUser(userUpdateRequest);

        if(optionalUser.isEmpty()){
            throw new Exception("Please check this user["+userUpdateRequest.getEmail()+"] is exist or not");
        }

        return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
    }








}
