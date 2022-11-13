package com.example.test.enetiy;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecID")
    int RecID;

    @JoinColumn(name = "rid")
    @OneToOne(fetch = FetchType.LAZY)
    MeetingRoom meetingRoom;

    @JoinColumn(name = "gid")
    @OneToOne(fetch = FetchType.LAZY)
    Group group;

    @Column(name = "startTime")
    Date startTime;

    @Column(name = "endTime")
    Date endTime;

    @Column(name = "name")
    String  name;
}
