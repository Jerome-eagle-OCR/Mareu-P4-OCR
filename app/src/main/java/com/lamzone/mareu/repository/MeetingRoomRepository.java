package com.lamzone.mareu.repository;

import com.lamzone.mareu.model.MeetingRoom;

import java.util.List;

public interface MeetingRoomRepository {

    /**
     * Get meeting rooms
     * @return
     */
    List<MeetingRoom> getMeetingRooms();

    /**
     * Get first free meeting room in list at given time slot
     * @return
     */
    MeetingRoom getFreeMeetingRoomAtGivenSlot(meetingTime, meetingDuration);

    /**
     * Schedule a meeting
     */
    MeetingRoom scheduleMeeting(meetingTime, meetingDuration, subject, participants);

    /**
     * Cancel a meeting
     */
    void cancelMeeting(meetingRoom, meetingTime, meetingDuration);
}
