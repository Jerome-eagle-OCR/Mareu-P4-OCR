package com.lamzone.mareu.utils;

import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.DummyMeetingRoomGenerator;

import java.util.Arrays;
import java.util.List;

public class UtilsForTesting {

    public static final MeetingRoom TEST_FREE_MEETING_ROOM = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(2);
    public static final long TEST_MEETING_ROOM1 = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(0).getId();
    public static final long TEST_MEETING_ROOM2 = DummyMeetingRoomGenerator.DUMMY_MEETING_ROOMS.get(1).getId();
    public static final long TEST_MEETING_ROOM3 = TEST_FREE_MEETING_ROOM.getId();
    public static final String TEST_MEETING_SUBJECT = "Test";
    public static final long TEST_MEETING_START_TIME = System.currentTimeMillis();
    public static final long TEST_MEETING_DATE = TEST_MEETING_START_TIME + 2880 * 60000; //+48h
    public static final long TEST_MEETING_END_TIME = TEST_MEETING_START_TIME + 30 * 60000;//+30mn
    public static final List<String> TEST_MEETING_PARTICIPANTS = Arrays.asList("testParticipant1@lamzone.test", "testParticipant2@lamzone.test", "testParticipant3@lamzone.test");

    //TEST_MEETING_ROOM3 is the only meeting room that can host our new TEST_MEETING
    public static final Meeting TEST_MEETING1 = new Meeting(TEST_MEETING_ROOM1, TEST_MEETING_SUBJECT + "1", TEST_MEETING_START_TIME - 5 * 60000, TEST_MEETING_END_TIME - 10 * 60000, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING2 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT + "2", TEST_MEETING_START_TIME - 30 * 60000, TEST_MEETING_START_TIME - 0, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING3 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT + "3", TEST_MEETING_END_TIME - 5 * 60000, TEST_MEETING_END_TIME + 10 * 60000, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING4 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT + "4", TEST_MEETING_END_TIME + 0, TEST_MEETING_END_TIME + 15 * 60000, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING5 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT + "5", TEST_MEETING_START_TIME - 15 * 60000, TEST_MEETING_START_TIME - 0, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING6 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT + "6", TEST_MEETING_START_TIME - 30 * 60000, TEST_MEETING_START_TIME - 15 * 60000, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING7 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT + "7", TEST_MEETING_END_TIME + 5 * 60000, TEST_MEETING_END_TIME + 20 * 60000, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING8 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT + "8", TEST_MEETING_END_TIME + 20 * 60000, TEST_MEETING_END_TIME + 45 * 60000, TEST_MEETING_PARTICIPANTS);

    public static final List<Meeting> TEST_MEETINGS = Arrays.asList(TEST_MEETING1, TEST_MEETING2, TEST_MEETING3, TEST_MEETING4, TEST_MEETING5, TEST_MEETING6, TEST_MEETING7, TEST_MEETING8);

    public static final Meeting TEST_MEETING = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, TEST_MEETING_DATE, TEST_MEETING_DATE + 30 * 60000, TEST_MEETING_PARTICIPANTS);
}
