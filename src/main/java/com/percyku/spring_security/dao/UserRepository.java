package com.percyku.spring_security.dao;

import com.percyku.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    @Query("select distinct u from User u LEFT JOIN FETCH u.userDetail d LEFT JOIN FETCH u.user_role t LEFT JOIN FETCH t.role where u.id = :id ")
    Optional<User> findUserWithRoleById(@Param("id") long id);

    @Query("select distinct u from User u LEFT JOIN FETCH u.userDetail d LEFT JOIN FETCH u.user_role t LEFT JOIN FETCH t.role where u.email = :email ")
    Optional<User> findUserWithRoleByEmail(@Param("email") String email);

    @Query("select distinct u from User u LEFT JOIN FETCH u.userDetail LEFT JOIN FETCH u.user_role t LEFT JOIN FETCH t.role r where u.id = :id and r.name = :name")
    Optional<User> findUserWithRoleByIdAndRoleName(@Param("id") long id, @Param("name") String name);

}

