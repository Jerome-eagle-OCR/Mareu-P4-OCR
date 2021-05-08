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
        if (!this.getMeetings().isEmpty()) {
            for (int i = 0; isNotAlreadyInList && i < this.getMeetings().size(); i++) {
                if (this.getMeetings().get(i).equals(meeting)) {
                    isNotAlreadyInList = false;
                }
            }
        }
        if (isNotAlreadyInList) mMeetings.add(meeting);
    }

    public void deleteMeeting(Meeting meeting) {
        if (!this.mMeetings.isEmpty()) {
            for (int i = 0; i < this.getMeetings().size(); i++) {
                if (this.mMeetings.get(i).equals(meeting)) {
                    this.mMeetings.remove(i);
                }
            }
        }
    }
}