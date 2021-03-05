package com.sxmp.apprentice;

import android.content.res.Resources;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
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

import java.util.Random;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteChangeDeckTest {

    @Rule
    public ActivityScenarioRule<DeckPicker> activityScenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private Resources activityRes;
    private final String DECK_MOVE_TO = "1 Move To Deck " +
            (new Random().nextInt(10000000));
    private final String FRONT_NOTE_STR = "1 NoteChangeDeckTest Front " +
            (new Random().nextInt(10000000));
    private final String BACK_NOTE_STR = "1 NoteChangeDeckTest Back " +
            (new Random().nextInt(10000000));

    @Before
    public void setUp() {
        activityScenarioRule.getScenario()
                .onActivity(new ActivityScenario.ActivityAction<DeckPicker>() {

                    @Override
                    public void perform(DeckPicker activity) {
                        activityRes = activity.getResources();
                    }
                });

        createDeck(DECK_MOVE_TO);
    }

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

    private void createNote(String noteFront, String noteBack, String deckName) {
        //Set the Type Spinner to "Basic" and verify
        onView(withId(R.id.note_type_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("Basic")))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_type_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString("Basic"))));

        //Set Deck spinner to deckName and verify
        onView(withId(R.id.note_deck_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(deckName)))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_deck_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString(deckName))));

        //Enter text for the front of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Front"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(noteFront), closeSoftKeyboard())
                .check(matches(withText(noteFront)));

        //Enter text for the back of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Back"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(noteBack), closeSoftKeyboard())
                .check(matches(withText(noteBack)));

        //Save (Create) the note
        onView(withId(R.id.action_save)).perform(click());
    }

    private void navigateFromNoteEditorToCardBrowser() {
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
    }

    /*
     * Set the deck spinner in the Toolbar to deckName.
     */
    private void setupCardBrowserToolbarSpinner(String deckName) {
        //Click the toolbar spinner to open
        onView(allOf(withClassName(endsWith("AppCompatSpinner")),
                withId(R.id.toolbar_spinner),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .perform(click());

        //Select the "All decks" deck to test finding new note
        onView(withText(deckName))
                .perform(click());

        //Verify toolbar spinner says "All decks"
        onView(allOf(withId(R.id.dropdown_deck_name),
                withClassName(endsWith("FixedTextView")),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .check(matches(withText(containsString(deckName))));
    }

    /*
     * Search for the card with front text noteFrontText and back text
     * noteBackText.
     */
    private void searchcHelper(String noteFrontText, String noteBackText) {
        //Select Search item
        onView(allOf(withId(R.id.action_search),
                withClassName(endsWith("ActionMenuItemView")),
                withContentDescription("Search")))
                .perform(click());

        //Search for note according to the Front and Back texts
        onView(allOf(withId(R.id.action_search),
                withClassName(endsWith("SearchView"))))
                .perform(replaceTextAndSubmitSearchView(noteFrontText + " " + noteBackText));

        //Clear the SearchView text
        onView(allOf(withId(R.id.search_close_btn),
                withContentDescription("Clear query"),
                withClassName(endsWith("AppCompatImageView")),
                isDescendantOfA(withId(R.id.search_plate))))
                .perform(click(), closeSoftKeyboard());
    }

    /*
     * Executes steps to change the deck from the CardBrowser activity.
     */
    private void changeDeckHelper(String deckNameToChangeTo) {
        //Open overflow menu
        onView(allOf(withClassName(endsWith("OverflowMenuButton")),
                isDescendantOfA(withClassName(endsWith("ActionMenuView"))),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .perform(click());

        //Select the "Change deck" (R.string.card_browser_change_deck) option
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                withId(R.id.title),
                withText(R.string.card_browser_change_deck)))
                .perform(click());

        //Select the DECK_MOVE_TO deck
        onView(allOf(withClassName(endsWith("AppCompatCheckedTextView")),
                withId(R.id.dropdown_deck_name),
                withText(deckNameToChangeTo)))
                .perform(click());
    }

    @Test
    public void noteChangeDeckTest() {
        //Navigate to NoteEditor activity
        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.add_note_action)).perform(click());

        onView(isRoot()).perform(closeSoftKeyboard());

        createNote(FRONT_NOTE_STR, BACK_NOTE_STR, "Default");

        navigateFromNoteEditorToCardBrowser();

        //Change the Toolbar deck spinner to "All decks"
        setupCardBrowserToolbarSpinner("All decks");

        //Search for the note
        searchcHelper(FRONT_NOTE_STR, BACK_NOTE_STR);

        //Long click the note containing FRONT_NOTE_STR text
        onView(withText(FRONT_NOTE_STR)).perform(longClick());

        //Change deck
        changeDeckHelper(DECK_MOVE_TO);

        //Select the note
        onView(withText(FRONT_NOTE_STR)).perform(click());

        //Check its "Deck:" spinner value to match
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_deck_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString(DECK_MOVE_TO))));
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