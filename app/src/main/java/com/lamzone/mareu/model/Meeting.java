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

    public String getMeetingSubject() {
        return mMeetingSubject;
    }

    public long getMeetingStartTime() {
        return mMeetingStartTime;
    }

    public long getMeetingEndTime() {
        return mMeetingEndTime;
    }

    public List<String> getMeetingParticipants() {
        return mMeetingParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return getMeetingStartTime() == meeting.getMeetingStartTime() &&
                getMeetingEndTime() == meeting.getMeetingEndTime() &&
                getMeetingSubject().equals(meeting.getMeetingSubject()) &&
                getMeetingParticipants().equals(meeting.getMeetingParticipants());
    }
}