package com.lamzone.mareu.model;

public class MeetingRoom {

    private long id;
    private final String mMeetingRoomName;
    private final int mMeetingRoomSymbol;

    public MeetingRoom(long id, String meetingRoomName, int meetingRoomSymbol) {
        this.id = id;
        mMeetingRoomName = meetingRoomName;
        mMeetingRoomSymbol = meetingRoomSymbol;
    }

    public long getId() {
        return id;
    }

    public String getMeetingRoomName() {
        return mMeetingRoomName;
    }

    public int getMeetingRoomSymbol() {
        return mMeetingRoomSymbol;
    }
}