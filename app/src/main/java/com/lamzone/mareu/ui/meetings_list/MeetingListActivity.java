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
import androidx.annotation.VisibleForTesting;
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
import com.lamzone.mareu.utils.UtilsForTesting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.meeting_list)
    RecyclerView mRecyclerView;
    private MeetingRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.create_meeting)
    FloatingActionButton mCreateMeetingBtn;

    private MeetingRoomRepository repository;
    private List<Meeting> mMeetingList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_list);
        ButterKnife.bind(this);
        if (savedInstanceState != null) repository = DI.getNewMeetingRoomRepository();
        repository = DI.getMeetingRoomRepository();
        setSupportActionBar(mToolbar);
        setMeetingList();
        setNewMeetingButton();
    }

    /**
     * Get dummy meetings and set recyclerView
     */
    private void setMeetingList() {
        if (repository.getMeetings().isEmpty())
            repository.getMeetings().addAll(Utils.DUMMY_MEETINGS);
        mMeetingList = repository.getMeetings();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MeetingRecyclerViewAdapter(mMeetingList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Set onClick and onLongClick listeners for new meeting button
     */
    private void setNewMeetingButton() {
        mCreateMeetingBtn.setOnClickListener(v -> {
            Intent newMeetingActivityIntent = new Intent(MeetingListActivity.this, NewMeetingActivity.class);
            startActivity(newMeetingActivityIntent);
        });
        mCreateMeetingBtn.setOnLongClickListener(v -> {
            mCreateMeetingBtn.setAlpha(mCreateMeetingBtn.getAlpha() == 1 ? (float) 0.2 : (float) 1);
            return true;
        });

    }

    /**
     * Create menu for list filtering
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meeting_list_menu, menu);
        return true;
    }

    /**
     * Perform filtering according to menu choice : by date (date picker), by room (grid of symbols), none
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

    @VisibleForTesting
    public void emptyMeetingList() {
        repository.getMeetings().clear();
        mAdapter.notifyDataSetChanged();
    }

    @VisibleForTesting
    public void addAllTestMeetings() {
        repository.getMeetings().addAll(UtilsForTesting.TEST_MEETINGS);
        mAdapter.notifyDataSetChanged();
    }

    @VisibleForTesting
    public void addAfterTomorrowTestMeeting() {
        repository.getMeetings().add(UtilsForTesting.TEST_MEETING);
        mAdapter.notifyDataSetChanged();
    }
}