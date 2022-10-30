package com.example.test.enetiy;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "Devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DID")
    int DID;




    @Column(name = "name",nullable = false)
    String name;

    @Column(name = "detail_Describe",length = 500,nullable = false)
    String detail;

    @Column(name = "count",nullable = false)
    int count;



    @Column(name = "RID")
    int RID;

    public Device(){}

    public Device(String name, String detail, int count) {
        this.name = name;
        this.detail = detail;
        this.count = count;
    }

    public Device(String name, String detail, int count, int RID) {
        this.name = name;
        this.detail = detail;
        this.count = count;
        this.RID = RID;
    }
}