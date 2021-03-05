package com.sxmp.apprentice;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.ichi2.anki.DeckPicker;
import com.ichi2.anki.R;
import com.ichi2.libanki.sched.AbstractDeckTreeNode;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeckCreatorTest {

    @Rule
    public ActivityScenarioRule<DeckPicker> activityScenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private View decorView;
    private final String DECKNAME = "1 DeckAddTest " + (new Random().nextInt(10000000));
    private final String DECKNAME_GIBBERISH = "1 DeckAddTest l≈Ω≤;†:ªÁVpeIk≠-≥ˆ>v";
    private final String DECKNAME_LONG = "1 DeckAddTest name which is really really really really" +
            "really really really really really really really really really really really really" +
            "really really really really really really really really really really really really" +
            "really really really really really really really really really really really long";
    private final String GIBBERISH = "oiejoiwej09ioj";

    @Before
    public void setUp() {
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<DeckPicker>() {

            @Override
            public void perform(DeckPicker activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    private void openCreateDeck() {
        //Click the FAB on bottom right, then click the "Create deck" button fab_expand_menu_button
        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.add_deck_action)).perform(click());
    }

    @Test
    public void checkCreateDeckDialogTest() {
        openCreateDeck();

        //Check if dialog appears — dialog is an MDRootLayout with res-name=md_root
        onView(withId(R.id.md_root)).check(matches(isDisplayed()));
    }

    @Test
    public void dialogCancelDismissTest() {
        openCreateDeck();

        //Click the "CANCEL" button on dialog
        onView(allOf(withId(R.id.md_buttonDefaultNegative),
                isDescendantOfA(withId(R.id.md_root))))
                .perform(click());

        //Check if dialog is no longer displayed
        onView(withId(R.id.md_root)).check(doesNotExist());
    }

    @Test
    public void dialogDismissedInvalidDeckTest() {
        openCreateDeck();

        //Click the "OK" button on dialog
        onView(allOf(withId(R.id.md_buttonDefaultPositive),
                isDescendantOfA(withId(R.id.md_root))))
                .perform(click());

        //Check for Toast with R.string.invalid_deck_name message
        onView(withText(R.string.invalid_deck_name))
                .inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));

        //Check if dialog is no longer displayed
        onView(withId(R.id.md_root)).check(doesNotExist());
    }

    @Test
    public void dialogCancelWithTextTest() {
        openCreateDeck();

        //Type into the dialog's EditText
        onView(allOf(withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.md_customViewFrame))))
                .perform(typeText(GIBBERISH), closeSoftKeyboard())
                .check(matches(withText(GIBBERISH)));

        //Click the "CANCEL" button on dialog
        onView(allOf(withId(R.id.md_buttonDefaultNegative),
                isDescendantOfA(withId(R.id.md_root))))
                .perform(click());

        //Check if dialog is no longer displayed
        onView(withId(R.id.md_root)).check(doesNotExist());
    }

    public void createDeckAndCheck(String deckName) {
        openCreateDeck();

        //Type into the dialog's EditText
        onView(allOf(withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.md_customViewFrame))))
                .perform(replaceText(deckName), closeSoftKeyboard())
                .check(matches(withText(deckName)));

        //Save the new deck
        onView(withText(R.string.dialog_ok)).perform(click());

        //Check for the deck
        onView(withId(R.id.files))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(deckName))));
        onView(withText(deckName)).check(matches(isDisplayed()));
    }

    @Test
    public void createDeckTest() {
        createDeckAndCheck(DECKNAME);
    }

    @Test
    public void createDeckGibberishTest() {
        createDeckAndCheck(DECKNAME_GIBBERISH);
    }

    @Test
    public void createDeckLongTest() {
        createDeckAndCheck(DECKNAME_LONG);
    }

    @Test
    public void createEmptyDeckTest() {
        openCreateDeck();

        //Type into the dialog's EditText
        onView(allOf(withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.md_customViewFrame))))
                .perform(replaceText("         "), closeSoftKeyboard())
                .check(matches(withText("         ")));

        //Save the new deck
        onView(withText(R.string.dialog_ok)).perform(click());

        //Check for Toast with R.string.invalid_deck_name message
        onView(withText(R.string.invalid_deck_name))
                .inRoot(RootMatchers.withDecorView(not(decorView)))
                .check(matches(isDisplayed()));

        //Check if dialog is no longer displayed
        onView(withId(R.id.md_root)).check(doesNotExist());
    }
}