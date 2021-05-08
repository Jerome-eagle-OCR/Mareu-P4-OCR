package com.lamzone.mareu.repository;

import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;

import java.util.List;

public interface MeetingRoomRepository {

    /**
     * Get meeting rooms
     *
     * @return the list of meeting rooms
     */
    List<MeetingRoom> getMeetingRooms();

    /**
     * Get first free meeting room in list at given time slot
     *
     * @return a meeting room where a meeting can be scheduled at tested time slot
     */
    MeetingRoom getFreeMeetingRoomAtGivenSlot(long meetingStartTime, long meetingEndTime);

    /**
     * Schedule a meeting
     */
    void scheduleMeeting(MeetingRoom meetingRoom, String meetingSubject, long meetingStartTime, long meetingEndTime, List<String> meetingParticipants);

    /**
     * Cancel a meeting
     *
     * @param meetingRoom where meeting take place
     * @param meeting     to cancel
     */
    void cancelMeeting(MeetingRoom meetingRoom, Meeting meeting);
}
