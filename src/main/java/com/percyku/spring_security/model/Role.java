package com.percyku.spring_security.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<UserRole> user_role  =new HashSet<>();;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(Long id,String name) {
        this.id=id;
        this.name=name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<UserRole> getUser_role() {
        return user_role;
    }

    public void setUser_role(Set<UserRole> user_role) {
        this.user_role = user_role;
    }



    public void addUser_role(UserRole userRole){
        if(user_role== null){
//            user_role=new ArrayList<>();
            user_role=new HashSet<>();
        }

        user_role.add(userRole);
    }



    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
