package com.sxmp.apprentice;

import android.content.res.Resources;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.ichi2.anki.DeckPicker;
import com.ichi2.anki.R;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


public class CardBrowserTagSearchTest {

    @Rule
    public ActivityScenarioRule<DeckPicker> scenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private Resources activityRes;

    private final String FRONT_TAG_TEST_NICE_STR = "CardBrowserTagSearch front";
    private final String BACK_TAG_TEST_NICE_STR = "CardBrowserTagSearch back";

    private final String SEARCH_TAG_NAME = "CardBrowserTagSearchTest" +
            (new Random().nextInt(10000000));
    private final String SEARCH_TAG_NAME_GIBBERISH = "CardBrowserTagSearchTest" +
            "+„87ba£§!§©π≤ch]ˇht]";
    private final String SEARCH_TAG_NAME_LONG = "CardBrowserTagSearchTest" + "defghijklmnopqrstuvw"
            + "1234567890ÏwÅˆ0Ç˚=»˜˘F-`J?åzyABCDEFGHIJKLMNOPQRSTUVWXYZÏwÅˆ0Ç˚=»˜˘F-`J?åzy" +
            "ÏwÅˆ0Ç˚=»˜˘F-`J?åzyabcdefghijklmnopqrstuvwxyz1234567890ÏwÅˆ0Ç˚=»˜˘F-`J?åzy" +
            "1234567890ÏwÅˆ0Ç˚=»˜˘F-`J?åzyABCDEFGHIJKLMNOPQRSTUVWXYZÏwÅˆ0Ç˚=»˜˘F-`J?åzy" +
            "ÏwÅˆ0Ç˚=»˜˘F-`J?åzyabcdefghijklmnopqrstuvwxyz1234567890ÏwÅˆ0Ç˚=»˜˘F-`J?åzy" +
            "1234567890ÏwÅˆ0Ç˚=»˜˘F-`J?åzyABCDEFGHIJKLMNOPQRSTUVWXYZÏwÅˆ0Ç˚=»˜˘F-`J?åzy" +
            "ÏwÅˆ0Ç˚=»˜˘F-`J?åzyabcdefghijklmnopqrstuvwxyz1234567890ÏwÅˆ0Ç˚=»˜˘F-`J?åzy";

    @Before
    public void setUp() {
        scenarioRule.getScenario()
                .onActivity(new ActivityScenario.ActivityAction<DeckPicker>() {

                    @Override
                    public void perform(DeckPicker activity) {
                        activityRes = activity.getResources();
                    }
                });
    }

