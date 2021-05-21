package com.lamzone.mareu.ui.meetings_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MeetingsListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mNewMeetingButton;

    private MeetingRoomRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_list);

        repository = DI.getMeetingRoomRepository();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("    GERER VOS REUNIONS");

        for (int i = 0; i < Utils.DUMMY_MEETINGS.size(); i++) {
            repository.scheduleMeeting(Utils.DUMMY_MEETINGS.get(i));
        }
        List<Meeting> meetingList = repository.getMeetings();
        Collections.sort(meetingList, new Utils.SortByStartTime());

        mRecyclerView = findViewById(R.id.meeting_list);
        mRecyclerView.hasFixedSize();
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MeetingRecyclerViewAdapter(meetingList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mNewMeetingButton = findViewById(R.id.create_meeting);
        mNewMeetingButton.setOnClickListener(v -> {
            Intent newMeetingActivityIntent = new Intent(MeetingsListActivity.this, NewMeetingActivity.class);
            startActivity(newMeetingActivityIntent);
        });
        mNewMeetingButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mNewMeetingButton.setAlpha(mNewMeetingButton.getAlpha() == 1 ? (float) 0.2 : (float) 1);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meeting_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.date_filtering:
                //TODO : add DatePicker and update getMeetingsForGivenDay
                //repository.getMeetingsForGivenDay(date);
            case R.id.meeting_room_filtering:
                //TODO : add MeetingRoomGrid Dialog to pick a meeting room
            case R.id.reinitialize_list:
                //TODO
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}