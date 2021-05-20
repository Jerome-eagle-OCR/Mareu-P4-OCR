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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.utils.Utils;

import java.util.Calendar;
import java.util.List;

public class NewMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {


    private TextInputEditText mMeetingDate;
    private TextInputEditText mMeetingTime;
    private TextInputEditText mMeetingRoom;
    private TextInputEditText mMeetingDuration;
    private Spinner mMeetingDurationSpinner;
    private Button mScheduleMeetingButton;

    private MeetingRoomRepository repository;

    private boolean isDateOk;
    private boolean isTimeOk;
    private boolean isDurationOk;
    private boolean isRoomOk;
    private boolean isParticipantsOk;

    Calendar calendar;

    private long mMeetingStartTimeMillis;
    private long mMeetingEndTimeMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        repository = DI.getMeetingRoomRepository();

        calendar = Calendar.getInstance();

        mMeetingDate = findViewById(R.id.meeting_date);
        mMeetingTime = findViewById(R.id.meeting_time);
        mMeetingRoom = findViewById(R.id.meeting_room);
        mMeetingDuration = findViewById(R.id.meeting_duration);
        mScheduleMeetingButton = findViewById(R.id.schedule_meeting);


        mMeetingDate.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        mMeetingTime.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment();
            if (isDateOk) {
                timePicker.show(getSupportFragmentManager(), "time picker");
            } else {
                Toast.makeText(v.getContext(), "Veuillez d'abord saisir une date.", Toast.LENGTH_SHORT).show();
            }
        });

        mMeetingDuration.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Veuillez d'abord saisir date et heure.", Toast.LENGTH_SHORT).show();
        });

        setMeetingDurationSpinnerAndListener();

        mMeetingRoom.setOnClickListener(v -> {
            if (isDurationOk) {
                showDialogMeetingRoomsGrid(v);
            } else {
                Toast.makeText(v.getContext(), "Veuillez d'abord saisir date, heure et durée.", Toast.LENGTH_SHORT).show();
            }
        });

        mScheduleMeetingButton.setOnClickListener(v -> {
            //repository.scheduleMeeting();
        });
    }

    private void setMeetingDurationSpinnerAndListener() {
        mMeetingDurationSpinner = findViewById(R.id.meeting_duration_spinner);
        mMeetingDurationSpinner.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meeting_durations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mMeetingDurationSpinner.setAdapter(adapter);
        mMeetingDurationSpinner.setOnItemSelectedListener(this);
    }

    private void showDialogMeetingRoomsGrid(View v) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.grid_meeting_rooms);

        List<MeetingRoom> availableMeetingRooms = repository.getFreeMeetingRoomsAtGivenSlot(mMeetingStartTimeMillis, mMeetingEndTimeMillis);

        MeetingRoomsGridAdapter meetingRoomsGridAdapter = new MeetingRoomsGridAdapter(availableMeetingRooms, v.getContext());

        GridView meetingRoomsGrid = dialog.findViewById(R.id.meeting_rooms_grid);
        meetingRoomsGrid.setAdapter(meetingRoomsGridAdapter);

        dialog.show();

        meetingRoomsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingRoom clickedMeetingRoom = meetingRoomsGridAdapter.getItem(position);
                mMeetingRoom.setText(clickedMeetingRoom.getMeetingRoomName());
                isRoomOk = true;
                couldEnableScheduleButton();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = Utils.formatDate(calendar, Utils.DATE_FORMAT_1);
        if (!selectedDate.isEmpty()) {
            mMeetingDate.setText(selectedDate);
            isDateOk = true;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String selectedTime = Utils.formatDate(calendar, Utils.TIME_FORMAT);
        if (!selectedTime.isEmpty()) {
            mMeetingTime.setText(selectedTime);
            isTimeOk = true;
            mMeetingDurationSpinner.setVisibility(View.VISIBLE);
            mMeetingStartTimeMillis = calendar.getTimeInMillis();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedDuration = parent.getItemAtPosition(position).toString();
        mMeetingDuration.setText(selectedDuration);
        long meetingDurationMillis = position * 15 * 60000;
        mMeetingEndTimeMillis = mMeetingStartTimeMillis + meetingDurationMillis;
        isDurationOk = meetingDurationMillis != 0;
    }

    private void couldEnableScheduleButton() {
        mScheduleMeetingButton.setEnabled(isDateOk && isTimeOk && isDurationOk && isRoomOk && isParticipantsOk);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}