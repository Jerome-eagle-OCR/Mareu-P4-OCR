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
    MeetingRoom getFreeMeetingRoomAtGivenSlot(long meetingStartTime, long meetingEndTime);

    /**
     * Schedule a meeting
     */
    void scheduleMeeting(MeetingRoom meetingRoom, long meetingStartTime, long meetingEndTime, String meetingSubject, List<String> meetingParticipants);

    /**
     * Cancel a meeting
     */
    void cancelMeeting(MeetingRoom meetingRoom, long meetingStartTime, long meetingEndTime);
}
