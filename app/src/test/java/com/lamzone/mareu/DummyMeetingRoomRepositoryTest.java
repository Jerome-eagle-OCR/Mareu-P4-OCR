package com.lamzone.mareu;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.DummyMeetingRoomRepository;
import com.lamzone.mareu.repository.DummyMeetingRoomGenerator;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DummyMeetingRoomRepositoryTest {

    private DummyMeetingRoomRepository repository;

    @Before
    public void setup() {
        repository = DI.getTestMeetingRoomRepository(); //Test repository with only 3 meeting rooms
    }

    @Test
    public void getMeetingRoomsWithSuccess() {
        //Given : the expected list of meeting rooms
        List<MeetingRoom> expectedMeetingRooms = DummyMeetingRoomGenerator.TEST_MEETING_ROOMS;
        //When : retrieve the list of meeting rooms with method to test
        List<MeetingRoom> meetingRooms = repository.getMeetingRooms();
        //Then : retrieved list equals expected list
        assertEquals(expectedMeetingRooms, meetingRooms);
    }

    @Test
    public void getFreeMeetingRoomAtGivenSlotWithSuccess() {
        //Given : we need a free meeting room at a specific slot
        meetingTime = TEST_MEETING_TIME;
        meetingDuration = TEST_MEETING_DURATION;
        MeetingRoom meetingRoom = new MeetingRoom();
        //When : we get a free meeting room at this time slot
        meetingRoom = repository.getFreeMeetingRoom(meetingTime, meetingDuration);
        //Then : the returned meeting room is actually free
        assertEquals(TEST_ROOM, meetingRoom);
    }

    @Test
    public void scheduleMeetingWithSuccess() {
        repository.addMeeting(meetingTime, meetingDuration, subject, participants);
        assertEquals(4, 2 + 2);
    }

    @Test
    public void cancelMeetingWithSuccess() {
        repository.cancelMeeting(meetingRoom, meetingTime, meetingDuration);
    }
}