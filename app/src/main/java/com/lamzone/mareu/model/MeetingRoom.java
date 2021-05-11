package com.lamzone.mareu.model;

public class MeetingRoom {

    private long id;
    private final String mMeetingRoomName;
    private final int mMeetingRoomImage;

    public MeetingRoom(long id, String meetingRoomName, int meetingRoomImage) {
        this.id = id;
        mMeetingRoomName = meetingRoomName;
        mMeetingRoomImage = meetingRoomImage;
    }

    public long getId() {
        return id;
    }

    public String getMeetingRoomName() {
        return mMeetingRoomName;
    }

    public int getMeetingRoomImage() {
        return mMeetingRoomImage;
    }
}