package com.lamzone.mareu.repository;

import com.lamzone.mareu.model.MeetingRoom;

import java.util.List;

public class DummyMeetingRoomRepository implements MeetingRoomRepository {

    private final List<MeetingRoom> meetingRooms;


    public DummyMeetingRoomRepository(boolean isTest) {
        meetingRooms = isTest ? DummyMeetingRoomGenerator.generateMeetingRooms().subList(0, 3) : DummyMeetingRoomGenerator.generateMeetingRooms();
    }

    @Override
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    @Override
    public MeetingRoom getFreeMeetingRoomAtGivenSlot(long meetingStartTime, long meetingEndTime) {
        return null;
    }

    @Override
    public void scheduleMeeting(MeetingRoom meetingRoom, long meetingStartTime, long meetingEndTime, String meetingSubject, List<String> meetingParticipants) {

    }

    @Override
    public void cancelMeeting(MeetingRoom meetingRoom, long meetingStartTime, long meetingEndTime) {

    }

}
