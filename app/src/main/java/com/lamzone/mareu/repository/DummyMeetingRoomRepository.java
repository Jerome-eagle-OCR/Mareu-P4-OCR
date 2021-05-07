package com.lamzone.mareu.repository;

import com.lamzone.mareu.model.MeetingRoom;

import java.util.List;

public class DummyMeetingRoomRepository implements MeetingRoomRepository {

    private final List<MeetingRoom> meetingRooms;


    public DummyMeetingRoomRepository() {
        meetingRooms = DummyMeetingRoomGenerator.generateMeetingRooms();
    }

    public DummyMeetingRoomRepository(int nbMeetingRooms) {
        meetingRooms = DummyMeetingRoomGenerator.generateMeetingRooms().subList(0, nbMeetingRooms + 1);
    }


    @Override
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    @Override
    public MeetingRoom getFreeMeetingRoomAtGivenSlot(meetingTime, meetingDuration) {
        //TODO
        return null;
    }

    @Override
    public MeetingRoom scheduleMeeting(meetingTime, meetingDuration, subject, participants) {
        //TODO
        return null;
    }

    @Override
    public void cancelMeeting() {

    }
}
