package com.percyku.spring_security.service;

import com.percyku.spring_security.dto.UserRegisterRequest;
import com.percyku.spring_security.dto.UserUpdateRequest;
import com.percyku.spring_security.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {
    int saveUser(UserRegisterRequest user);

    Optional<User> findUserWithRoleByEmail(String email);

    Optional<User> updateUser(UserUpdateRequest user);

}
