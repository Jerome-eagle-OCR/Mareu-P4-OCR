package com.lamzone.mareu.model;

import java.util.ArrayList;
import java.util.List;

public class MeetingRoom {

    private final String mMeetingRoomName;
    private final int mMeetingRoomImage;
    private List<Meeting> mMeetings;

    public MeetingRoom(String meetingRoomName, int meetingRoomImage) {
        mMeetingRoomName = meetingRoomName;
        mMeetingRoomImage = meetingRoomImage;
        mMeetings = new ArrayList<>();
    }

    public String getMeetingRoomName() {
        return mMeetingRoomName;
    }

    public int getMeetingRoomImage() {
        return mMeetingRoomImage;
    }

    public List<Meeting> getMeetings() {
        return mMeetings;
    }

    public void addMeeting(Meeting meeting) {
        boolean isNotAlreadyInList = true;
        for (int i = 0; isNotAlreadyInList && i < mMeetings.size(); i++) {
            if (mMeetings.get(i).equals(meeting)) isNotAlreadyInList = false;
            i++;
        }
        if (isNotAlreadyInList) mMeetings.add(meeting);
    }

    public void deleteMeeting(Meeting meeting) {
        for (int i = 0; i < mMeetings.size(); i++) {
            if (mMeetings.get(i).equals(meeting)) mMeetings.remove(i);
        }
    }
}