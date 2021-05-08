package com.lamzone.mareu;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.DummyMeetingRoomGenerator;
import com.lamzone.mareu.repository.MeetingRoomRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MeetingRoomRepositoryTest {

    private final MeetingRoom TEST_MEETING_ROOM = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(0);
    private final MeetingRoom NEXT_TEST_MEETING_ROOM = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(1);
    private final String TEST_MEETING_SUBJECT = "Réunion test";
    private final long TEST_MEETING_START_TIME = System.currentTimeMillis() / 60000; //Convert time in minutes
    private final long TEST_MEETING_END_TIME = TEST_MEETING_START_TIME + 30;
    private final List<String> TEST_MEETING_PARTICIPANTS = Arrays.asList("testParticipant1@lamzone.test", "testParticipant2@lamzone.test");

    private MeetingRoomRepository repository;


    @Before
    public void setup() {
        repository = DI.getTestMeetingRoomRepository(); //Test repository with only 3 meeting rooms
        repository.getMeetingRooms().forEach(meetingRoom -> meetingRoom.getMeetings().clear());
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

    @Test
    public void getFreeMeetingRoomAtGivenSlotWithSuccess() {
        //Given : we need a free meeting room at a specific slot
        //When : we get a free meeting room at this time slot
        //Then : the returned meeting room is the expected free one
        TEST_MEETING_ROOM.addMeeting(new Meeting(TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME - 5, TEST_MEETING_END_TIME - 10,
                TEST_MEETING_PARTICIPANTS));
        MeetingRoom meetingRoom = repository.getFreeMeetingRoomAtGivenSlot(TEST_MEETING_START_TIME, TEST_MEETING_END_TIME);
        assertEquals(NEXT_TEST_MEETING_ROOM, meetingRoom);
    }

    @Test
    public void scheduleMeetingWithSuccess() {
        //Given : we have a free meeting room for our new meeting
        //When : we schedule our new meeting
        //Then : the new meeting is added in the meeting room
        repository.scheduleMeeting(NEXT_TEST_MEETING_ROOM, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        Meeting expectedMeeting = new Meeting(TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        assertEquals(expectedMeeting, NEXT_TEST_MEETING_ROOM.getMeetings().get(0));
    }

    @Test
    public void cancelMeetingWithSuccess() {
        //Given : we want to cancel a meeting
        //When : we cancel the meeting
        //Then : the meeting is no more in the meetings list of the concerned meeting room
        repository.scheduleMeeting(NEXT_TEST_MEETING_ROOM, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        Meeting unExpectedMeeting = new Meeting(TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        repository.cancelMeeting(NEXT_TEST_MEETING_ROOM, unExpectedMeeting);
        assertFalse(NEXT_TEST_MEETING_ROOM.getMeetings().contains(unExpectedMeeting));
    }
}