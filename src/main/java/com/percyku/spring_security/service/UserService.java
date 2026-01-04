package com.percyku.spring_security.service;

import com.percyku.spring_security.dto.UserRegisterRequest;
import org.springframework.stereotype.Service;

public interface UserService {
    int saveUser(UserRegisterRequest user);

}
