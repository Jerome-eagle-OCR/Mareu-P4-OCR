package com.lamzone.mareu.utils;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public abstract class Utils {


    public static MeetingRoomRepository mMeetingRoomRepository = DI.getTestMeetingRoomRepository();

    public static final MeetingRoom DUMMY_FREE_MEETING_ROOM = mMeetingRoomRepository.getMeetingRooms().get(2);
    public static final long DUMMY_MEETING_ROOM1 = mMeetingRoomRepository.getMeetingRooms().get(0).getId();
    public static final long DUMMY_MEETING_ROOM2 = mMeetingRoomRepository.getMeetingRooms().get(1).getId();
    public static final long DUMMY_MEETING_ROOM3 = DUMMY_FREE_MEETING_ROOM.getId();
    public static final String DUMMY_MEETING_SUBJECT = "Relecture de code application Maréu";
    public static final long DUMMY_MEETING_START_TIME = System.currentTimeMillis();
    public static final long DUMMY_MEETING_END_TIME = DUMMY_MEETING_START_TIME + (30 * 60000);
    public static final List<String> DUMMY_MEETING_PARTICIPANTS = Arrays.asList("dummyParticipant1@lamzone.com", "dummyParticipant2@lamzone.com", "dummyParticipant3@lamzone.com");

    //DUMMY_MEETING_ROOM3 is the only meeting room that can host our new DUMMY_MEETING
    public static final Meeting DUMMY_MEETING1 = new Meeting(DUMMY_MEETING_ROOM1, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_START_TIME - 5 * 60000, DUMMY_MEETING_END_TIME - 10 * 60000, DUMMY_MEETING_PARTICIPANTS);
    public static final Meeting DUMMY_MEETING2 = new Meeting(DUMMY_MEETING_ROOM2, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_START_TIME - 30 * 60000, DUMMY_MEETING_START_TIME - 0, DUMMY_MEETING_PARTICIPANTS);
    public static final Meeting DUMMY_MEETING3 = new Meeting(DUMMY_MEETING_ROOM2, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_END_TIME - 20 * 60000, DUMMY_MEETING_END_TIME - 5 * 60000, DUMMY_MEETING_PARTICIPANTS);
    public static final Meeting DUMMY_MEETING4 = new Meeting(DUMMY_MEETING_ROOM2, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_END_TIME + 0, DUMMY_MEETING_END_TIME + 15 * 60000, DUMMY_MEETING_PARTICIPANTS);
    public static final Meeting DUMMY_MEETING5 = new Meeting(DUMMY_MEETING_ROOM3, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_START_TIME - 15 * 60000, DUMMY_MEETING_START_TIME - 0, DUMMY_MEETING_PARTICIPANTS);
    public static final Meeting DUMMY_MEETING6 = new Meeting(DUMMY_MEETING_ROOM3, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_START_TIME - 30 * 60000, DUMMY_MEETING_START_TIME - 15 * 60000, DUMMY_MEETING_PARTICIPANTS);
    public static final Meeting DUMMY_MEETING7 = new Meeting(DUMMY_MEETING_ROOM3, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_END_TIME + 5 * 60000, DUMMY_MEETING_END_TIME + 20 * 60000, DUMMY_MEETING_PARTICIPANTS);
    public static final Meeting DUMMY_MEETING8 = new Meeting(DUMMY_MEETING_ROOM3, DUMMY_MEETING_SUBJECT, DUMMY_MEETING_END_TIME + 20 * 60000, DUMMY_MEETING_END_TIME + 45 * 60000, DUMMY_MEETING_PARTICIPANTS);

    public static final List<Meeting> DUMMY_MEETINGS = Arrays.asList(DUMMY_MEETING1, DUMMY_MEETING2, DUMMY_MEETING3, DUMMY_MEETING4, DUMMY_MEETING5, DUMMY_MEETING6, DUMMY_MEETING7, DUMMY_MEETING8);

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

    public static class SortByStartTime implements Comparator<Meeting> {
        @Override
        public int compare(Meeting a, Meeting b) {
            return (int) (a.getMeetingStartTime() - b.getMeetingStartTime());
        }
    }
}
