package com.sxmp.apprentice;

import android.content.res.Resources;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.ichi2.anki.DeckPicker;
import com.ichi2.anki.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;



@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateNotesTest {

    @Rule
    public ActivityScenarioRule<DeckPicker> scenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private Resources activityRes;
    private View decorView;
    private DeckPicker theActivity;
    private final String FRONT_TEST_STR = "CreateNotesTest front " +
            (new Random().nextInt(10000000));;
    private final String BACK_TEST_STR = "CreateNotesTest back " +
            (new Random().nextInt(10000000));;


    @Before
    public void setupToNoteEditorActivity() {

        scenarioRule.getScenario()
                .onActivity(new ActivityScenario.ActivityAction<DeckPicker>() {

                    @Override
                    public void perform(DeckPicker activity) {
                        activityRes = activity.getResources();
                        decorView = activity.getWindow().getDecorView();
                        theActivity = activity;
                    }
                });

        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.add_note_action)).perform(click());

        onView(isRoot()).perform(closeSoftKeyboard());
    }

    @Test
    public void noteEditorDidLoadTest() {
        //Confirm the Toolbar title matches expected title
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                withText(R.string.menu_add_note),
                isDescendantOfA(withId(R.id.toolbar))))
                .check(matches(isDisplayed()));

        //Confirm an object from note_editor.xml is in the view hierarchy
        onView(withId(R.id.note_editor_layout)).check(matches(isDisplayed()));
    }

    /**
     * Checks if the Type TextView and spinner is in view and interactive.
     */
    @Test
    public void typeSpinnerTest() {
        onView(withId(R.id.CardEditorModelText)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.note_type_spinner)).perform(scrollTo()).check(matches(isDisplayed()));

        //Click on Type spinner
        onView(withId(R.id.note_type_spinner)).perform(scrollTo(), click());

        //Convert "Basic (and reversed card) into a String
        String typeStr = activityRes.getString(R.string.forward_reverse_model_name);

        //Test the spinner changes
        onData(allOf(is(instanceOf(String.class)), is(typeStr)))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_type_spinner))))
                .check(matches(withText(R.string.forward_reverse_model_name)));
    }

    /*
     * Helper method to set the text for the front and back
     * as well as set the note type and deck.
     */
    private void setupNote(String frontText, String backText,
                           String noteType, String deckName) {
        //Set the Type Spinner to noteType and verify
        onView(withId(R.id.note_type_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(noteType)))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_type_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString(noteType))));

        //Set Deck to deckName and verify
        onView(withId(R.id.note_deck_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(deckName)))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_deck_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString(deckName))));

        //Enter text for the front of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription(R.string.front_field_name),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(frontText), closeSoftKeyboard())
                .check(matches(withText(frontText)));

        //Enter text for the back of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription(R.string.back_field_name),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(backText), closeSoftKeyboard())
                .check(matches(withText(backText)));
    }

    private void checkNoteEditorCleared() {
        //Check the Front EditText field is empty
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Front"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo())
                .check(matches(withText("")));

        //Check the Back EditText field is empty
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Back"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo())
                .check(matches(withText("")));
    }

    @Test
    public void createBasicNoteTest() {
        setupNote(FRONT_TEST_STR, BACK_TEST_STR,
                "Basic", "Default");

        //Get the string for the Toast message that will appear from plurals
        String cardsAddedTextString = activityRes.
                getQuantityString(R.plurals.factadder_cards_added, 1);
        //Remove the number so we can match text
        String toastTextNumberRemoved =  cardsAddedTextString.split(" ", 2)[1];

        //Save (Create) the note
        onView(withId(R.id.action_save)).perform(click());

        //Check for Toast message
        onView(withText(containsString(toastTextNumberRemoved)))
                .inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));

        checkNoteEditorCleared();
    }
}