package com.lamzone.mareu.repository;

import com.lamzone.mareu.R;
import com.lamzone.mareu.model.MeetingRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingRoomGenerator {

    public static final List<MeetingRoom> DUMMY_MEETING_ROOMS = Arrays.asList(
            new MeetingRoom("Bouleau... à abattre", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Cyprès... du but", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Chêne... de valeur", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Saule... ou à plusieurs", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Hêtre... toujours positif", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Peuplier... mais ne rompt pas", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Mélèze... pas tomber", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Thuya... rivera", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("If... watch your switchCase", R.drawable.dflt_meeting_room_image),
            new MeetingRoom("Epicéa... ton tour", R.drawable.dflt_meeting_room_image)
    );


    public static List<MeetingRoom> generateMeetingRooms() {
        return new ArrayList<>(DUMMY_MEETING_ROOMS);
    }
}
