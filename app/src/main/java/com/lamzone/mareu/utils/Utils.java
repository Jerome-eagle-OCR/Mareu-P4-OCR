package com.lamzone.mareu.utils;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

    public static MeetingRoomRepository mMeetingRoomRepository = DI.getTestMeetingRoomRepository();

    public static final MeetingRoom TEST_FREE_MEETING_ROOM = mMeetingRoomRepository.getMeetingRooms().get(2);
    public static final long TEST_MEETING_ROOM1 = mMeetingRoomRepository.getMeetingRooms().get(0).getId();
    public static final long TEST_MEETING_ROOM2 = mMeetingRoomRepository.getMeetingRooms().get(1).getId();
    public static final long TEST_MEETING_ROOM3 = TEST_FREE_MEETING_ROOM.getId();
    public static final String TEST_MEETING_SUBJECT = "Test";
    public static final String TEST_MEETING_DAY = "Test day";
    public static final long TEST_MEETING_START_TIME = System.currentTimeMillis() / 60000; //Current time in minutes
    public static final long TEST_MEETING_END_TIME = TEST_MEETING_START_TIME + 30;
    public static final List<String> TEST_MEETING_PARTICIPANTS = Arrays.asList("testParticipant1@lamzone.test", "testParticipant2@lamzone.test", "testParticipant3@lamzone.test");

    //TEST_MEETING_ROOM3 is the only meeting room that can host our new TEST_MEETING
    public static final Meeting TEST_MEETING1 = new Meeting(TEST_MEETING_ROOM1, TEST_MEETING_SUBJECT, "Ven\n14\nsep", TEST_MEETING_START_TIME - 5, TEST_MEETING_END_TIME - 10, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING2 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, "Ven\n14\nsep", TEST_MEETING_START_TIME - 30, TEST_MEETING_START_TIME - 0, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING4 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, "Ven\n14\nsep", TEST_MEETING_END_TIME - 5, TEST_MEETING_END_TIME + 10, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING3 = new Meeting(TEST_MEETING_ROOM2, TEST_MEETING_SUBJECT, "Ven\n14\nsep", TEST_MEETING_END_TIME + 0, TEST_MEETING_END_TIME + 15, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING5 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, "Mer\n25\noct", TEST_MEETING_START_TIME - 15, TEST_MEETING_START_TIME - 0, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING6 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, "Lun\n02\nmai", TEST_MEETING_START_TIME - 30, TEST_MEETING_START_TIME - 15, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING7 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, "Jeu\n18\ndéc", TEST_MEETING_END_TIME + 5, TEST_MEETING_END_TIME + 20, TEST_MEETING_PARTICIPANTS);
    public static final Meeting TEST_MEETING8 = new Meeting(TEST_MEETING_ROOM3, TEST_MEETING_SUBJECT, "Ven\n30\njuin", TEST_MEETING_END_TIME + 20, TEST_MEETING_END_TIME + 45, TEST_MEETING_PARTICIPANTS);

    public static final List<Meeting> TEST_MEETINGS = Arrays.asList(TEST_MEETING1, TEST_MEETING2, TEST_MEETING3, TEST_MEETING4, TEST_MEETING5, TEST_MEETING6, TEST_MEETING7, TEST_MEETING8);

    public static final String DATE_FORMAT_1 = "EEEE d MMMM yyyy";
    public static final String DATE_FORMAT_2 = "EEE\nd\nMMM\nHH:mm";
    public static final String TIME_FORMAT = "HH:mm";
    private static SimpleDateFormat formatter = null;
    private static final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();


    /**
     * Format a date for displaying purpose
     *
     * @param calendar
     * @param dateFormat
     * @return formatted date as a String
     */
    public static String formatDate(Calendar calendar, String dateFormat) {
        formatter = new SimpleDateFormat(dateFormat);
        String formattedDate = formatter.format(calendar.getTime());
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
        return formattedDate;
    }

    /**
     * Format a date for displaying purpose from timeStamp
     *
     * @param timeMillis
     * @return formatted date as a String
     */
    public static String formatDate(long timeMillis) {
        dateFormatSymbols.setShortWeekdays(new String[]{"Unused", "DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM"});
        dateFormatSymbols.setShortMonths(new String[]{"JAN", "FEV", "MAR", "AVR", "MAI", "JUIN", "JUIL", "AOU", "SEP", "OCT", "NOV", "DEC"});
        formatter = new SimpleDateFormat(DATE_FORMAT_2, dateFormatSymbols);
        Date dateTime = new Date(timeMillis);
        return formatter.format(dateTime);
    }
}
