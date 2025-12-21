package com.percyku.spring_security.security;

import com.percyku.spring_security.dao.UserRepository;
import com.percyku.spring_security.model.User;
import com.percyku.spring_security.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser =userRepository.findUserWithRoleByEmail(username);

        if(!optionalUser.isPresent()){
            throw new UsernameNotFoundException("User not found for:" + username);
        }else{
            User existUser = optionalUser.get();
            String email = existUser.getEmail();
            String password = existUser.getPassword();
            Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(existUser.getUser_role());
            return new org.springframework.security.core.userdetails.User(email, password, authorities);


        }
    }


    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Set<UserRole> userRoles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (UserRole userRole : userRoles) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(userRole.getRole().getName());
            authorities.add(tempAuthority);
        }

        return authorities;
    }
}
