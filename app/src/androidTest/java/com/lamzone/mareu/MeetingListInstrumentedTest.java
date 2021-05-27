package com.lamzone.mareu;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.lamzone.mareu", appContext.getPackageName());
    }

    @Test
    public void meetingListShouldNotBeEmpty() {
    }

    @Test
    public void meetingListDeleteActionShouldRemoveItem() {
    }

    @Test
    public void filterByDateShouldFilterMeetingListBySelectedDate() {
    }

    @Test
    public void filterByMeetingRoomShouldFilterMeetingListBySelectedMeetingRoom() {
    }

    @Test
    public void reinitializeListActionShouldReinitializeMeetingListProperly() {
    }

    @Test
    public void addNewMeetingShouldAddNewItemInMeetingList() {
    }
}