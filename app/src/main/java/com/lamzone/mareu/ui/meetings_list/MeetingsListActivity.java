package com.lamzone.mareu.ui.meetings_list;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MeetingsListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private RecyclerView mRecyclerView;
    private MeetingRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Toolbar mToolbar;
    private FloatingActionButton mNewMeetingButton;

    private MeetingRoomRepository repository = DI.getMeetingRoomRepository();
    List<Meeting> meetingList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_list);

        if (savedInstanceState != null) repository = DI.getNewMeetingRoomRepository();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        for (int i = 0; i < Utils.DUMMY_MEETINGS.size(); i++) {
            repository.scheduleMeeting(Utils.DUMMY_MEETINGS.get(i));
        }
        meetingList = repository.getMeetings();
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
        mNewMeetingButton.setOnLongClickListener(v -> {
            mNewMeetingButton.setAlpha(mNewMeetingButton.getAlpha() == 1 ? (float) 0.2 : (float) 1);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meeting_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getOrder()) {
            case 1:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                break;
            case 2:
                final Dialog dialog = new Dialog(MeetingsListActivity.this);
                dialog.setContentView(R.layout.grid_meeting_rooms);
                MeetingRoomsGridAdapter meetingRoomsGridAdapter = new MeetingRoomsGridAdapter(repository.getMeetingRooms(), MeetingsListActivity.this);
                GridView meetingRoomsGrid = dialog.findViewById(R.id.meeting_rooms_grid);
                meetingRoomsGrid.setAdapter(meetingRoomsGridAdapter);
                dialog.show();
                meetingRoomsGrid.setOnItemClickListener((parent, view, position, id) -> {
                    long selectedMeetingRoomId = meetingRoomsGridAdapter.getItem(position).getId();
                    mAdapter.refreshList(repository.getMeetingsForGivenMeetingRoom(selectedMeetingRoomId));
                    dialog.dismiss();
                });
                break;
            case 3:
                mAdapter.refreshList(repository.getMeetings());
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mAdapter.refreshList(repository.getMeetingsForGivenDate(calendar.getTimeInMillis()));
    }
}