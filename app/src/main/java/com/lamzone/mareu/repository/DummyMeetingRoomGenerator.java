package com.lamzone.mareu.repository;

import com.lamzone.mareu.R;
import com.lamzone.mareu.model.MeetingRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingRoomGenerator {

    public static final List<MeetingRoom> DUMMY_MEETING_ROOMS = Arrays.asList(
            new MeetingRoom(1, "Bouleau... à abattre", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(2, "Cyprès... du but", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(3, "Chêne... de valeur", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(4, "Saule... ou à plusieurs", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(5, "Hêtre... toujours positif", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(6, "Peuplier... mais ne rompt pas", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(7, "Mélèze... pas tomber", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(8, "Thuya... rivera", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(9, "If... watch your switchCase", R.drawable.dflt_meeting_room_image),
            new MeetingRoom(10, "Epicéa... ton tour", R.drawable.dflt_meeting_room_image)
    );


    public static List<MeetingRoom> generateMeetingRooms() {
        return new ArrayList<>(DUMMY_MEETING_ROOMS);
    }
}
