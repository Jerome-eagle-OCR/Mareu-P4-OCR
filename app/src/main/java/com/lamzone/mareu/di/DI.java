package com.lamzone.mareu.di;

import com.lamzone.mareu.repository.DummyMeetingRoomRepository;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.repository.TestMeetingRoomRepository;

public class DI {

    /**
     * Dependency injector to get instance of repository
     */
    public static MeetingRoomRepository repository = new DummyMeetingRoomRepository();

    /**
     *
     * @return
     */
    public MeetingRoomRepository getMeetingRoomRepository() {
        return repository;
    }

    /**
     *
     * @return
     */
    public MeetingRoomRepository getTestMeetingRoomRepository() {
        return new DummyMeetingRoomRepository(3);
    }

}
