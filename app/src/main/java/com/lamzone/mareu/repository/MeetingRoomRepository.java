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
     * Get meeting room from its id
     *
     * @param id of meeting room
     * @return the meeting room
     */
    MeetingRoom getMeetingRoomById(long id);

    /**
     * Get meetings
     *
     * @return the full list of meetings
     */
    List<Meeting> getMeetings();

    /**
     * Get all meetings taking place in given meeting room
     *
     * @param meetingRoomId the given meeting room id
     * @return a list of meetings
     */
    List<Meeting> getMeetingsForGivenMeetingRoom(long meetingRoomId);

    /**
     * Check meeting rooms one by one in list to get first one available at given slot
     *
     * @param newMeetingStartTime Start time of new meeting to schedule
     * @param newMeetingEndTime   End time of new meeting to schedule
     * @return Available meeting rooms, empty list if none
     */
    List<MeetingRoom> getFreeMeetingRoomsAtGivenSlot(long newMeetingStartTime, long newMeetingEndTime);

    /**
     * Schedule a meeting
     *
     * @param idMeetingRoom       The id of meeting room where meeting will take place
     * @param meetingSubject      The subject of this meeting
     * @param meetingStartTime    The meeting start time
     * @param meetingEndTime      The meeting end time
     * @param meetingParticipants The list of participants
     */
    void scheduleMeeting(long idMeetingRoom, String meetingSubject, long meetingStartTime, long meetingEndTime, List<String> meetingParticipants);

    /**
     * Schedule a meeting
     *
     * @param meeting The meeting to schedule
     */
    void scheduleMeeting(Meeting meeting);

    /**
     * Cancel a meeting
     *
     * @param meeting to cancel
     */
    void cancelMeeting(Meeting meeting);

    /**
     * Get all meetings occurring on a specific day
     *
     * @param dayTimeStamp the specific day timeStamp in ms
     * @return a list of meetings
     */
    List<Meeting> getMeetingsForGivenDate(long dayTimeStamp);
}
