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
import java.util.List;

public class MeetingListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private RecyclerView mRecyclerView;
    private MeetingRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Toolbar mToolbar;
    private FloatingActionButton mNewMeetingButton;

    private MeetingRoomRepository repository;
    private List<Meeting> mMeetingList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_list);
        if (savedInstanceState != null) repository = DI.getNewMeetingRoomRepository();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        repository = DI.getMeetingRoomRepository();
        if (repository.getMeetings().isEmpty())
            repository.getMeetings().addAll(Utils.DUMMY_MEETINGS);
        mMeetingList = repository.getMeetings();

        mRecyclerView = findViewById(R.id.meeting_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MeetingRecyclerViewAdapter(mMeetingList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mNewMeetingButton = findViewById(R.id.create_meeting);
        mNewMeetingButton.setOnClickListener(v -> {
            Intent newMeetingActivityIntent = new Intent(MeetingListActivity.this, NewMeetingActivity.class);
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

    /**
     * Run filtering according to menu choice : by date (date picker), by room (grid of symbols), none
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getOrder()) {
            case 1:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                break;
            case 2:
                final Dialog dialog = new Dialog(MeetingListActivity.this);
                dialog.setContentView(R.layout.grid_meeting_rooms);
                MeetingRoomsGridAdapter meetingRoomsGridAdapter = new MeetingRoomsGridAdapter(repository.getMeetingRooms(), MeetingListActivity.this);
                GridView meetingRoomsGrid = dialog.findViewById(R.id.meeting_rooms_grid);
                meetingRoomsGrid.setAdapter(meetingRoomsGridAdapter);
                dialog.show();
                meetingRoomsGrid.setOnItemClickListener((parent, view, position, id) -> {
                    long selectedMeetingRoomId = meetingRoomsGridAdapter.getItem(position).getId();
                    mAdapter.refreshList(Utils.FilterType.BY_MEETING_ROOM, selectedMeetingRoomId);
                    dialog.dismiss();
                });
                break;
            case 3:
                mAdapter.refreshList(Utils.FilterType.NONE, 0);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set date according to picker selection and filter list by this date
     *
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mAdapter.refreshList(Utils.FilterType.BY_DATE, calendar.getTimeInMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.refreshList(); //refresh recyclerViewAdapter list taking into account previous filtering
    }
}