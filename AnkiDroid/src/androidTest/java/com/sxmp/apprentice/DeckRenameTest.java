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
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeckRenameTest {

    @Rule
    public ActivityScenarioRule<DeckPicker> activityScenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private final String DECKNAME_RENAME = "1 DeckRenameTest Rename Me " +
            (new Random().nextInt(10000000));
    private final String DECKNAME_RENAMED = "1 DeckRenameTest Renamed " +
            (new Random().nextInt(10000000));;

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
    public void renameDeckTest() {
        createDeck(DECKNAME_RENAME);

        //Scroll to deck to bring in view
        onView(withId(R.id.files))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(DECKNAME_RENAME))));
        onView(withText(DECKNAME_RENAME)).check(matches(isDisplayed()));

        //Long click on new deck to bring up options
        onView(allOf(withClassName(endsWith("FixedTextView")),
                withId(R.id.deckpicker_name),
                withText(DECKNAME_RENAME)))
                .perform(longClick());

        //Click "Rename deck" (R.string.rename_deck) option
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                withId(R.id.md_title),
                withText(R.string.rename_deck)))
                .perform(click());

        //Replace text in EditText
        onView(withClassName(endsWith("FixedEditText")))
                .perform(replaceText(DECKNAME_RENAMED), closeSoftKeyboard());

        //Click the RENAME (R.string.rename) button
        onView(allOf(withClassName(endsWith("MDButton")),
                withId(R.id.md_buttonDefaultPositive),
                withText(R.string.rename)))
                .perform(click());

        //Check if deck exists with new name (DECKNAME_RENAMED)
        onView(allOf(withClassName(endsWith("FixedTextView")),
                withId(R.id.deckpicker_name),
                withText(DECKNAME_RENAMED)))
                .check(matches(isDisplayed()));

        //Check if deck with old name (DECKNAME_RENAME) no longer there
        onView(allOf(withClassName(endsWith("FixedTextView")),
                withId(R.id.deckpicker_name),
                withText(DECKNAME_RENAME)))
                .check(doesNotExist());
    }
}