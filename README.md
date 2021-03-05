# Project Submission

## DeckCreatorTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity.
	 - Used the @Before annotation to retrieve the activity's DecorView for later use.
	 - Used the @Test annotation to create my testing methods.

 - I thought it was important to test this feature since it is one of the central ones for a flash card app such as this one. A note can automatically go to the "Default" deck, but it's more useful (and most likely to be used) by the user to create decks of certain subjects.
 - Corner cases
	 - I tested for the creation of a reasonably named deck, a deck with strange characters, a deck with a long name, a deck with no name, and cancellation of a deck creation.
 - I got stuck trying to figure out how to check for a Toast message. However, I found it was located elsewhere in the hierarchy. I was also stuck when trying to find the newly created decks to verify the creation. Luckily, for RecyclerViews, there is an action (scrollTo) which was helpful in scrolling to a deck by name and then verifying it was in view.
 - ViewMatchers used
 	 - withID
 	 - hasDescendantOfA
 	 - isDisplayed
 	 - withClassName
 	 - withText

 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - replaceText
 	 - typeText

 - ViewAssertions used
 	 - doesNotExist
 	 - matches
 - Tested Views
 	 - FloatingActionButton to create a deck.
 	 - MDRootLayout for a dialog
 	 - MaterialDialogButton for selecting Cancel and OK
 	 - RecyclerView since this is where the decks are listed.
 	 - Toast when verifying an invalid deck.
 	 - EditText to enter deck name.

## DeleteDeckTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
 - I thought it was important to test this feature since it goes together with creating a deck, thus completing the deck's cycle. A user would most likely find deleting decks useful.
 - Corner cases
	 - I tested to make sure the wrong deck wasn't removed.
  -  I was stuck when trying to find the deck to delete once the list was out of view. Luckily, for RecyclerViews, there is an action (scrollTo) which was helpful in scrolling to a deck by name and then verifying it was in view.
 - ViewMatchers used
 	 - isDescendentOfA
 	 - withClassName
 	 - withId
 	 - withText
 	 - hasDescendant
 	 - isDisplayed
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - longClick
 	 - replaceText
 - ViewAssertions used
 	 - doesNotExist
 	 - matches

 - Tested Views
 	 - FloatingActionButton to create deck.
 	 - EditText to create the decks to delete and stay.
 	 - RecyclerView to scroll to the deck to be deleted.
 	 - FixedTextView to check if displayed and click open deck options dialog.
 	 - AppCompatTextView to click on the "Delete Deck" option.

## CreateNoteTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
	 - Used the @Before annotation to navigate, as the user would, to the NoteEditor activity and to retrieve the activity's DecorView and Resources for later use.
 - I thought it was important to test this feature since notes are a main feature of the app.
 - Corner cases
	 - I tested for the creation of a reasonably made card (front and back note), a note with strange characters, a note with a long name, and a note with no front text (invalid).
 - I got stuck with the spinners, but was able to correctly set them with the proper usage of onData.
 - ViewMatchers used
 	 - isDescendentOfA
 	 - isDisplayed
 	 - isRoot
 	 - withClassName
 	 - withContentDescription
 	 - withId
 	 - withText
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - replaceText
 	 - scrollTo
 - ViewAssertions used
 	 - matches

 - Tested Views
 	 - FloatingActionButton to go to new activity.
 	 - LinearLayout and AppCompatTextView to ensure view loaded.
 	 - Spinners to set the note type and deck type. AppCompatTextView to verify the Spinners changed.
 	 - EditText to set the text for both sides of a note. FieldEditLine to confirm I was editing the correct EditText. Also tested to see the front and back EditTexts were cleared after making a note.
 	 - Toast to verify the note created message.

## NoteChangeDeckTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class.
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
	 - Used the @Before annotation to get the activity Resources for later use and create a deck.
 - I thought it was important to test this feature since notes are a main feature of the app. Since notes are an important piece of this app, I wanted to test different features relating to them, like changing their deck. I wanted to test whatever a user would find helpful or useful.
 - Initially, I wasn't sure how to find the notes best before opting for a combination of making a destination deck and using the search functionality in the card browser. Was also a bit hard to follow the code before grouping some code which logically made sense together (such as combining all the steps for executing a deck change or the steps for searching).
 - ViewMatchers used
 	 - isDescendentOfA
 	 - isRoot
 	 - withClassName
 	 - withContentDescription
 	 - withId
 	 - withText
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - longClick
 	 - replaceText
 	 - scrollTo
 - ViewAssertions used
 	 - matches

 - TestedViews
 	 - FloatingActionBar to create a deck and note.
 	 - EditText to name the deck and fill in the front and back of the note.
 	 -   FieldEditLine to ensure I selected the correct EditField.
 	 - Spinner to set the card type and deck. AppCompatTextView to verify Spinner changes.
 	 - AppCompatImageButton to navigate around the activities.
 	 - Toolbar to verify the correct selection of other Views (e.g. buttons).
 	 - FixedTextView to verity Spinner changes.
 	 - ActionMenuItemView and SearchView to trigger a search.
 	 - AppCompatImageView to close a search.
 	 - OverflowMenuButton to open the overflow menu.
 	 - AppcompatTextView again to select the option to change the deck.
 	 - AppCompatCheckedTextView to select the deck to change to.

## NoteDeleteTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class.
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
	 - Used the @Before annotation to get the activity Resources for later use, create a deck, and navigate to NoteEditor.
 - Along with the creation of the note, deletion seems like a useful thing to users. My goal throughout the project was to test the things a user would use more.
 - I tested cases for single, double, and multiple note deletion to test potential issues depending on number selected/deleted.
 - I was stuck in deleting multiple notes because I wasn't sure how I could find and select the notes I created to test for deletion. I opted for creating a deck and placing the notes destined for deletion to be in the same deck to make them easier to find together.
 - ViewMatchers used
 	 - isDescendentOfA
 	 - isDisplayed
 	 - isRoot
 	 - withClassName
 	 - withContentDescription
 	 - withId
 	 - withText
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - longClick
 	 - replaceText
 	 - scrollTo
 - ViewAssertions used
 	 - doesNotExist
 	 - matches

 - TestedViews
 	 - FloatingActionBar to navigate to NoteEditor and create.
 	 - EditText to name deck and fill out front and back note fields.
 	 - FieldEditLine to verify the correct choice of EditText
 	 - Spinner to set the note type.
 	 - AppCompatTextView to veirfy the Spinner change.
 	 - AppCompatImageButton to navigate through the app activities.
 	 - AppCompatSpinner to filter the deck and Toolbar view to verify Spinner.
 	 - FixedTextView to match the Spinner with the deck meant to be selected.
 	 - ActionMenuItemView and SearchView for searching for note.
 	 - AppCompatImageView to clear the search.
 	 - OverflowMenuButton to get to the "Delete Notes" option and ActionMenuView and Toolbar to ensure correct button click.
 	 - AppCompatTextView to select the "Delete Notes" option
 	 - AppCompatButton to "Undo" a deletion.

## DeckRenameTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class.
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
 - Another feature test chosen because it seems like an important piece of the app's usefulness. In between creating and deleting a deck, the need for renaming must definitely come up for a user.
 - I was stuck, like in the other deck parts, with finding the deck in a list. Implemented a RecyclerViewAction.scrollTo ViewAction to easily help me identify the deck I was searching for.
 - ViewMatchers used
 	 - hasDescendant
 	 - isDescendantOfA
 	 - isDisplayed()
 	 - withClassName
 	 - withId
 	 - withText
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - longClick
 - ViewAssertions used
 	 - doesNotExist
 	 - matches

 - TestedViews
 	 - FloatingActionBar to navigate to create a deck.
 	 - EditText to rename the deck.
 	 - RecyclerView to scrollTo the deck I was looking to rename
 	 - FixedTextView to bring up the deck's options after a long click.
 	 - AppCompatTextView to select the "Rename deck" option.
 	 - FixedEditText to replace the current deck name with the new name.
 	 - MDButton to click the rename button.
 	 - FixedTextView to verify the change of name and verify deck with old name is no longer (somehow) there.

## NoteTagAddTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class.
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
	 - Used the @Before annotation to set up the activity's Resources for later,  navigate to NoteEditor, and close the keyboard that automatically pops up on the activity.
 - I thought writing a test for this feature was important because it is a nice, extra way to categorize a note. It's also more versatile than organizing by deck since notes can have multiple tags.
 - Corner cases
	 - Tested creating tags with simple text of numbers, random characters, long text, as well as attempting to make one with whitespace.
 - I had some trouble when creating and trying to verify a tag because the app automatically deletes the text in the field when there's a whitespace. Once I found it out, I edited my test strings to not have whitespace so I could correctly verify.
 - ViewMatchers used
 	 - hasDescendant
 	 - isDescendantOfA
 	 - isDisplayed()
 	 - withClassName
 	 - withContentDescription
 	 - withHint
 	 - withId
 	 - withText
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - replaceText
 	 - scrollTo
 - ViewAssertions used
 	 - doesNotExist
 	 - matches

 - TestedViews
 	 - FloatingActionBar to navigate to NoteEditor
 	 - Toolbar, TextView, MenuItems, and MDButtons to verify a dialog.
 	 - LinearLayout to open up the tags dialog.
 	 - Spinners to set the note type and deck and AppCompatTextViews to verify the selected options.
 	 - EditText to set the front and back note texts with FieldEditLine to verify the distinction between the two.
 	 - TextView, EditText, and MDButtons to verify another dialog.
 	 - MenuItems to open up a dialog.
 	 - EditText to enter the tag name.
 	 - MDButton to OK the entered tag name and another MDButton to OK the tag selection in the "Tags" dialog.
 	 - MenuItem to save the note (along with the tag).
 	 - MenuItem to open the SearchView to search for the new tag.
 	 - CheckedTextView to verify tag remains (was saved).

