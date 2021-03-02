package com.sxmp.apprentice;

import android.content.res.Resources;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.ichi2.anki.DeckPicker;
import com.ichi2.anki.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public ActivityScenarioRule<DeckPicker> activityForNoteEditorScenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private Resources activityRes;
    private View decorView;
    private DeckPicker theActivity;

    @Before
    public void setupToNoteEditorActivity() {

        activityForNoteEditorScenarioRule.getScenario()
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

    private final String FRONTTESTSTR = "Testing with Front String";
    private final String BACKTESTSTR = "Testing with Back String";

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

        //Testing if Spinner changes using "Basic (and reversed card)" as test selection
        onView(withId(R.id.note_type_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("Basic (and reversed card)")))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_type_spinner))))
                .check(matches(withText(containsString("Basic (and reversed card)"))));
    }

    @Test
    public void createBasicNoteTest() {
        //Set the Type Spinner to "Basic" and verify
        onView(withId(R.id.note_type_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("Basic")))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_type_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString("Basic"))));

        //Set Deck to "Default" and verify
        onView(withId(R.id.note_deck_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("Default")))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_deck_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString("Default"))));

        //Enter text for the front of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Front"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(FRONTTESTSTR), closeSoftKeyboard())
                .check(matches(withText(FRONTTESTSTR)));

        //Enter text for the back of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Back"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(BACKTESTSTR), closeSoftKeyboard())
                .check(matches(withText(BACKTESTSTR)));

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

        //Navigate Back Up
        onView(allOf(withClassName(endsWith("AppCompatImageButton")),
                withContentDescription("Navigate up"),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .perform(click());

        //Open the navigation drawer
        onView(allOf(withClassName(endsWith("AppCompatImageButton")),
                withContentDescription("Navigate up"),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .perform(click());

        //Click on the Card Browser item
        onView(withId(R.id.nav_browser))
                .perform(click());

        //Click the toolbar spinner to open
        onView(allOf(withClassName(endsWith("AppCompatSpinner")),
                withId(R.id.toolbar_spinner),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .perform(click());

        //Select the "All decks" deck to test finding new note
        onView(withText("All decks"))
                .perform(click());

        //Verify toolbar spinner says "All decks"
        onView(allOf(withId(R.id.dropdown_deck_name),
                withClassName(endsWith("FixedTextView")),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .check(matches(withText(containsString("All decks"))));

        //Select Search item
        onView(allOf(withId(R.id.action_search),
                withClassName(endsWith("ActionMenuItemView")),
                withContentDescription("Search")))
                .perform(click());

        //Search for note according to the Front and Back texts
        onView(allOf(withId(R.id.action_search),
                withClassName(endsWith("SearchView"))))
                .perform(replaceTextAndSubmitSearchView(FRONTTESTSTR + " " + BACKTESTSTR));

        //Clear the SearchView text
        onView(allOf(withId(R.id.search_close_btn),
                withContentDescription("Clear query"),
                withClassName(endsWith("AppCompatImageView")),
                isDescendantOfA(withId(R.id.search_plate))))
                .perform(click(), closeSoftKeyboard());

        //Check if the new note
        onView(withText(FRONTTESTSTR)).check(matches(isDisplayed()));
    }

    public static ViewAction replaceTextAndSubmitSearchView(String text) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return withClassName(endsWith("SearchView"));
            }

            @Override
            public String getDescription() {
                return "replaceTextSearchView ViewAction";
            }

            @Override
            public void perform(UiController uiController, View view) {
                SearchView searchView = (SearchView) view;
                searchView.setQuery(text, true);
            }
        };
    }

}
