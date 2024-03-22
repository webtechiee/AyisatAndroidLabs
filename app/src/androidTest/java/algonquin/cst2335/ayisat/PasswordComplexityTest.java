package algonquin.cst2335.ayisat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PasswordComplexityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Test case for a password missing a digit.
     * The TextView should display "You shall not pass!".
     */
    @Test
    public void testPasswordMissingDigit() {
        // Find EditText and type password without a digit
        ViewInteraction appCompatEditText = onView(withId(R.id.editTextText2));
        appCompatEditText.perform(replaceText("PasswordNoDigit"));

        // Find and click the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        // Find TextView and check the text
        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for a password missing an uppercase letter.
     * The TextView should display "You shall not pass!".
     */
    @Test
    public void testPasswordMissingUpperCase() {
        // Find EditText and type password without an uppercase letter
        ViewInteraction editText = onView(withId(R.id.editTextText2));
        editText.perform(replaceText("password no uppercase1"));

        // Find and click the button
        ViewInteraction button = onView(withId(R.id.button));
        button.perform(click());

        // Find TextView and check the text
        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for a password missing a lowercase letter.
     * The TextView should display "You shall not pass!".
     */
    @Test
    public void testPasswordMissingLowerCase() {
        // Find EditText and type password without a lowercase letter
        ViewInteraction editText = onView(withId(R.id.editTextText2));
        editText.perform(replaceText("PASSWORDNOLOWERCASE1"));

        // Find and click the button
        ViewInteraction button = onView(withId(R.id.button));
        button.perform(click());

        // Find TextView and check the text
        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for a password missing a special character.
     * The TextView should display "You shall not pass!".
     */
    @Test
    public void testPasswordMissingSpecialCharacter() {
        // Find EditText and type password without a special character
        ViewInteraction editText = onView(withId(R.id.editTextText2));
        editText.perform(replaceText("PasswordNoSpecial1"));

        // Find and click the button
        ViewInteraction button = onView(withId(R.id.button));
        button.perform(click());

        // Find TextView and check the text
        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case for a password that meets all complexity requirements.
     * The TextView should display "Your password is complex enough".
     */
    @Test
    public void testPasswordComplexEnough() {
        // Find EditText and type password with all requirements
        ViewInteraction editText = onView(withId(R.id.editTextText2));
        editText.perform(replaceText("ComplexPassword1!"));

        // Find and click the button
        ViewInteraction button = onView(withId(R.id.button));
        button.perform(click());

        // Find TextView and check the text
        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("Your password meets the requirements")));
    }
}
