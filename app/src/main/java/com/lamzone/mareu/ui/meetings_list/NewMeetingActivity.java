package com.lamzone.mareu.ui.meetings_list;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;

import java.util.Calendar;

public class NewMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {


    private TextInputEditText mMeetingDate;
    private TextInputEditText mMeetingTime;
    private TextInputEditText mMeetingRoom;
    private TextInputEditText mMeetingDuration;
    private Spinner mMeetingDurationSpinner;
    private Button mCreateButton;

    private MeetingRoomRepository repository;

    private boolean isDateOk;
    private boolean isTimeOk;
    private boolean isDurationOk;
    private boolean isRoomOk;
    private boolean isParticipantsOk;

    private long mMeetingStartTimeMillis;
    private long mMeetingDurationMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        repository = DI.getMeetingRoomRepository();

        mMeetingDate = findViewById(R.id.meeting_date);
        mMeetingTime = findViewById(R.id.meeting_time);
        mMeetingRoom = findViewById(R.id.meeting_room);
        mMeetingDuration = findViewById(R.id.meeting_duration);
        mCreateButton = findViewById(R.id.create);

        mCreateButton.setEnabled(isDateOk && isTimeOk && isDurationOk && isRoomOk && isParticipantsOk);

        mMeetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        mMeetingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        mMeetingDurationSpinner = findViewById(R.id.meeting_duration_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meeting_durations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mMeetingDurationSpinner.setAdapter(adapter);
        mMeetingDurationSpinner.setOnItemSelectedListener(this);

        mMeetingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMeetingRoomsGrid(v);
            }
        });
    }

    private void showDialogMeetingRoomsGrid(View v) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.grid_meeting_rooms);

        MeetingRoomsGridAdapter meetingRoomsGridAdapter = new MeetingRoomsGridAdapter(repository.getMeetingRooms(), v.getContext());

        GridView meetingRoomsGrid = dialog.findViewById(R.id.meeting_rooms_grid);
        meetingRoomsGrid.setAdapter(meetingRoomsGridAdapter);

        dialog.show();

        meetingRoomsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingRoom clickedMeetingRoom = meetingRoomsGridAdapter.getItem(position);
                mMeetingRoom.setText(clickedMeetingRoom.getMeetingRoomName());
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        String selectedDate = dayOfMonth + " / " + (month + 1) + " / " + year;
        mMeetingDate.setText(selectedDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String selectedTime = String.valueOf(minute).length() == 1 ? hourOfDay + " : 0" + minute : hourOfDay + " : " + minute;
        mMeetingTime.setText(selectedTime);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedDuration = parent.getItemAtPosition(position).toString();
        mMeetingDuration.setText(selectedDuration);
        mMeetingDurationMillis = position * 15 * 60000;
        isDurationOk = mMeetingDurationMillis != 0;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}