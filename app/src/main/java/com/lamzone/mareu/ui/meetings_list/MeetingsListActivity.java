package com.lamzone.mareu.ui.meetings_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class MeetingsListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mCreateMeetingButton;

    private MeetingRoomRepository repository;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meeting_list_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_list);

        repository = DI.getMeetingRoomRepository();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("    GERER VOS REUNIONS");


        ArrayList<MeetingItem> meetingItems = new ArrayList<>();
        for (int i = 0; i < Utils.TEST_MEETINGS.size(); i++) {
            repository.scheduleMeeting(Utils.TEST_MEETINGS.get(i));
        }
        for (int i = 0; i < repository.getMeetings().size(); i++) {
            Meeting currentMeeting = repository.getMeetings().get(i);
            MeetingRoom currentMeetingRoom = repository.getMeetingRoomById(currentMeeting.getMeetingRoomId());
            meetingItems.add(new MeetingItem(currentMeetingRoom.getMeetingRoomName(), currentMeetingRoom.getMeetingRoomSymbol(),
                    currentMeeting.getMeetingSubject(), currentMeeting.getMeetingParticipants().toString(), currentMeeting.getMeetingDay()));
        }

        mRecyclerView = findViewById(R.id.meeting_list);
        mRecyclerView.hasFixedSize();
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MeetingRecyclerViewAdapter(meetingItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mCreateMeetingButton = findViewById(R.id.create_meeting);
        mCreateMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newMeetingActivityIntent = new Intent(MeetingsListActivity.this, NewMeetingActivity.class);
                startActivity(newMeetingActivityIntent);
            }
        });
    }
}