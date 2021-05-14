package com.lamzone.mareu.ui.meetings_list;

public class MeetingFragment {

    private String mMeetingRoomName;
    private int mMeetingRoomSymbol;
    private String mMeetingSubject;
    private String mMeetingParticipants;
    private String mMeetingDate;

    public MeetingFragment(String meetingRoomName, int meetingRoomSymbol, String meetingSubject, String meetingParticipants, String meetingDate) {
        mMeetingRoomName = meetingRoomName;
        mMeetingRoomSymbol = meetingRoomSymbol;
        mMeetingSubject = meetingSubject;
        mMeetingParticipants = meetingParticipants;
        mMeetingDate = meetingDate;
    }

    public String getMeetingRoomName() {
        return mMeetingRoomName;
    }

    public int getMeetingRoomSymbol() {
        return mMeetingRoomSymbol;
    }

    public String getMeetingSubject() {
        return mMeetingSubject;
    }

    public String getMeetingParticipants() {
        return mMeetingParticipants;
    }

    public String getMeetingDate() {
        return mMeetingDate;
    }
}
