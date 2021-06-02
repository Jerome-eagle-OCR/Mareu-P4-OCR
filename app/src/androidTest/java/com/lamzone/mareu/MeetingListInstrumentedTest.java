package com.lamzone.mareu;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.repository.MeetingRoomRepository;
import com.lamzone.mareu.ui.meetings_list.MeetingListActivity;
import com.lamzone.mareu.utils.CheckAssertionAction;
import com.lamzone.mareu.utils.DeleteViewAction;
import com.lamzone.mareu.utils.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.lamzone.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETING1;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETINGS;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETING_DATE;
import static com.lamzone.mareu.utils.UtilsForTesting.TEST_MEETING_SUBJECT;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListInstrumentedTest {

    private MeetingRoomRepository repository = DI.getTestMeetingRoomRepository();

    private final String SELECTED_MEETING_ROOM_NAME = "Sun";
    private final int EXPECTED_COUNT_FOR_SELECTED_ROOM = 1;
    private final int FIRST_POSITION_IN_LIST = 0;
    private final int SELECTED_ROOM_INDEX_IN_GRID = 0;

    @Rule
    public ActivityScenarioRule<MeetingListActivity> activityScenarioRule = new ActivityScenarioRule<>(MeetingListActivity.class);

    @Before
    public void setup() {
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::emptyMeetingList); //Be sure we have no meetings in the list
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

    @Test(expected = PerformException.class)
    public void meetingListDeleteActionShouldRemoveItem() {
        //Given : we have 8 meetings scheduled
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        //When : we delete the first one in list by clicking the button
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST, click()));
        onView(withText(TEST_MEETING1.getMeetingSubject())).check(matches(isDisplayed()));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST, new DeleteViewAction()));
        //Then : the meeting has disappeared
        onView(withId(R.id.meeting_list)).check(withItemCount(TEST_MEETINGS.size() - 1));
    }

    @Test(expected = PerformException.class)
    public void filterByDateShouldFilterMeetingListBySelectedDate() {
        //Given : we have 8 meetings occurring today and 1 occurring after tomorrow scheduled
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAllTestMeetings);
        activityScenarioRule.getScenario().onActivity(MeetingListActivity::addAfterTomorrowTestMeeting);
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(TEST_MEETINGS.size(), click()));//Click on the secondly added meeting just after the list of 8
        onView(withText(Utils.formatDate(TEST_MEETING_DATE))).check(matches(isDisplayed()));
        //When : we filter the list by today's date
        onView(withContentDescription(R.string.menu)).perform(click());
        onView(withText(R.string.byDate)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());//date of today is selected by default
        //Then : only the 8 meetings occurring today are displayed
        onView(withId(R.id.meeting_list)).check(withItemCount(TEST_MEETINGS.size()));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(Utils.formatDate(TEST_MEETING_DATE)))));
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
        onView(allOf(withId(R.id.meeting_room_name), not(withText(SELECTED_MEETING_ROOM_NAME)))).check(doesNotExist());
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
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 6))))));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST + 1,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 2))))));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST + 2,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 5))))));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST + 3,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 1))))));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST + 4,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 3))))));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST + 5,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 4))))));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST + 6,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 7))))));
        onView(withId(R.id.meeting_list)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_POSITION_IN_LIST + 7,
                new CheckAssertionAction(matches(hasDescendant(withText(TEST_MEETING_SUBJECT + 8))))));
    }

    @Test
    public void addNewMeetingShouldAddNewItemInMeetingList() {
    }
}