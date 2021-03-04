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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteTagSearchTest {

    private Resources activityRes;

    @Rule
    public ActivityScenarioRule<DeckPicker> scenarioRule
            = new ActivityScenarioRule<>(DeckPicker.class);

    private final String SEARCH_TAG_NAME = "NoteTagSearchTest" +
            (new Random().nextInt(10000000));
    private final String SEARCH_TAG_NAME_GIBBERISH = "NoteTagSearchTest" + "+„87ba£§!§©π≤ch]ˇht]";
    private final String SEARCH_TAG_NAME_LONG = "NoteTagSearchTest" + "abcdefghijklmnopqrstuvwxyz"
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

        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.add_note_action)).perform(click());

        onView(isRoot()).perform(closeSoftKeyboard());
    }

    //Helper method to prepare tag adding process to search
    private void addTagHelper(String tagName) {
        //Click on the "Tags:" bar
        onView(allOf(withId(R.id.CardEditorTagButton),
                hasDescendant(withId(R.id.CardEditorTagText))))
                .perform(scrollTo(), click());

        //Click on the add button to bring up dialog
        onView(withId(R.id.tags_dialog_action_add)).perform(click());

        //Enter the tag name into Add tag EditText
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
    }

    //Helper method to perform the search
    public void tagSearchHelper(String tagName) {
        //Click the search button
        onView(allOf(withId(R.id.tags_dialog_action_filter),
                withContentDescription(R.string.filter_tags)))
                .perform(click());

        //Check the bar changes view
        onView(allOf(withId(R.id.search_src_text),
                withHint(R.string.add_new_filter_tags)))
                .check(matches(isDisplayed()));

        //Type into the SearchView
        onView(allOf(withClassName(endsWith("SearchView")),
                withId(R.id.tags_dialog_action_filter)))
                .perform(replaceTextAndSubmitSearchView(tagName));

        //Check the tag shows
        onView(allOf(withClassName(endsWith("CheckedTextView")),
                withId(R.id.tags_dialog_tag_item),
                withText(tagName)))
                .check(matches(isDisplayed()));
    }

    private void addTagAndSearchHelper(String tagName) {
        addTagHelper(tagName);
        tagSearchHelper(tagName);
    }

    @Test
    public void tagSearchNormalTest() {
        addTagAndSearchHelper(SEARCH_TAG_NAME);
    }

    @Test
    public void tagSearchGibberishTest() {
        addTagAndSearchHelper(SEARCH_TAG_NAME_GIBBERISH);
    }

    @Test
    public void tagSearchLongTest() {
        addTagAndSearchHelper(SEARCH_TAG_NAME_LONG);
    }

    @Test
    public void tagSearchEmptyTest() {
        String emptyStr = " ";

        //Click on the "Tags:" bar
        onView(allOf(withId(R.id.CardEditorTagButton),
                hasDescendant(withId(R.id.CardEditorTagText))))
                .perform(scrollTo(), click());

        //Click on the add button to bring up dialog
        onView(withId(R.id.tags_dialog_action_add)).perform(click());

        //Enter the empty string into Add tag EditText
        onView(allOf(withClassName(endsWith("EditText")),
                withHint(R.string.tag_name)))
                .perform(replaceText(emptyStr), closeSoftKeyboard())
                .check(matches(withText("")));

        //Click the OK button
        onView(allOf(withClassName(endsWith("MDButton")),
                withId(R.id.md_buttonDefaultPositive),
                withText(R.string.dialog_ok)))
                .perform(click());

        //Check for no snackbar message since blank text is ignored
        onView(withId(R.id.snackbar_text))
                .check(doesNotExist());

        //Click the search button
        onView(allOf(withId(R.id.tags_dialog_action_filter),
                withContentDescription(R.string.filter_tags)))
                .perform(click());

        //Type into the SearchView
        onView(allOf(withClassName(endsWith("SearchView")),
                withId(R.id.tags_dialog_action_filter)))
                .perform(replaceTextAndSubmitSearchView(" "));

        //Check SearchView shows "Add/filter" (R.string.add_new_filter_tags)
        onView(allOf(withId(R.id.search_src_text),
                withHint(R.string.add_new_filter_tags)))
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