package com.example.test.repo;

import com.example.test.enetiy.Group;
import com.example.test.enetiy.MeetingRoom;
import com.example.test.enetiy.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    List<Record> findAllByMeetingRoom(MeetingRoom meetingRoom);

    List<Record> findAllByGroup(Group group);
}