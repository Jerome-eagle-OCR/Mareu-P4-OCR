package com.lamzone.mareu.model;

import java.util.List;
import java.util.Objects;

public class Meeting {

    private long meetingRoomId;
    private String mMeetingSubject;
    private String mMeetingDay;
    private long mMeetingStartTime;
    private long mMeetingEndTime;
    private List<String> mMeetingParticipants;

    public Meeting(long meetingRoomId, String meetingSubject, String meetingDay, long meetingStartTime, long meetingEndTime, List<String> meetingParticipants) {
        this.meetingRoomId = meetingRoomId;
        mMeetingSubject = meetingSubject;
        mMeetingDay = meetingDay;
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

    public String getMeetingDay() {
        return mMeetingDay;
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
        return meetingRoomId == meeting.meetingRoomId &&
                mMeetingStartTime == meeting.mMeetingStartTime &&
                mMeetingEndTime == meeting.mMeetingEndTime &&
                Objects.equals(mMeetingSubject, meeting.mMeetingSubject) &&
                Objects.equals(mMeetingDay, meeting.mMeetingDay) &&
                Objects.equals(mMeetingParticipants, meeting.mMeetingParticipants);
    }
}