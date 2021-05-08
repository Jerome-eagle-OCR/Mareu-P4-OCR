package com.lamzone.mareu.repository;

import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;

import java.util.List;

/**
 * Dummy mock for the repository
 */
public class DummyMeetingRoomRepository implements MeetingRoomRepository {

    private final List<MeetingRoom> meetingRooms;

    /**
     * DummyMeetingRoomRepository constructor valorizing  meeting rooms list
     *
     * @param isTest if true list is reduced to only 3 meeting rooms (for testing purpose)
     */
    public DummyMeetingRoomRepository(boolean isTest) {
        meetingRooms = isTest ? DummyMeetingRoomGenerator.generateMeetingRooms().subList(0, 3) : DummyMeetingRoomGenerator.generateMeetingRooms();
    }

    /**
     * Get the list of meeting rooms
     *
     * @return the list
     */
    @Override
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    /**
     * Check meeting rooms one by one in list to get first one available at given slot
     *
     * @param newMeetingStartTime Start time of new meeting to schedule
     * @param newMeetingEndTime   End time of new meeting to schedule
     * @return First available meeting room or null if none
     */
    @Override
    public MeetingRoom getFreeMeetingRoomAtGivenSlot(long newMeetingStartTime, long newMeetingEndTime) {
        for (int i = 0; i < meetingRooms.size(); i++) {
            if (meetingRooms.get(i).getMeetings().isEmpty()) {
                return meetingRooms.get(i);
            } else {
                boolean loop = true;
                int j = 0;
                while (loop && j < meetingRooms.get(i).getMeetings().size()) {
                    if (newMeetingStartTime >= meetingRooms.get(i).getMeetings().get(j).getMeetingEndTime()) {
                        if (j == meetingRooms.get(i).getMeetings().size() - 1) {
                            return meetingRooms.get(i);
                        } else {
                            j++;
                        }
                    } else if (newMeetingEndTime <= meetingRooms.get(i).getMeetings().get(j).getMeetingStartTime()) {
                        if (j == meetingRooms.get(i).getMeetings().size() - 1) {
                            return meetingRooms.get(i);
                        } else {
                            j++;
                        }
                    } else {
                        loop = false;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Schedule a meeting
     *
     * @param meetingRoom         The meeting room where meeting will take place
     * @param meetingStartTime    The meeting start time
     * @param meetingEndTime      The meeting end time
     * @param meetingSubject      The subject of this meeting
     * @param meetingParticipants The list of participants
     */
    @Override
    public void scheduleMeeting(MeetingRoom meetingRoom, String meetingSubject, long meetingStartTime, long meetingEndTime, List<String> meetingParticipants) {
        Meeting newMeeting = new Meeting(meetingSubject, meetingStartTime, meetingEndTime, meetingParticipants);
        meetingRoom.addMeeting(newMeeting);
    }

    @Override
    public void cancelMeeting(MeetingRoom meetingRoom, Meeting meeting) {
        meetingRoom.deleteMeeting(meeting);
    }

}
