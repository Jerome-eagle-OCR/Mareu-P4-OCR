package com.lamzone.mareu.ui.meetings_list;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    @BindView(R.id.meeting_subject_lyt)
    TextInputLayout mMeetingSubjectLyt;
    @BindView(R.id.meeting_subject)
    TextInputEditText mMeetingSubject;
    @BindView(R.id.meeting_date)
    TextInputEditText mMeetingDate;
    @BindView(R.id.meeting_time)
    TextInputEditText mMeetingTime;
    @BindView(R.id.meeting_room)
    TextInputEditText mMeetingRoom;
    @BindView(R.id.meeting_duration)
    TextInputEditText mMeetingDuration;
    @BindView(R.id.meeting_participants)
    TextInputEditText mMeetingParticipant;
    @BindView(R.id.chip_group)
    ChipGroup mChipGroup;
    @BindView(R.id.meeting_duration_spinner)
    Spinner mMeetingDurationSpinner;
    @BindView(R.id.schedule_meeting_btn)
    Button mScheduleMeetingBtn;

    private MeetingRoomRepository repository;
    private Calendar calendar;
    private List<String> mMeetingParticipantList;

    private long mMeetingStartTimeMillis;
    private long mMeetingEndTimeMillis;
    private long mMeetingDurationMillis;
    private MeetingRoom clickedMeetingRoom;
    private int chipId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        ButterKnife.bind(this);
        init();
    }

    /**
     * Initialize vars and set all listeners including TextChangedListeners and EditorActionListeners
     */
    private void init() {
        repository = DI.getMeetingRoomRepository();
        calendar = Calendar.getInstance();
        mMeetingParticipantList = new ArrayList<>();

        mMeetingDate.setOnClickListener(this);
        mMeetingTime.setOnClickListener(this);
        mMeetingRoom.setOnClickListener(this);
        mMeetingDuration.setOnClickListener(this);
        mScheduleMeetingBtn.setOnClickListener(this);

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

        onMeetingParticipantSet();
    }

    /**
     * Called by init() to set specific meeting participant input
     */
    private void onMeetingParticipantSet() {
        mMeetingParticipant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ") || s.toString().contains(";")) {
                    mMeetingParticipant.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mMeetingParticipant.setOnEditorActionListener((v, actionId, event) -> {
            String inputText = v.getText().toString();
            inputText = inputText.replace(" ", "").replace(";", "");
            if (inputText.contains("@") && !inputText.endsWith("@") && inputText.contains(".") && !inputText.endsWith(".")) {
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
            } else {
                Snackbar.make(v, "Email non valide, pas de nouveau participant ajouté.", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.lamzoneDark)).show();
            }
            couldEnableScheduleButton();
            mMeetingParticipant.setText("");
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
        mMeetingRoom.setText("");
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
        mMeetingRoom.setText("");
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
        mMeetingDurationMillis = (position) * 15 * 60000; //each item is +15mn from previous one
        mMeetingRoom.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Set meeting start and end times and display a grid with only available rooms and finally set the picked one (and text)
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
        mScheduleMeetingBtn.setEnabled(!mMeetingSubject.getText().toString().equals("")
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
                    Snackbar.make(v, getResources().getString(R.string.msg_room_disabled), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.lamzoneDark)).show();
                } else {
                    showDialogMeetingRoomsGrid(v);
                }
                break;
            case R.id.schedule_meeting_btn:
                repository.scheduleMeeting(clickedMeetingRoom.getId(), mMeetingSubject.getText().toString(),
                        mMeetingStartTimeMillis, mMeetingEndTimeMillis, mMeetingParticipantList);
                finish();
                break;
            default:
        }
    }
}