    private void navigateToNoteEditor() {
        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.add_note_action)).perform(click());

        onView(isRoot()).perform(closeSoftKeyboard());
    }

    private void navigateToCardBrowswer() {
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
     * Helper method to set up the necessary "Add note" fields since Tags need to be
     * created along with a note, or else it's not saved.
     */
    private void setUpNote(String noteTypeStr, String noteDeckStr,
                           String noteFrontStr, String noteBackStr) {
        //Set the Type Spinner to noteTypeStr and verify
        onView(withId(R.id.note_type_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(noteTypeStr)))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_type_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString(noteTypeStr))));

        //Set Deck Spinner to noteDeckStr and verify
        onView(withId(R.id.note_deck_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(noteDeckStr)))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_deck_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString(noteDeckStr))));

        //Enter noteFrontStr for the front of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Front"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(noteFrontStr), closeSoftKeyboard())
                .check(matches(withText(noteFrontStr)));

        //Enter noteBackStr for the back of note
        onView(allOf(withClassName(endsWith("EditText")),
                withContentDescription("Back"),
                isDescendantOfA(withClassName(endsWith("FieldEditLine"))),
                isDescendantOfA(withId(R.id.constraint_layout))))
                .perform(scrollTo(), replaceText(noteBackStr), closeSoftKeyboard())
                .check(matches(withText(noteBackStr)));
    }

    /*
     * Helper method to set the spinners and fill Front and Back edit
     * fields, then create the tag, and save the note to save the tag.
     */
    private void createAndSaveNewTagHelper(String tagName) {
        //Fill out fields to create note
        setUpNote("Basic", "Default",
                FRONT_TAG_TEST_NICE_STR, BACK_TAG_TEST_NICE_STR);

        //Click on the "Tags:" bar
        onView(allOf(withId(R.id.CardEditorTagButton),
                hasDescendant(withId(R.id.CardEditorTagText))))
                .perform(scrollTo(), click());

        //Click on the add button
        onView(withId(R.id.tags_dialog_action_add)).perform(click());

        //Enter tagName into Add tag EditText
        onView(allOf(withClassName(endsWith("EditText")),
                withHint(R.string.tag_name)))
                .perform(replaceText(tagName), closeSoftKeyboard())
                .check(matches(withText(tagName)));

        //Create string to match with coming snackbar message
        String snackbarStr = activityRes.getString(
                R.string.tag_editor_add_feedback, tagName,
                activityRes.getString(R.string.dialog_ok));

        //Click the OK button
        onView(allOf(withClassName(endsWith("MDButton")),
                withId(R.id.md_buttonDefaultPositive),
                withText(R.string.dialog_ok)))
                .perform(click());

        //Check for snackbar message
        onView(allOf(withId(R.id.snackbar_text),
                withText(snackbarStr)))
                .check(matches(isDisplayed()));

        //Click OK on the Tags dialog
        onView(allOf(withId(R.id.md_buttonDefaultPositive),
                withText(R.string.dialog_ok)))
                .perform(click());

        //Save (create) the note to save tag
        onView(withId(R.id.action_save)).perform(click());
    }

    /*
     * Helper method to navigate from NoteEditor to DeckPicker to Cardbrowser
     * to the filter by tag dialog, and search for the newly created tag.
     */
    private void goToCardBrowserAndSearchForTag(String tagName) {
        //Navigate back up to DeckPicker from NoteEditor
        onView(allOf(withClassName(endsWith("AppCompatImageButton")),
                withContentDescription("Navigate up"),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .perform(click());

        navigateToCardBrowswer();

        //Open overflow menu
        onView(allOf(withClassName(endsWith("OverflowMenuButton")),
                isDescendantOfA(withClassName(endsWith("ActionMenuView"))),
                isDescendantOfA(allOf(withClassName(endsWith("Toolbar")),
                        withId(R.id.toolbar)))))
                .perform(click());

        //Click on "Filter by tag" (R.string.card_browser_search_by_tag)
        onView(allOf(withId(R.id.title),
                withClassName(endsWith("AppCompatTextView")),
                withText(R.string.card_browser_search_by_tag)))
                .perform(click());

        //Click on search icon
        onView(allOf(withClassName(endsWith("ActionMenuItemView")),
                withId(R.id.tags_dialog_action_filter),
                withContentDescription(R.string.filter_tags)))
                .perform(click());

        //Search for the tag
        onView(allOf(withClassName(endsWith("SearchView")),
                withId(R.id.tags_dialog_action_filter)))
                .perform(replaceTextAndSubmitSearchView(tagName));

        //Check if tag is there
        onView(allOf(withClassName(endsWith("CheckedTextView")),
                withId(R.id.tags_dialog_tag_item),
                withText(tagName)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void cardBrowserTagSearchNormalTest() {
        navigateToNoteEditor();

        createAndSaveNewTagHelper(SEARCH_TAG_NAME);

        goToCardBrowserAndSearchForTag(SEARCH_TAG_NAME);
    }

    @Test
    public void cardBrowserTagSearchGibberishTest() {
        navigateToNoteEditor();

        createAndSaveNewTagHelper(SEARCH_TAG_NAME_GIBBERISH);

        goToCardBrowserAndSearchForTag(SEARCH_TAG_NAME_GIBBERISH);
    }

    @Test
    public void cardBrowserTagSearchLongTest() {
        navigateToNoteEditor();

        createAndSaveNewTagHelper(SEARCH_TAG_NAME_LONG);

        goToCardBrowserAndSearchForTag(SEARCH_TAG_NAME_LONG);
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