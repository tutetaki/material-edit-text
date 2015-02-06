package fr.erictruong.materialedittext.app;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DemoTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public DemoTest() {
        super(MainActivity.class);
    }

    public void testDemo_darkTheme() throws InterruptedException {
        Intent intent = new Intent();
        intent.putExtra("theme", 1);
        setActivityIntent(intent);
        performDemo(getActivity());
    }

    public void testDemo_lightTheme() throws InterruptedException {
        Intent intent = new Intent();
        intent.putExtra("theme", 0);
        setActivityIntent(intent);
        performDemo(getActivity());
    }

    private void performDemo(Activity activity) throws InterruptedException {
        onView(withId(R.id.main_edt_api)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_normal_with_hint_text)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_normal_with_input_text)).perform(scrollTo(), click());
        final EditText edtNormalWithInputText = (EditText) activity.findViewById(R.id.main_edt_normal_with_input_text);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                edtNormalWithInputText.setSelection(edtNormalWithInputText.length());
            }
        });
        while (!TextUtils.isEmpty(edtNormalWithInputText.getText())) {
            onView(withId(R.id.main_edt_normal_with_input_text)).perform(pressKey(KeyEvent.KEYCODE_DEL));
        }

        onView(withId(R.id.main_edt_error)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_disabled)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_normal_with_hint_text_icon)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_normal_with_input_text_icon)).perform(scrollTo(), click());
        final EditText edtNormalWithInputTextIcon = (EditText) activity.findViewById(R.id.main_edt_normal_with_input_text_icon);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                edtNormalWithInputTextIcon.setSelection(edtNormalWithInputTextIcon.length());
            }
        });
        while (!TextUtils.isEmpty(edtNormalWithInputTextIcon.getText())) {
            onView(withId(R.id.main_edt_normal_with_input_text_icon)).perform(pressKey(KeyEvent.KEYCODE_DEL));
        }

        onView(withId(R.id.main_edt_normal_with_hint_text_label)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_normal_with_input_text_label)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_disabled_label)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_normal_with_input_text_multi)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_normal_with_input_text_label_multi)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_character_counter)).perform(scrollTo(), swipeRight());
        final EditText edtCharacterCounter = (EditText) activity.findViewById(R.id.main_edt_character_counter);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                edtCharacterCounter.setSelection(edtCharacterCounter.length());
            }
        });
        String stringToBeTyped = "I am a loooooong text";
        onView(withId(R.id.main_edt_character_counter)).perform(typeTextIntoFocusedView(stringToBeTyped));
        for (int i = 0; i < stringToBeTyped.length(); i++) {
            onView(withId(R.id.main_edt_character_counter)).perform(pressKey(KeyEvent.KEYCODE_DEL));
            Thread.sleep(50);
        }

        onView(withId(R.id.main_edt_character_counter_exceeding)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_character_counter_multi)).perform(scrollTo(), click());

        onView(withId(R.id.main_edt_character_counter_exceeding_multi)).perform(scrollTo(), click());
    }
}
