package com.lamzone.mareu;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.DummyMeetingRoomGenerator;
import com.lamzone.mareu.repository.MeetingRoomRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MeetingRoomRepositoryTest {

    private MeetingRoomRepository repository;

    @Before
    public void setup() {
        repository = DI.getTestMeetingRoomRepository(); //Test repository with only 3 meeting rooms
    }

    @Test
    public void getMeetingRoomsWithSuccess() {
        //Given : we want the list of meeting rooms
        //When : retrieve the list of meeting rooms
        //Then : retrieved list equals expected list
        List<MeetingRoom> expectedMeetingRooms = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.subList(0, 3);
        List<MeetingRoom> meetingRooms = repository.getMeetingRooms();
        assertEquals(expectedMeetingRooms, meetingRooms);
    }
/*
    @Test
    public void getFreeMeetingRoomAtGivenSlotWithSuccess() {
        //Given : we need a free meeting room at a specific slot
        //When : we get a free meeting room at this time slot
        //Then : the returned meeting room is actually free
        meetingTime = TEST_MEETING_TIME;
        meetingDuration = TEST_MEETING_DURATION;
        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom = repository.getFreeMeetingRoom(meetingTime, meetingDuration);
        assertEquals(TEST_ROOM, meetingRoom);
    }

    @Test
    public void scheduleMeetingWithSuccess() {
        repository.scheduleMeeting(meetingStartTime, meetingEndTime, meetingSubject, meetingParticipants);
        assertEquals(4, 2 + 2);
    }

    @Test
    public void cancelMeetingWithSuccess() {
        repository.cancelMeeting(meetingRoom, meetingTime, meetingDuration);
    }*/
}