package com.percyku.spring_security.service.imp;

import com.percyku.spring_security.dao.UserRepository;
import com.percyku.spring_security.dto.UserRegisterRequest;
import com.percyku.spring_security.dto.UserUpdateRequest;
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

    @Override
    public Optional<User> updateUser(UserUpdateRequest user) {

        Optional<User> userOptional =userRepository.findUserWithRoleByEmail(user.getEmail());
        if(userOptional.isPresent()){
            User existUser=userOptional.get();


            if(!user.getPassword().equals("")){
                existUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            existUser.setUserName(user.getUsername());
            existUser.setEmail(user.getEmail());
            existUser.setFirstName(user.getFirst_name());
            existUser.setLastName(user.getLast_name());

            if(existUser.getUserDetail()!=null){
                if(existUser.getUserDetail()!=null){
                    existUser.getUserDetail().setDesc(existUser.getUserDetail().getDesc());
                    existUser.getUserDetail().setJob(existUser.getUserDetail().getJob());
                }else{
                    existUser.getUserDetail().setUser(existUser);
                    existUser.setUserDetail(existUser.getUserDetail());
                }

            }else{
                if(existUser.getUserDetail()!=null){
                    existUser.getUserDetail().setUser(null);
                }
                existUser.setUserDetail(null);
            }


            return Optional.of(userRepository.save(existUser));

        }

        return Optional.empty();
    }
}
