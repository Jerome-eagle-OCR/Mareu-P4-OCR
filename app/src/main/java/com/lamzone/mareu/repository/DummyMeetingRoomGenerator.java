package com.lamzone.mareu.repository;

import com.lamzone.mareu.R;
import com.lamzone.mareu.model.MeetingRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingRoomGenerator {

    public static final List<MeetingRoom> DUMMY_MEETING_ROOMS = Arrays.asList(
            new MeetingRoom(1, "Sun", R.drawable.sun),
            new MeetingRoom(2, "Mercury", R.drawable.mercury),
            new MeetingRoom(3, "Venus", R.drawable.venus),
            new MeetingRoom(4, "Earth", R.drawable.earth),
            new MeetingRoom(5, "Moon", R.drawable.moon),
            new MeetingRoom(6, "Mars", R.drawable.mars),
            new MeetingRoom(7, "Jupiter", R.drawable.jupiter),
            new MeetingRoom(8, "Saturn", R.drawable.saturn),
            new MeetingRoom(9, "Uranus", R.drawable.uranus),
            new MeetingRoom(10, "Neptune", R.drawable.neptune)
    );


    public static List<MeetingRoom> generateMeetingRooms() {
        return new ArrayList<>(DUMMY_MEETING_ROOMS);
    }
}
