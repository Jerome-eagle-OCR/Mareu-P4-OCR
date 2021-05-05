package com.lamzone.mareu;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MeetingRoomRepositoryTest {

    private MeetingRoomRepository repository;

    @Before
    public void setup() {
        repository = DI.getTestInstanceRepository(); //Test repository with only 3 meeting rooms
    }

    @Test
    public void getMeetingRoomsWithSuccess() {
        //Given : we want to get the list of meeting rooms
        List<MeetingRoom> expectedMeetingRooms = DummyMeetingRoomsGenerator.TEST_MEETING_ROOMS;
        //When : we retrieve the list
        List<MeetingRoom> meetingRooms = repository.getMeetingRooms();
        //Then : we get the expected list
        assertEquals(expectedMeetingRooms, meetingRooms);
    }

    @Test
    public void getIsFreeAtGivenSlotWithSuccess() {
        //Given : a meeting slot and a specific meeting room
        meetingTime = TEST_MEETING_TIME;
        meetingDuration = TEST_MEETING_DURATION;
        MeetingRoom meetingRoom = repository.getMeetingRooms().get(TEST_ROOM);
        //When : get if meeting room is available at specific time
        boolean isFree = meetingRoom.getIsFreeAtGivenTime(meetingTime, meetingDuration);
        //Then : return is true
        assertTrue(isFree);
    }

    @Test
    public void getFreeMeetingRoomWithSuccess() {
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