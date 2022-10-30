package com.example.test.enetiy;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gid")
    int gid;


    @Column(name = "name",nullable = false)
    String name;

    @Column(name = "details",length = 500,nullable = false)
    String details;



    public Group(){}
    public Group(String name, String details) {
        this.name = name;
        this.details = details;
    }
}
