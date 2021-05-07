package com.lamzone.mareu.di;

import com.lamzone.mareu.repository.DummyMeetingRoomRepository;
import com.lamzone.mareu.repository.MeetingRoomRepository;

public class DI {

    /**
     * Dependency injector to get instance of repository
     */
    public static MeetingRoomRepository repository = new DummyMeetingRoomRepository(false);

    /**
     * @return
     */
    public static MeetingRoomRepository getMeetingRoomRepository() {
        return repository;
    }

    /**
     * @return
     */
    public static MeetingRoomRepository getTestMeetingRoomRepository() {
        return new DummyMeetingRoomRepository(true);
    }

}
