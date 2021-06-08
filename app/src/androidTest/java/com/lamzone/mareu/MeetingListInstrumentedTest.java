package com.lamzone.mareu;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.chip.Chip;
import com.lamzone.mareu.ui.meetings_list.MeetingListActivity;
import com.lamzone.mareu.utils.DeleteViewAction;
import com.lamzone.mareu.utils.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.lamzone.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.lamzone.mareu.utils.UtilsForTesting.EXPECTED_COUNT_FOR_SELECTED_ROOM;
import static com.lamzone.mareu.utils.UtilsForTesting.FIRST_POSITION_IN_LIST;
import static com.lamzone.mareu.utils.UtilsForTesting.NEW_MEETING_MEETINGROOM_NAME;
import static com.lamzone.mareu.utils.UtilsForTesting.NEW_MEETING_SUBJECT;
import static com.lamzone.mareu.utils.UtilsForTesting.SELECTED_DURATION_IN_SPINNER;
import static com.lamzone.mareu.utils.UtilsForTesting.SELECTED_MEETINGROOM_NAME;
import static com.lamzone.mareu.utils.UtilsForTesting.SELECTED_ROOM_INDEX_IN_GRID;
import static com.lamzone.mareu.utils.UtilsForTesting.SUBJECT_SUFFIX_LIST;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETING1;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETINGS;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETING_DATE;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETING_PARTICIPANTS;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETING_SUBJECT;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListInstrumentedTest {

    private Calendar calendar;

    @Rule
    public ActivityScenarioRule<MeetingListActivity> activityScenarioRule = new ActivityScenarioRule<>(MeetingListActivity.class);


    @Before
    public void setup() {
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::emptyMeetingList); //be sure we have no meetings in the list
    }


    @Test
    public void meetingListShouldNotBeEmpty() {
        //Given : we have 8 meetings scheduled
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        //When : we have the list displayed
        onView(withId(R.id.meeting_list)).check(matches(isDisplayed()));
        //Then : the 8 meetings appear
        onView(withId(R.id.meeting_list)).check(withItemCount(TEST_MEETINGS.size()));
    }

    @Test
    public void meetingListDeleteActionShouldRemoveItem() {
        //Given : we have 8 meetings scheduled
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        //When : we delete the first one in list, with subject "Test1", by clicking the delete button
        onView(allOf(withParent(withId(R.id.meeting_list)), withParentIndex(FIRST_POSITION_IN_LIST)))
                .check(matches(hasDescendant(allOf(withId(R.id.meeting_subject), withText(TEST_MEETING1.getMeetingSubject()))))); //check the meeting we will delete is at the expected position
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST, new DeleteViewAction()));
        //Then : the list counts one meeting less
        onView(withId(R.id.meeting_list)).check(withItemCount(TEST_MEETINGS.size() - 1));
    }

    @Test
    public void filterByDateShouldFilterMeetingListBySelectedDate() {
        //Given : we have 8 meetings occurring today and 1 occurring after tomorrow scheduled
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAfterTomorrowTestMeeting);
        onView(allOf(withParent(withId(R.id.meeting_list)), withParentIndex(TEST_MEETINGS.size())))
                .check(matches(hasDescendant(allOf(withId(R.id.meeting_date), withText(Utils.formatDate(TEST_MEETING_DATE)))))); //assert the after tomorrow meeting has been added
        //When : we filter the list by today's date
        onView(withContentDescription(R.string.menu)).perform(click());
        onView(withText(R.string.byDate)).perform(click());
        onView(withId(android.R.id.button1)).perform(click()); //date of today is selected by default
        //Then : the only 8 meetings displayed are all occurring today
        onView(withId(R.id.meeting_list)).check(withItemCount(TEST_MEETINGS.size()));
        checkOnlyTodayMeetings();
    }

    @Test
    public void filterByMeetingRoomShouldFilterMeetingListBySelectedMeetingRoom() {
        //Given : we have 8 meetings occurring in several meeting rooms
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        //When : we filter the list by "SUN" (id = 1) meeting room
        onView(withContentDescription(R.string.menu)).perform(click());
        onView(withText(R.string.byMeetingRoom)).perform(click());
        onView(allOf(withParent(withId(R.id.meeting_rooms_grid)), withParentIndex(SELECTED_ROOM_INDEX_IN_GRID))).perform(click());
        //Then : only the one and only meeting taking place in the "SUN" meeting room is displayed
        onView(withId(R.id.meeting_list)).check(withItemCount(EXPECTED_COUNT_FOR_SELECTED_ROOM));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST, click()));
        onView(allOf(withId(R.id.meeting_room_name), not(withText(SELECTED_MEETINGROOM_NAME)))).check(doesNotExist());
    }

    @Test
    public void reinitializeListActionShouldReinitializeMeetingListProperly() {
        //Given : we have 8 meetings occurring in several meeting rooms and we filter the list by "SUN" (id = 1) meeting room
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        onView(withContentDescription(R.string.menu)).perform(click());
        onView(withText(R.string.byMeetingRoom)).perform(click());
        onView(allOf(withParent(withId(R.id.meeting_rooms_grid)), withParentIndex(SELECTED_ROOM_INDEX_IN_GRID))).perform(click());
        // When : we reinitialize the list
        onView(withContentDescription(R.string.menu)).perform(click());
        onView(withText(R.string.resetList)).perform(click());
        //Then : the list is properly reinitialized and sorted
        onView(withId(R.id.meeting_list)).check(withItemCount(TEST_MEETINGS.size()));
        ///check items one by one to check sorting
        for (int i = FIRST_POSITION_IN_LIST; i < TEST_MEETINGS.size(); i++) {
            onView(allOf(withParent(withId(R.id.meeting_list)), withParentIndex(i)))
                    .check(matches(hasDescendant(allOf(withId(R.id.meeting_subject),
                            withText(TEST_MEETING_SUBJECT + SUBJECT_SUFFIX_LIST.get(i))))));
        }
    }

    @Test
    public void addNewMeetingShouldAddNewItemInMeetingList() {
        //Given : we have 8 meeting existing, we click the add meeting button, get to activity and set all fields for a new meeting
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        onView(withId(R.id.create_meeting)).perform(click());
        onView(withId(R.id.new_meeting_layout)).check(matches(isDisplayed())); //new meeting activity has been reached
        checkMeetingNotSchedulable(); //meeting cannot be scheduled yet
        checkMeetingRoomNotSettable(); //meeting room cannot be set yet
        fillNewMeetingForm();
        //When : we click on the schedule meeting button
        onView(withId(R.id.schedule_meeting_btn)).perform(click());
        //Then : the new meeting has been added and appears in the list
        onView(withId(R.id.meeting_list)).check(withItemCount(TEST_MEETINGS.size() + 1));
        onView(allOf(withParent(withId(R.id.meeting_list)), withParentIndex(4)))
                .check(matches(hasDescendant(allOf(withId(R.id.meeting_subject), withText(NEW_MEETING_SUBJECT)))));
    }

    private void checkOnlyTodayMeetings() {
        boolean noTodayMeetings = false;
        try {
            onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(Utils.formatDate(TEST_MEETING_DATE))))); //must throw PerformException

        } catch (PerformException expected) {
            noTodayMeetings = true;
        }
        assertTrue(noTodayMeetings);
    }

    private void fillNewMeetingForm() {
        ///set duration (will set spinner VISIBLE)
        onView(withId(R.id.meeting_duration)).perform(click());
        onView(withText(SELECTED_DURATION_IN_SPINNER)).check(matches(isDisplayed())); //check spinner actually visible
        onView(withText(SELECTED_DURATION_IN_SPINNER)).perform(click());
        onView(withId(R.id.meeting_duration)).check(matches(withText(SELECTED_DURATION_IN_SPINNER))); //check duration set
        checkMeetingRoomNotSettable();
        checkMeetingNotSchedulable();
        ///set date
        calendar = Calendar.getInstance();
        onView(withId(R.id.meeting_date)).perform(click());
        onView(withId(android.R.id.button1)).perform(click()); //date of today selected by default
        onView(withId(R.id.meeting_date)).check(matches(withText(Utils.formatDate(calendar, Utils.DATE_FORMAT_1)))); //check date set
        checkMeetingRoomNotSettable();
        checkMeetingNotSchedulable();
        ///set time
        onView(withId(R.id.meeting_time)).perform(click());
        onView(withId(android.R.id.button1)).perform(click()); //time of now selected by default
        onView(withId(R.id.meeting_time)).check(matches(withText(Utils.formatDate(calendar, Utils.TIME_FORMAT)))); //check time set
        checkMeetingNotSchedulable();
        ///set meeting room (now possible)
        onView(withId(R.id.meeting_room)).perform(click());
        onView(allOf(withParent(withId(R.id.meeting_rooms_grid)), withParentIndex(SELECTED_ROOM_INDEX_IN_GRID))).perform(click());
        onView(withId(R.id.meeting_room)).check(matches(withText(NEW_MEETING_MEETINGROOM_NAME))); //check room set ("Earth")
        checkMeetingNotSchedulable();
        ///set subject
        onView(withId(R.id.meeting_subject)).perform(click());
        onView(withId(R.id.meeting_subject)).perform(typeTextIntoFocusedView(NEW_MEETING_SUBJECT), closeSoftKeyboard(), pressImeActionButton());
        onView(withId(R.id.meeting_subject)).check(matches(withText(NEW_MEETING_SUBJECT))); //check subject set
        checkMeetingNotSchedulable();
        ///set participants
        for (int i = 0; i < TEST_MEETING_PARTICIPANTS.size(); i++) {
            onView(withId(R.id.meeting_participants)).perform(click()); //set participants
            onView(withId(R.id.meeting_participants)).perform(typeTextIntoFocusedView(TEST_MEETING_PARTICIPANTS.get(i)), closeSoftKeyboard(), pressImeActionButton());
            onView(allOf(withText(TEST_MEETING_PARTICIPANTS.get(i)), withClassName(is(Chip.class.getName())))).check(matches(isDisplayed())); //check chip added
            onView(withId(R.id.schedule_meeting_btn)).check(matches(isEnabled())); //scheduling must be now possible
        }
    }

    private void checkMeetingNotSchedulable() {
        onView(withId(R.id.schedule_meeting_btn)).check(matches(not(isEnabled())));
    }

    private void checkMeetingRoomNotSettable() {
        onView(withId(R.id.meeting_room)).perform(click()); //try to set meeting room
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(R.string.msg_room_disabled)))
                .check(matches(isDisplayed())); //snackbar asking to set date, time and duration displays
        onView(withId(R.id.meeting_rooms_grid)).check(doesNotExist());
    }
}