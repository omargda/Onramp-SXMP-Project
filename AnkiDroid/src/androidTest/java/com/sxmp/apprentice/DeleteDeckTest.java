package com.sxmp.apprentice;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.ichi2.anki.DeckPicker;
import com.ichi2.anki.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeleteDeckTest {

    @Rule
    public ActivityScenarioRule<DeckPicker> activityScenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private final String DECKNAME_DELETE = "1 DeleteDeckTest Delete " +
            (new Random().nextInt(10000000));
    private final String DECKNAME_STAY = "1 DeleteDeckTest Stay";

    private void createDeck(String deckName) {
        //Click the FAB on bottom right, then click the "Create deck" button fab_expand_menu_button
        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.add_deck_action)).perform(click());

        //Type into the dialog's EditText
        onView(allOf(withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.md_customViewFrame))))
                .perform(replaceText(deckName), closeSoftKeyboard())
                .check(matches(withText(deckName)));

        //Save the new deck
        onView(withText(R.string.dialog_ok)).perform(click());
    }

    @Test
    public void deleteDeckTest() {
        createDeck(DECKNAME_DELETE);
        createDeck(DECKNAME_STAY);

        //Scroll to DECKNAME_DELETE to bring in view
        onView(withId(R.id.files))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(DECKNAME_DELETE))));
        onView(withText(DECKNAME_DELETE)).check(matches(isDisplayed()));

        //Long click on new deck, DECKNAME_DELETE, to bring up options
        onView(allOf(withClassName(endsWith("FixedTextView")),
                withId(R.id.deckpicker_name),
                withText(DECKNAME_DELETE)))
                .perform(longClick());

        //Click "Delete deck" (R.string.contextmenu_deckpicker_delete_deck) option
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                withId(R.id.md_title),
                withText(R.string.contextmenu_deckpicker_delete_deck)))
                .perform(click());

        //Check deck is gone
        onView(allOf(withClassName(endsWith("FixedTextView")),
                withId(R.id.deckpicker_name),
                withText(DECKNAME_DELETE)))
                .check(doesNotExist());
    }
}