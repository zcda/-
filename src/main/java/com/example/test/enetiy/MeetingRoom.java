package com.example.test.enetiy;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "meetingRooms")
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RID")
    int RID;



    @Column(name = "name",unique = true)
    String name;

    @Column(name = "address",nullable = false)
    String address;

    @Column(name = "Max_capacity",nullable = false)
    int max_capacity;

    @JoinColumn(name = "RID")
    @OneToMany(fetch = FetchType.LAZY,targetEntity = Device.class,cascade = CascadeType.ALL)
    List<Device> devices;

    public MeetingRoom(){}

    public MeetingRoom(String name, String address, int max_capacity) {
        this.name = name;
        this.address = address;
        this.max_capacity = max_capacity;
    }
}
