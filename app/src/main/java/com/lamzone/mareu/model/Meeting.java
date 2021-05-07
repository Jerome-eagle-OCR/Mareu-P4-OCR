package com.lamzone.mareu.model;

import java.util.List;

public class Meeting {

    private String mMeetingSubject;
    private long mMeetingStartTime;
    private long mMeetingEndTime;
    private List<String> mMeetingParticipants;

    public Meeting(String meetingSubject, long meetingStartTime, long meetingEndTime, List<String> meetingParticipants) {
        mMeetingSubject = meetingSubject;
        mMeetingStartTime = meetingStartTime;
        mMeetingEndTime = meetingEndTime;
        mMeetingParticipants = meetingParticipants;
    }
}