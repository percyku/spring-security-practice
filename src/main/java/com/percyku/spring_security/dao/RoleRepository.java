package com.percyku.spring_security.dao;

import com.percyku.spring_security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String name);

    @Query("select distinct r from Role r LEFT JOIN FETCH r.user_role t where r.name = :name")
    Optional<Role> findRoleAndUserByName(@Param("name")String name);

//    @Query("select distinct r from Role r  LEFT JOIN FETCH r.user_role t LEFT JOIN FETCH t.user where r.name = :name")
//    Optional<Role> findByName(@Param("name")String name);

}
