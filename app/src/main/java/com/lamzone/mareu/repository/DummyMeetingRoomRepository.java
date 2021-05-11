package com.lamzone.mareu.repository;

import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the repository
 */
public class DummyMeetingRoomRepository implements MeetingRoomRepository {

    private final List<MeetingRoom> meetingRooms;

    private List<Meeting> mMeetings = new ArrayList<>();

    /**
     * DummyMeetingRoomRepository constructor valorizing  meeting rooms list
     *
     * @param isTest if true list is reduced to only 3 meeting rooms (for testing purpose)
     */
    public DummyMeetingRoomRepository(boolean isTest) {
        meetingRooms = isTest ? DummyMeetingRoomGenerator.generateMeetingRooms().subList(0, 3) : DummyMeetingRoomGenerator.generateMeetingRooms();
    }

    /**
     * Get meeting rooms
     *
     * @return the list of meeting rooms
     */
    @Override
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    /**
     * Get meeting room from its id
     *
     * @param id of meeting room
     * @return the meeting room
     */
    @Override
    public MeetingRoom getMeetingRoomById(long id) {
        for (int i = 0; i < meetingRooms.size(); i++) {
            if (meetingRooms.get(i).getId() == id) return meetingRooms.get(i);
        }
        return null;
    }

    /**
     * Get meetings
     *
     * @return the full list of meetings
     */
    public List<Meeting> getMeetings() {
        return mMeetings;
    }

    /**
     * Get all meetings taking place in given meeting room
     *
     * @param meetingRoomId the given meeting room id
     * @return a list of meetings
     */
    @Override
    public List<Meeting> getMeetingsForGivenMeetingRoom(long meetingRoomId) {
        List<Meeting> meetings = new ArrayList<>();
        for (int i = 0; i < mMeetings.size(); i++) {
            if (mMeetings.get(i).getMeetingRoomId() == meetingRoomId) {
                meetings.add(mMeetings.get(i));
            }
        }
        return meetings;
    }

    /**
     * Check meeting rooms one by one in list to get first one available at given slot
     *
     * @param newMeetingStartTime Start time of new meeting to schedule
     * @param newMeetingEndTime   End time of new meeting to schedule
     * @return Available meeting rooms, empty list if none
     */
    @Override
    public List<MeetingRoom> getFreeMeetingRoomsAtGivenSlot(long newMeetingStartTime, long newMeetingEndTime) {
        List<MeetingRoom> freeMeetingRoomsAtGivenSlot = new ArrayList<>();
        for (int i = 0; i < meetingRooms.size(); i++) {
            List<Meeting> meetingsInCurrentMeetingRoom = this.getMeetingsForGivenMeetingRoom(meetingRooms.get(i).getId());
            if (meetingsInCurrentMeetingRoom.isEmpty()) {
                freeMeetingRoomsAtGivenSlot.add(meetingRooms.get(i));
            } else {
                boolean loop = true;
                int j = 0;
                while (loop && j < meetingsInCurrentMeetingRoom.size()) {
                    if (newMeetingStartTime >= meetingsInCurrentMeetingRoom.get(j).getMeetingEndTime()) {
                        if (j == meetingsInCurrentMeetingRoom.size() - 1) {
                            freeMeetingRoomsAtGivenSlot.add(meetingRooms.get(i));
                        }
                        j++;
                    } else if (newMeetingEndTime <= meetingsInCurrentMeetingRoom.get(j).getMeetingStartTime()) {
                        if (j == meetingsInCurrentMeetingRoom.size() - 1) {
                            freeMeetingRoomsAtGivenSlot.add(meetingRooms.get(i));
                        }
                        j++;
                    } else {
                        loop = false;
                    }
                }
            }
        }
        return freeMeetingRoomsAtGivenSlot;
    }

    /**
     * Schedule a meeting
     *
     * @param idMeetingRoom       The id of meeting room where meeting will take place
     * @param meetingSubject      The subject of this meeting
     * @param meetingDay          The day of the meeting
     * @param meetingStartTime    The meeting start time
     * @param meetingEndTime      The meeting end time
     * @param meetingParticipants The list of participants
     */
    @Override
    public void scheduleMeeting(long idMeetingRoom, String meetingSubject, String meetingDay, long meetingStartTime, long meetingEndTime, List<String> meetingParticipants) {
        Meeting newMeeting = new Meeting(idMeetingRoom, meetingSubject, meetingDay, meetingStartTime, meetingEndTime, meetingParticipants);
        mMeetings.add(newMeeting);
    }

    /**
     * Cancel a meeting
     *
     * @param meeting to cancel
     */
    @Override
    public void cancelMeeting(Meeting meeting) {
        for (int i = 0; i < mMeetings.size(); i++) {
            if (mMeetings.get(i).equals(meeting)) mMeetings.remove(i);
        }
    }

    /**
     * Get all meetings occurring on a specific day
     *
     * @param day the specific day
     * @return a list of meetings
     */
    @Override
    public List<Meeting> getMeetingsForGivenDay(String day) {
        //TODO
        return null;
    }
}
