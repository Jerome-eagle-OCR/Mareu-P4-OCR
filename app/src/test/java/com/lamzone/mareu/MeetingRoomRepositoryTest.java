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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MeetingRoomRepositoryTest {

    private static final MeetingRoom TEST_FREE_MEETING_ROOM = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(2);
    private static final long TEST_MEETING_ROOM1 = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(0).getId();
    private static final long TEST_MEETING_ROOM2 = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(1).getId();
    private static final long TEST_MEETING_ROOM3 = TEST_FREE_MEETING_ROOM.getId();
    private static final String TEST_MEETING_SUBJECT = "Test";
    private static final long TEST_MEETING_START_TIME = System.currentTimeMillis();
    private static final long TEST_MEETING_DATE = TEST_MEETING_START_TIME + 2880 * 60000; //+48h
    private static final long TEST_MEETING_END_TIME = TEST_MEETING_START_TIME + 30 * 60000;//+30mn
    private static final List<String> TEST_MEETING_PARTICIPANTS = Arrays.asList("testParticipant1@lamzone.test", "testParticipant2@lamzone.test", "testParticipant3@lamzone.test");

    //TEST_MEETING_ROOM3 is the only meeting room that can host our new TEST_MEETING
    private static final Meeting TEST_MEETING1 = new Meeting(TEST_MEETING_ROOM1, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME - 5 * 60000, TEST_MEETING_END_TIME - 10 * 60000, TEST_MEETING_PARTICIPANTS);
    private static final Meeting TEST_MEETING2 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME - 30 * 60000, TEST_MEETING_START_TIME - 0, TEST_MEETING_PARTICIPANTS);
    private static final Meeting TEST_MEETING3 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, TEST_MEETING_END_TIME - 5 * 60000, TEST_MEETING_END_TIME + 10 * 60000, TEST_MEETING_PARTICIPANTS);
    private static final Meeting TEST_MEETING4 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, TEST_MEETING_END_TIME + 0, TEST_MEETING_END_TIME + 15 * 60000, TEST_MEETING_PARTICIPANTS);
    private static final Meeting TEST_MEETING5 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME - 15 * 60000, TEST_MEETING_START_TIME - 0, TEST_MEETING_PARTICIPANTS);
    private static final Meeting TEST_MEETING6 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME - 30 * 60000, TEST_MEETING_START_TIME - 15 * 60000, TEST_MEETING_PARTICIPANTS);
    private static final Meeting TEST_MEETING7 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_END_TIME + 5 * 60000, TEST_MEETING_END_TIME + 20 * 60000, TEST_MEETING_PARTICIPANTS);
    private static final Meeting TEST_MEETING8 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_END_TIME + 20 * 60000, TEST_MEETING_END_TIME + 45 * 60000, TEST_MEETING_PARTICIPANTS);

    public static final List<Meeting> TEST_MEETINGS = Arrays.asList(TEST_MEETING2, TEST_MEETING6, TEST_MEETING5, TEST_MEETING1, TEST_MEETING3, TEST_MEETING4, TEST_MEETING7, TEST_MEETING8);


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
        //Then : the retrieved list equals expected list
        List<MeetingRoom> expectedMeetingRooms = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.subList(0, 3);
        List<MeetingRoom> meetingRooms = repository.getMeetingRooms();
        assertEquals(expectedMeetingRooms, meetingRooms);
    }

    @Test
    public void getMeetingRoomByIdWithSuccess() {
        //Given : we want to retrieve a meeting room from its id
        //When : we retrieve the meeting room
        //Then : the retrieved meeting room is the expected one
        assertEquals(TEST_FREE_MEETING_ROOM, repository.getMeetingRoomById(TEST_MEETING_ROOM3));
    }

    @Test
    public void getMeetingsWithSuccess() {
        //Given : we want the list of meetings
        //When : we retrieve the list of meetings
        //Then : the retrieved list equals the expected list
        repository.getMeetings().addAll(TEST_MEETINGS.subList(0, 3)); //Only test on 3 meetings
        List<Meeting> meetings = repository.getMeetings();
        assertEquals(TEST_MEETINGS.subList(0, 3), meetings);
    }

    @Test
    public void getMeetingsForGivenMeetingRoomWithSuccess() {
        //Given : we want the list of meetings taking place in a given meeting room
        //When : we retrieve the list of meetings
        //Then : the retrieved list equals the expected list
        repository.getMeetings().addAll(TEST_MEETINGS);
        List<Meeting> expectedList = Stream.concat(TEST_MEETINGS.subList(0, 1).stream(), TEST_MEETINGS.subList(4, 6).stream()).collect(Collectors.toList());
        assertEquals(expectedList, repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM2));
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
        //Then : the new meeting is scheduled (added in meetings list)
        repository.scheduleMeeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        assertEquals(new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_START_TIME, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS), repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM3).get(0));
    }

    @Test
    public void scheduleMeetingAlternativeWithSuccess() {
        //Given : we have a meeting (object)
        //When : we schedule our new meeting
        //Then : the new meeting is scheduled (added in meetings list)
        repository.scheduleMeeting(TEST_MEETING1);
        assertEquals(TEST_MEETING1, repository.getMeetings().get(0));
    }

    @Test
    public void cancelMeetingWithSuccess() {
        //Given : we want to cancel a meeting
        //When : we cancel the meeting
        //Then : the meeting is no more in the meetings list of the concerned meeting room
        repository.scheduleMeeting(TEST_MEETING1);
        repository.cancelMeeting(repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM1).get(0));
        assertTrue(repository.getMeetingsForGivenMeetingRoom(TEST_MEETING_ROOM1).isEmpty());
    }


    @Test
    public void getMeetingsForGivenDateWithSuccess() {
        //Given : we want to get all meetings occurring at a given date (day)
        //When : we retrieve the meetings
        //Then : we get the expected list of meetings
        repository.scheduleMeeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DATE, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS);
        for (int i = 0; i < TEST_MEETINGS.size(); i++) {
            repository.scheduleMeeting(TEST_MEETINGS.get(i));
        }
        List<Meeting> expectedList = Collections.singletonList(new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DATE, TEST_MEETING_END_TIME, TEST_MEETING_PARTICIPANTS));
        assertEquals(expectedList, repository.getMeetingsForGivenDate(TEST_MEETING_DATE));
    }
}