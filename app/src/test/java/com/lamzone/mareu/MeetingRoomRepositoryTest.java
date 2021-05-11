package com.lamzone.mareu;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.DummyMeetingRoomGenerator;
import com.lamzone.mareu.repository.MeetingRoomRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MeetingRoomRepositoryTest {

    private final MeetingRoom TEST_FREE_MEETING_ROOM = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(2);
    private final long TEST_MEETING_ROOM1 = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(0).getId();
    private final long TEST_MEETING_ROOM2 = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(1).getId();
    private final long TEST_MEETING_ROOM3 = TEST_FREE_MEETING_ROOM.getId();
    private final String TEST_MEETING_SUBJECT = "Test";
    private final String TEST_MEETING_DAY = "Test day";
    private final long TEST_MEETING_START_TIME = System.currentTimeMillis() / 60000; //Convert time in minutes
    private final long TEST_MEETING_END_TIME = TEST_MEETING_START_TIME + 30;
    private final List<String> TEST_MEETING_PARTICIPANTS = Arrays.asList("testParticipant1@lamzone.test", "testParticipant2@lamzone.test");

    private final Meeting TEST_MEETING1 = new Meeting(TEST_MEETING_ROOM1, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_START_TIME - 5, TEST_MEETING_END_TIME - 10, TEST_MEETING_PARTICIPANTS);
    private final Meeting TEST_MEETING2 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_START_TIME - 30, TEST_MEETING_START_TIME, TEST_MEETING_PARTICIPANTS);
    private final Meeting TEST_MEETING4 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_END_TIME - 5, TEST_MEETING_END_TIME + 10, TEST_MEETING_PARTICIPANTS);
    private final Meeting TEST_MEETING3 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_END_TIME, TEST_MEETING_END_TIME + 15, TEST_MEETING_PARTICIPANTS);
    private final Meeting TEST_MEETING5 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_START_TIME - 15, TEST_MEETING_START_TIME, TEST_MEETING_PARTICIPANTS);
    private final Meeting TEST_MEETING6 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_START_TIME - 30, TEST_MEETING_START_TIME - 15, TEST_MEETING_PARTICIPANTS);
    private final Meeting TEST_MEETING7 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_END_TIME, TEST_MEETING_END_TIME + 20, TEST_MEETING_PARTICIPANTS);
    private final Meeting TEST_MEETING8 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_END_TIME + 20, TEST_MEETING_END_TIME + 45, TEST_MEETING_PARTICIPANTS);

    private final List<Meeting> TEST_MEETINGS = Arrays.asList(TEST_MEETING1, TEST_MEETING2, TEST_MEETING3, TEST_MEETING4, TEST_MEETING5, TEST_MEETING6, TEST_MEETING7, TEST_MEETING8);

    private MeetingRoomRepository repository;


    @Before
    public void setup() {
        repository = DI.getTestMeetingRoomRepository(); //Test repository with only 3 meeting rooms
        repository.getMeetings().clear(); //Be sure we have no meetings in any meeting room
    }


    @Test
    public void getMeetingRoomsWithSuccess() {
        //Given : we want the list of meeting rooms
        //When : we retrieve the list of meeting rooms
        //Then : retrieved list equals expected list
        List<MeetingRoom> expectedMeetingRooms = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.subList(0, 3);
        List<MeetingRoom> meetingRooms = repository.getMeetingRooms();
        assertEquals(expectedMeetingRooms, meetingRooms);
    }

    @Test
    public void getMeetingRoomByIdWithSuccess() {
        //Given : we want to retrieve a meeting room from its id
        //When : we retrieve the meeting room
        //Then : retrieved meeting room is the expected one
        assertEquals(TEST_FREE_MEETING_ROOM, repository.getMeetingRoomById(TEST_MEETING_ROOM3));
    }

    @Test
    public void getMeetingsWithSuccess() {
        //Given : we want the list of meetings
        //When : we retrieve the list of meetings
        //Then : retrieved list equals the expected list
        repository.getMeetings().addAll(TEST_MEETINGS.subList(0, 3)); //Only test on 3 meetings
        List<Meeting> meetings = repository.getMeetings();
        assertEquals(TEST_MEETINGS.subList(0, 3), meetings);
    }

    @Test
    public void getMeetingsForGivenMeetingRoomWithSuccess() {
        //Given : we want the list of meetings taking place in a given meeting room
        //When : we retrieve the list of meetings
        //Then : retrieved list equals the expected list
        repository.getMeetings().addAll(TEST_MEETINGS.subList(0, 5));
        List<Meeting> meetings = repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM2);
        assertEquals(TEST_MEETINGS.subList(1, 4), meetings);
    }

    @Test
    public void getFreeMeetingRoomAtGivenSlotWithSuccess() {
        //Given : we need a free meeting room at a specific slot
        //When : we get a free meeting room at this time slot
        //Then : the returned meeting room is the expected free one
        //Collections.shuffle(TEST_MEETINGS);
        repository.getMeetings().addAll(TEST_MEETINGS);
        assertEquals(Collections.singletonList(TEST_FREE_MEETING_ROOM), repository.getFreeMeetingRoomsAtGivenSlot(TEST_MEETING_START_TIME, TEST_MEETING_END_TIME));
    }

    @Test
    public void scheduleMeetingWithSuccess() {
        //Given : we have a free meeting room for our new meeting
        //When : we schedule our new meeting
        //Then : the new meeting is added in the meeting room
        repository.scheduleMeeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        assertEquals(repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM3).get(0), new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS));
    }

    @Test
    public void cancelMeetingWithSuccess() {
        //Given : we want to cancel a meeting
        //When : we cancel the meeting
        //Then : the meeting is no more in the meetings list of the concerned meeting room
        repository.scheduleMeeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DAY, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        repository.cancelMeeting(repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM3).get(0));
        assertTrue(repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM3).isEmpty());
    }


    @Test
    public void getMeetingsForGivenDateRangeWithSuccess() {
        //Given : we want to get all meetings for a given date range (year, month, week or day)
        //When : we retrieve the meetings
        //Then : we get the expected list of meetings
        //TODO
    }

}