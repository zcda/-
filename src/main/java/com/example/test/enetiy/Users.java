package com.example.test.enetiy;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    int uid;

    @Column(name = "real_name",nullable = false)
    String name;

    @Column(name = "sex",nullable = false)
    String sex;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY,targetEntity = Group.class,cascade = CascadeType.ALL)
    @JoinTable(name = "user_group",
                joinColumns = @JoinColumn(name = "uid"),
                inverseJoinColumns = @JoinColumn(name = "gid"))
    Set<Group> group;

    public Users() {
    }

    public Users(String name, String sex, String phoneNumber) {
        this.name = name;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
    }
}
