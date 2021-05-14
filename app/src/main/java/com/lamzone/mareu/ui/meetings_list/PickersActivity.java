package com.lamzone.mareu.ui.meetings_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.lamzone.mareu.R;

import java.text.DateFormat;
import java.util.Calendar;

public class PickersActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Button mButton;
    private TextView mTextView;
    private Button mButton2;
    private TextView mTextView2;
    private TextView mTextView3;

    private long timeFromDatePicker;
    private long combinationTime;
    private TextView scrollingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickers_testing);

        scrollingText = findViewById(R.id.textView3);
        scrollingText.setSelected(true);
/**
 getSupportActionBar().setDisplayShowHomeEnabled(true);
 getSupportActionBar().setLogo(R.mipmap.ic_launcher);
 getSupportActionBar().setDisplayUseLogoEnabled(true);
 getSupportActionBar().setTitle("    Gérer vos réunions");
 **/

        mButton = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);

        mButton2 = findViewById(R.id.button2);
        mTextView2 = findViewById(R.id.textView2);

        mTextView3 = findViewById(R.id.textView3);

        mButton.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        mButton2.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timeFromDatePicker = calendar.getTimeInMillis() - ((((calendar.get(Calendar.HOUR) + calendar.get(Calendar.AM_PM)) * 60) + calendar.get(Calendar.MINUTE)) * 60000);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        mTextView.setText(selectedDate);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String selectedTime = String.valueOf(minute).length() == 1 ? hourOfDay + " : 0" + minute : hourOfDay + " : " + minute;
        mTextView2.setText(selectedTime);
        combinationTime = timeFromDatePicker + ((hourOfDay * 60 + minute) * 60000);
        String finalTime = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(combinationTime);
        mTextView3.setText(finalTime);
    }

}