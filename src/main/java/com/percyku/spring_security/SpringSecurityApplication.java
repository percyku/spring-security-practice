package com.percyku.spring_security;

import com.percyku.spring_security.dao.UserRepository;
import com.percyku.spring_security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

//	@Bean
	public CommandLineRunner commandLineRunner(){
		return runner->{
			System.out.println("Hi");
//			System.out.println(userRepository.findUserWithRoleByEmail("test_2@gmail.com"));
//
			Optional<User> optionalUser = userRepository.findUserWithRoleByEmail("test_1@gmail.com");

			if(optionalUser.isPresent()){
				User user = optionalUser.get();
				System.out.println(user.getEmail());
				System.out.println(user.getUser_role());
			}


		};
	}
}
