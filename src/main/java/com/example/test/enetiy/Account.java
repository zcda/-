package com.example.test.enetiy;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int ID;


    @Column(name = "username",nullable = false)
    String username;

    @Column(name = "password",nullable = false)
    String password;

    @Column(name = "email",unique = true)
    String email;


    @Column(name = "role",nullable = false)
    String role;

    @JoinColumn(name = "uid")
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    Users accountDetail;


}