## NoteTagSearchTest

 - Technology Utilization
	 - I utilized the @RunWith(AndroidJunit4.class) and @LargeTest to use the test class.
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
	 - Used the @Before annotation to set up the activity's Resources for later use,  navigate to NoteEditor, and close the keyboard that automatically pops up on the activity.
 - While tags are incredibly useful, they become hectic to manage or find once they grow — and the number of tags can grow quickly because of the versatility they add to the notes. This is why I believed it would be important to test out the search function of tags. This class is one part of it testing the search while in NoteEditor; the other part is in another class which tests searching for tags in the CardBrowser.
 - Corner Cases
	 - Like with adding, I test searching for tags of random characters, a long tag, and an empty tag to try and see if anything goes wrong.
 - I didn't have much trouble with this since I had done many of the same steps when testing the add tag feature.
 - ViewMatchers used
 	 - hasDescendant
 	 - isDisplayed()
 	 - isRoot
 	 - withClassName
 	 - withContentDescription
 	 - withHint
 	 - withId
 	 - withText
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - replaceText
 	 - scrollTo
 - ViewAssertions used
 	 - doesNotExist
 	 - matches

 - TestedViews
 	 - FloatingActionBar to navigate to NoteEditor.
 	 - LinearLayout to open up the tags dialog.
 	 - MenuItem to open the dialog to enter tag name.
 	 - EditText to set the tag name.
 	 - MDButton to confirm tag name and go back to "Tags" dialog.
 	 - MenuItem to open the SearchView to search for the new tag.
 	 - CheckedTextView to verify tag remains (was saved).
 	 - SearchView again to verify adding a blank String changed nothing.

## CardBrowserTagSearchTest

 - Technology Utilization
	 - Within the class, I used @Rule to create the ActivityScenarioRule so it could launch and close the activity. 
	 - Used the @Test annotation to create my testing methods.
	 - Used the @Before annotation to set up the activity's Resources for later use.
 - This class is the second part of testing the search of tags, but, this time, in the CardBrowser. This is important due to the size of the list tags can make, thereby makint it harder to find and select the ones you want.
 - Corner Cases
	 - I tested searching for tags of random characters and a long tag to  try and see if anything goes wrong since it would be hard to believe a user would opt for a tag like these.
 - This one posed some trouble with navigating since there were a lot of dialogs, menus, and items to select and go through. Made things slow to figure out, but looking at the hierarchy and layout files helped out.
 - ViewMatchers used
 	 - hasDescendant
 	 - isDescendantOfA
 	 - isDisplayed
 	 - isRoot
 	 - withClassName
 	 - withContentDescription
 	 - withHint
 	 - withId
 	 - withText
 - ViewActions used
 	 - click
 	 - closeSoftKeyboard
 	 - replaceText
 	 - scrollTo
 - ViewAssertions used
 	 - matches

 - TestedViews
 	 - FloatingActionBar to navigate to NoteEditor.
 	 - AppCompatImageButton to help navigate to the CardBrowser activity with Toolbar to verify the correct Button
 	 - Spinner to set the card type and deck name of the dummy note created (since new tags can't be made without one)
 	 - EditText views to set the text for the front and back of the note along with FieldEditLine to verify the correct EditText view is selected.
 	 - LinearLayout to show "Tags" dialog and MenuItem to bring up the dialog to enter the tag name.
 	 - EditText to enter the tag name and verify entered String matches what's shown.
 	 - MDButton views to OK the tag name and then OK out of the "Tags" dialog to attach tag to the note.
 	 - MenuItem to save the note (and the tag along with it).
 	 - AppCompatImageButton to navigate up to DeckPicker with a Toolbar to verify it's the correct button.
 	 - OverflowMenuButton to open menu options with an ActionMenuView and Toolbar to ensure it's the correct Button.
 	 - AppCompatTextView to show the tags dialog.
 	 - ActionMenuItemView to open the SearchView which is then used to enter tag for searching and filtering the list.
 	 - CheckedTextView to verify the tag shows.
