package com.lamzone.mareu.ui.meetings_list;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextInputLayout mMeetingSubjectLyt;
    private TextInputEditText mMeetingSubject;
    private TextInputEditText mMeetingDate;
    private TextInputEditText mMeetingTime;
    private TextInputEditText mMeetingRoom;
    private TextInputEditText mMeetingDuration;
    private TextInputEditText mMeetingParticipants;
    private ChipGroup mChipGroup;
    private Spinner mMeetingDurationSpinner;
    private Button mScheduleMeetingButton;

    private MeetingRoomRepository repository;
    private Calendar calendar;
    private long mMeetingStartTimeMillis;
    private long mMeetingEndTimeMillis;
    private long mMeetingDurationMillis;
    private MeetingRoom clickedMeetingRoom;
    private List<String> mMeetingParticipantList;
    private int chipId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);

        repository = DI.getMeetingRoomRepository();
        calendar = Calendar.getInstance();
        mMeetingParticipantList = new ArrayList<>();

        //Bind widgets
        mMeetingSubjectLyt = findViewById(R.id.meeting_subject_lyt);
        mMeetingSubject = findViewById(R.id.meeting_subject);
        mMeetingDate = findViewById(R.id.meeting_date);
        mMeetingTime = findViewById(R.id.meeting_time);
        mMeetingRoom = findViewById(R.id.meeting_room);
        mMeetingDuration = findViewById(R.id.meeting_duration);
        mMeetingDurationSpinner = findViewById(R.id.meeting_duration_spinner);
        mMeetingParticipants = findViewById(R.id.meeting_participants);
        mChipGroup = findViewById(R.id.chip_group);
        mScheduleMeetingButton = findViewById(R.id.schedule_meeting);

        init();
    }

    /**
     * Set all listeners including TextChangedListeners
     */
    private void init() {
        mMeetingDate.setOnClickListener(this);
        mMeetingTime.setOnClickListener(this);
        mMeetingRoom.setOnClickListener(this);
        mMeetingDuration.setOnClickListener(this);
        mScheduleMeetingButton.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meeting_durations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mMeetingDurationSpinner.setAdapter(adapter);
        mMeetingDurationSpinner.setOnItemSelectedListener(this);

        mMeetingSubjectLyt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                couldEnableScheduleButton();
            }
        });

        mMeetingParticipants.setOnEditorActionListener((v, actionId, event) -> {
            String inputText = v.getText().toString();
            if (!inputText.contains(" ") && inputText.contains("@") && !inputText.endsWith("@") && inputText.contains(".")) {
                mMeetingParticipantList.add(inputText);
                Chip chip = new Chip(NewMeetingActivity.this);
                chip.setText(inputText);
                chip.setChipBackgroundColorResource(R.color.teal_200);
                chip.setCloseIconVisible(true);
                chip.setCloseIconTintResource(R.color.lamzoneDarker);
                chip.setTextColor(getResources().getColor(R.color.lamzoneDarker));
                chip.setId(chipId++);
                chip.setOnCloseIconClickListener(v1 -> {
                    mChipGroup.removeView(v1);
                    mMeetingParticipantList.remove(chip.getText().toString());
                    couldEnableScheduleButton();
                });
                mChipGroup.addView(chip);
                mMeetingParticipants.setText(null);
            } else {
                Snackbar.make(v, "Email non valide, pas de nouveau particpant ajouté.", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.lamzoneDark)).show();
            }
            couldEnableScheduleButton();
            return false;
        });
    }

    /**
     * Set meeting date (calendar and formatted text) according to picker selection and reset meeting room
     *
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mMeetingDate.setText(Utils.formatDate(calendar, Utils.DATE_FORMAT_1));
        mMeetingRoom.setText(null);
    }

    /**
     * Set meeting time (calendar and formatted text) according to picker selection and reset meeting room
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        mMeetingTime.setText(Utils.formatDate(calendar, Utils.TIME_FORMAT));
        mMeetingRoom.setText(null);
    }

    /**
     * Set meeting duration (and text) according to spinner selection and reset meeting room
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mMeetingDuration.setText(parent.getItemAtPosition(position).toString());
        mMeetingDurationMillis = (position) * 15 * 60000;
        mMeetingRoom.setText(null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Set meeting start and end time and display a grid with only available rooms and finally set the picked one (and text)
     * Call couldEnableScheduleButton() to allow meeting scheduling in case other fields are all set
     *
     * @param v
     */
    private void showDialogMeetingRoomsGrid(View v) {
        Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.grid_meeting_rooms);

        mMeetingStartTimeMillis = calendar.getTimeInMillis();
        mMeetingEndTimeMillis = mMeetingStartTimeMillis + mMeetingDurationMillis;
        List<MeetingRoom> availableMeetingRooms = repository.getFreeMeetingRoomsAtGivenSlot(mMeetingStartTimeMillis, mMeetingEndTimeMillis);

        MeetingRoomsGridAdapter meetingRoomsGridAdapter = new MeetingRoomsGridAdapter(availableMeetingRooms, v.getContext());
        GridView meetingRoomsGrid = dialog.findViewById(R.id.meeting_rooms_grid);
        meetingRoomsGrid.setAdapter(meetingRoomsGridAdapter);
        meetingRoomsGrid.setOnItemClickListener((parent, view, position, id) -> {
            clickedMeetingRoom = meetingRoomsGridAdapter.getItem(position);
            mMeetingRoom.setText(clickedMeetingRoom.getMeetingRoomName());
            dialog.dismiss();
            couldEnableScheduleButton();
        });
        dialog.show();
    }

    /**
     * Enable schedule button if all is good (no empty field)
     */
    private void couldEnableScheduleButton() {
        mScheduleMeetingButton.setEnabled(!mMeetingSubject.getText().toString().equals("")
                && !mMeetingDate.getText().toString().equals("") && !mMeetingTime.getText().toString().equals("")
                && !mMeetingDuration.getText().toString().equals("") && !mMeetingRoom.getText().toString().equals("")
                && !mMeetingParticipantList.isEmpty());
    }

    /**
     * One onClick() for all with switch for each onClickListener (see init() )
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.meeting_date:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.meeting_time:
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                break;
            case R.id.meeting_duration:
                mMeetingDurationSpinner.setVisibility(View.VISIBLE);
                mMeetingDurationSpinner.performClick();
                break;
            case R.id.meeting_room:
                if (mMeetingDate.getText().toString().equals("") || mMeetingTime.getText().toString().equals("")
                        || mMeetingDuration.getText().toString().equals("")) {
                    Snackbar.make(v, "Veuillez d'abord saisir une date, une heure, une durée.", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.lamzoneDark)).show();
                } else {
                    showDialogMeetingRoomsGrid(v);
                }
                break;
            case R.id.schedule_meeting:
                repository.scheduleMeeting(clickedMeetingRoom.getId(), mMeetingSubject.getText().toString(),
                        mMeetingStartTimeMillis, mMeetingEndTimeMillis, mMeetingParticipantList);
                finish();
                break;
            default:
        }
    }
}