package com.percyku.spring_security.service.imp;

import com.percyku.spring_security.dao.UserRepository;
import com.percyku.spring_security.dto.UserRegisterRequest;
import com.percyku.spring_security.model.User;
import com.percyku.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Override
    public int saveUser(UserRegisterRequest member) {
        Optional<User> optionalUser =userRepository.findUserWithRoleByEmail(member.getEmail());
        if(optionalUser.isPresent()){
            return -1;
        }
        User newUser=new User("", passwordEncoder.encode(member.getPassword()),member.getEmail(),"","");

        userRepository.save(newUser);



        return 1;
    }
}
