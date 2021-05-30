package com.lamzone.mareu;

import com.lamzone.mareu.utils.Utils;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    final Calendar calendar = Calendar.getInstance();

    @Test
    public void formatDateWithSuccess() {
        calendar.set(2222, 1, 22);
        assertEquals("Vendredi 22 février 2222", Utils.formatDate(calendar, Utils.DATE_FORMAT_1));
    }

    @Test
    public void formatDateFromTimeStampWithSuccess() {
        calendar.set(2222, 1, 22, 22, 22);
        long timeStamp = calendar.getTimeInMillis();
        assertEquals("VEN\n22\nFEV\n22:22", Utils.formatDate(timeStamp));
    }

    @Test
    public void formatTimeWithSuccess() {
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 22);
        assertEquals("22:22", Utils.formatDate(calendar, Utils.TIME_FORMAT));
    }
}