package com.lamzone.mareu.model;

import java.util.ArrayList;

public class MeetingRoom {

    private final String mMeetingRoomName;
    private final int mMeetingRoomImage;
    private ArrayList mMeetings;

    public MeetingRoom(String meetingRoomName, int meetingRoomImage) {
        mMeetingRoomName = meetingRoomName;
        mMeetingRoomImage = meetingRoomImage;
    }

}