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


@RunWith(AndroidJUnit4.class)
@LargeTest

public class NoteTagsTest {

    @Rule
    public ActivityScenarioRule<DeckPicker> deckPickerActivityForTagScenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private Resources activityRes;

    private String FRONT_TAG_TEST_NICE_STR = "Tag test note front";
    private String BACK_TAG_TEST_NICE_STR = "Tag test note back";
    private String TAG_NAME;

    @Before
    public void setUp() {
        deckPickerActivityForTagScenarioRule.getScenario()
                .onActivity(new ActivityScenario.ActivityAction<DeckPicker>() {

                    @Override
                    public void perform(DeckPicker activity) {
                        activityRes = activity.getResources();
                    }
                });

        TAG_NAME = "" + (new Random().nextInt(10000000));

        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.add_note_action)).perform(click());

        onView(isRoot()).perform(closeSoftKeyboard());
    }

    private void tagDialogDidShowHelper(){
        //Check Tag dialog toolbar shows
        onView(withId(R.id.tags_dialog_toolbar)).check(matches(isDisplayed()));

        //Check Tag dialog TextView "Tags" shows
        onView(allOf(withClassName(endsWith("TextView")),
                withText(R.string.card_details_tags)))
                .check(matches(isDisplayed()));

        //Check Tag dialog Search item shows
        onView(withId(R.id.tags_dialog_action_filter)).check(matches(isDisplayed()));

        //Check Tag dialog Add item shows
        onView(withId(R.id.tags_dialog_action_add)).check(matches(isDisplayed()));

        //Check Tag dialog Select All item shows
        onView(withId(R.id.tags_dialog_action_select_all)).check(matches(isDisplayed()));

        //Check Cancel button shows
        onView(allOf(withId(R.id.md_buttonDefaultNegative),
                withText(R.string.dialog_cancel)))
                .check(matches(isDisplayed()));

        //Check OK button shows
        onView(allOf(withId(R.id.md_buttonDefaultPositive),
                withText(R.string.dialog_ok)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tagsDialogClickTest() {
        //Click on Tags to open dialog
        onView(allOf(withId(R.id.CardEditorTagButton),
                hasDescendant(withId(R.id.CardEditorTagText))))
                .perform(scrollTo(), click());

        tagDialogDidShowHelper();
    }

    /*
     * Helper method to set up the necessary "Add note" fields since Tags need to be
     * created along with a note, or else it's not created.
     */
    private void createTagHelper(String noteTypeStr, String noteDeckStr,
                                 String noteFrontStr, String noteBackStr) {
        //Set the Type Spinner to noteTypeStr and verify
        onView(withId(R.id.note_type_spinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is(noteTypeStr)))
                .perform(click());
        onView(allOf(withClassName(endsWith("AppCompatTextView")),
                isDescendantOfA(withId(R.id.note_type_spinner))))
                .perform(scrollTo())
                .check(matches(withText(containsString(noteTypeStr))));

        //Set Deck to noteDeckStr and verify
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

    private void addTagDialogDidShowHelper() {
        //Check for Material dialog title with text R.string.add_tag
        onView(allOf(withClassName(endsWith("TextView")),
                withText(R.string.add_tag),
                withId(R.id.md_title)))
                .check(matches(isDisplayed()));

        //Check for EditText
        onView(allOf(withClassName(endsWith("EditText")),
                withHint(R.string.tag_name)))
                .check(matches(isDisplayed()));

        //Check for Cancel button
        onView(allOf(withClassName(endsWith("MDButton")),
                withId(R.id.md_buttonDefaultNegative),
                withText(R.string.dialog_cancel)))
                .check(matches(isDisplayed()));

        //Check for OK button
        onView(allOf(withClassName(endsWith("MDButton")),
                withId(R.id.md_buttonDefaultPositive),
                withText(R.string.dialog_ok)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void addTagDialogDidShowTest() {
        //Click on the "Tags:" bar
        onView(allOf(withId(R.id.CardEditorTagButton),
                hasDescendant(withId(R.id.CardEditorTagText))))
                .perform(scrollTo(), click());

        //Click on the add button
        onView(withId(R.id.tags_dialog_action_add)).perform(click());

        //Check the dialog loads its views
        addTagDialogDidShowHelper();
    }

    private void checkForTagInBrowser(String tagName) {

    }

    @Test
    public void createNewTagTest() {
        //Fill out fields to create note
        createTagHelper("Basic", "Default",
                FRONT_TAG_TEST_NICE_STR, BACK_TAG_TEST_NICE_STR);

        //Click on the "Tags:" bar
        onView(allOf(withId(R.id.CardEditorTagButton),
                hasDescendant(withId(R.id.CardEditorTagText))))
                .perform(scrollTo(), click());

        //Ensure Tag dialog shows properly
        tagDialogDidShowHelper();

        //Click on the add button
        onView(withId(R.id.tags_dialog_action_add)).perform(click());

        //Check "Add tag" (R.string.add_tag) dialog appears
        addTagDialogDidShowHelper();

        //Enter tag name into Add tag EditText
        onView(allOf(withClassName(endsWith("EditText")),
                withHint(R.string.tag_name)))
                .perform(replaceText(TAG_NAME), closeSoftKeyboard())
                .check(matches(withText(TAG_NAME)));

        //Create string to match with coming snackbar message
        String snackbarStr = activityRes.getString(
                R.string.tag_editor_add_feedback, TAG_NAME,
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

        //Check tag is saved by clicking Tags: bar again and searching
        onView(allOf(withId(R.id.CardEditorTagButton),
                hasDescendant(withId(R.id.CardEditorTagText))))
                .perform(scrollTo(), click());

        //Click search icon
        onView(allOf(withId(R.id.tags_dialog_action_filter),
                withContentDescription(R.string.filter_tags)))
                .perform(click());

        //Search for TAG_NAME
        onView(allOf(withId(R.id.tags_dialog_action_filter),
                withClassName(endsWith("SearchView"))))
                .perform(replaceTextAndSubmitSearchView(TAG_NAME));

        //Check new tag named TAG_NAME is there
        onView(allOf(withId(R.id.tags_dialog_tag_item),
                withText(TAG_NAME)))
                .check(matches(isDisplayed()));
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