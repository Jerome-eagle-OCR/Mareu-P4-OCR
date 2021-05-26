package com.lamzone.mareu.ui.meetings_list;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.MeetingRoom;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NewMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {


    private TextInputEditText mMeetingDate;
    private TextInputEditText mMeetingTime;
    private TextInputEditText mMeetingRoom;
    private TextInputEditText mMeetingDuration;
    private TextInputEditText mMeetingParticipants;
    private HorizontalScrollView mChipGroupScrollView;
    private ChipGroup mChipGroup;
    private Spinner mMeetingDurationSpinner;
    private Button mScheduleMeetingButton;

    private MeetingRoomRepository repository;
    private Calendar calendar;
    private int chipId;

    private long mMeetingStartTimeMillis;
    private long mMeetingEndTimeMillis;
    private long mMeetingDurationMillis;
    private MeetingRoom clickedMeetingRoom;
    private List<String> mMeetingParticipantsList;


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
        mMeetingParticipants = findViewById(R.id.meeting_participants);
        mChipGroupScrollView = findViewById(R.id.chip_group_scrollview);
        mChipGroup = findViewById(R.id.chip_group);
        mScheduleMeetingButton = findViewById(R.id.schedule_meeting);

        mMeetingDate.setOnClickListener(v -> {
            //Toast.makeText(this, Objects.requireNonNull(mMeetingDate.getText()).toString(), Toast.LENGTH_LONG).show();
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        mMeetingTime.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment();
            if (!Objects.requireNonNull(mMeetingDate.getText()).toString().isEmpty()) {
                timePicker.show(getSupportFragmentManager(), "time picker");
            } else {
                Toast.makeText(v.getContext(), "Veuillez d'abord saisir une date.", Toast.LENGTH_SHORT).show();
            }
        });

        mMeetingDuration.setOnClickListener(v -> {
            if (!Objects.requireNonNull(mMeetingTime.getText()).toString().isEmpty()) {
                mMeetingDurationSpinner.setVisibility(View.VISIBLE);
                mMeetingDurationSpinner.performClick();
            } else {
                Toast.makeText(v.getContext(), "Veuillez d'abord saisir date et heure.", Toast.LENGTH_SHORT).show();
            }
        });
        setMeetingDurationSpinnerAndListener();

        mMeetingRoom.setOnClickListener(v -> {
            if (!Objects.requireNonNull(mMeetingDuration.getText()).toString().isEmpty()) {
                showDialogMeetingRoomsGrid(v);
            } else {
                Toast.makeText(v.getContext(), "Veuillez d'abord saisir date, heure et durée.", Toast.LENGTH_SHORT).show();
            }
        });

        mMeetingParticipantsList = new ArrayList<>();
        mChipGroup.setOnClickListener(view -> mMeetingParticipants.performClick());
        mMeetingParticipants.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Saisir une adresse de courriel :").setIcon(AppCompatResources.getDrawable(v.getContext(), R.drawable.lamzone));
            EditText inputEmail = new EditText(v.getContext());
            inputEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            inputEmail.setHint(" prénom.nom@lamzone.com");
            inputEmail.setBackground(AppCompatResources.getDrawable(v.getContext(), R.drawable.lyr_input_participant_dlg));
            builder.setView(inputEmail);
            builder.setPositiveButton("AJOUTER", (dialog, which) -> {
                if (inputEmail.getText().length() != 0 && inputEmail.getText().toString().contains("@")) {
                    mMeetingParticipants.setText(" ");
                    mMeetingParticipantsList.add(inputEmail.getText().toString());
                    Chip chip = new Chip(NewMeetingActivity.this);
                    chip.setText(inputEmail.getText());
                    chip.setChipBackgroundColorResource(R.color.teal_200);
                    chip.setCloseIconVisible(true);
                    chip.setCloseIconTintResource(R.color.lamzoneDarker);
                    chip.setTextColor(getResources().getColor(R.color.lamzoneDarker));
                    chip.setId(chipId++);
                    chip.setOnCloseIconClickListener(v1 -> {
                        mChipGroup.removeView(v1);
                        mMeetingParticipantsList.remove(chip.getText().toString());
                        checkParticipants();
                    });
                    mChipGroup.addView(chip);
                } else {
                    Toast.makeText(v.getContext(), "Adresse non valide, aucun participant ajouté.", Toast.LENGTH_SHORT).show();
                }
                checkParticipants();
            });
            builder.show();
        });

        mScheduleMeetingButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Entrer le sujet de la réunion :").setIcon(AppCompatResources.getDrawable(v.getContext(), R.drawable.lamzone));
            EditText inputSubject = new EditText(v.getContext());
            inputSubject.setHint(" Sujet");
            inputSubject.setBackground(AppCompatResources.getDrawable(v.getContext(), R.drawable.lyr_input_participant_dlg));
            builder.setView(inputSubject);
            builder.setPositiveButton("Ajouter la nouvelle réunion", (dialogInterface, i) -> {
                repository.scheduleMeeting(clickedMeetingRoom.getId(), inputSubject.getText().toString(), mMeetingStartTimeMillis, mMeetingEndTimeMillis, mMeetingParticipantsList);
                Intent meetingsListActivityIntent = new Intent(v.getContext(), MeetingsListActivity.class);
                startActivity(meetingsListActivityIntent);
            });
            builder.show();
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
        }
        mMeetingRoom.setText(null);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String selectedTime = Utils.formatDate(calendar, Utils.TIME_FORMAT);
        if (!selectedTime.isEmpty()) {
            mMeetingTime.setText(selectedTime);
            mMeetingDurationSpinner.setVisibility(View.VISIBLE);
        }
        mMeetingRoom.setText(null);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedDuration = parent.getItemAtPosition(position).toString();
        mMeetingDuration.setText(selectedDuration);
        mMeetingDurationMillis = (position + 1) * 15 * 60000;
        mMeetingRoom.setText(null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

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

    private void checkParticipants() {
        if (mChipGroup.getChildCount() != 0) {
            mChipGroupScrollView.setVisibility(View.VISIBLE);
        } else {
            mChipGroupScrollView.setVisibility(View.INVISIBLE);
            mMeetingParticipants.setText(null);
        }
        couldEnableScheduleButton();
    }

    private void couldEnableScheduleButton() {
        mScheduleMeetingButton.setEnabled(mMeetingDate.getText() != null
                && mMeetingTime.getText() != null
                && mMeetingDuration.getText() != null
                && mMeetingRoom.getText() != null
                && mMeetingParticipants.getText() != null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMeetingDate.setText("");
        mMeetingTime.setText("");
        mMeetingRoom.setText("");
        mMeetingDuration.setText("");
        mMeetingParticipants.setText("");
    }
}