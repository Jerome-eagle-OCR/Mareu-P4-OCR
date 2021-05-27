package com.lamzone.mareu.model;

import java.util.List;

public class Meeting {

    private final long meetingRoomId;
    private final String mMeetingSubject;
    private final long mMeetingStartTime;
    private final long mMeetingEndTime;
    private final List<String> mMeetingParticipants;

    public Meeting(long meetingRoomId, String meetingSubject, long meetingStartTime, long meetingEndTime, List<String> meetingParticipants) {
        this.meetingRoomId = meetingRoomId;
        mMeetingSubject = meetingSubject;
        mMeetingStartTime = meetingStartTime;
        mMeetingEndTime = meetingEndTime;
        mMeetingParticipants = meetingParticipants;
    }

    public long getMeetingRoomId() {
        return this.meetingRoomId;
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

        if (meetingRoomId != meeting.meetingRoomId) return false;
        if (mMeetingStartTime != meeting.mMeetingStartTime) return false;
        if (mMeetingEndTime != meeting.mMeetingEndTime) return false;
        if (!mMeetingSubject.equals(meeting.mMeetingSubject)) return false;
        return mMeetingParticipants.equals(meeting.mMeetingParticipants);
    }
}