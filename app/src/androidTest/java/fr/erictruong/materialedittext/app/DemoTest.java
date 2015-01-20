package fr.erictruong.materialedittext.app;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.widget.EditText;
import com.robotium.solo.Solo;

public class DemoTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public DemoTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testThemeDark() {
        Intent intent = new Intent();
        intent.putExtra("theme", 1);
        setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());

        demo();
    }

    public void testDemoLight() {
        Intent intent = new Intent();
        intent.putExtra("theme", 0);
        setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());

        demo();
    }

    private void demo() {
        int sleepDuration = 400;

        solo.clickOnView(solo.getView(R.id.edt_api));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_hint_text));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_press));
        solo.sleep(sleepDuration);

        EditText medtFocus = (EditText) solo.getView(R.id.medt_focus);
        solo.clickOnView(medtFocus);
        solo.sleep(sleepDuration);
        while (!TextUtils.isEmpty(medtFocus.getText())) {
            solo.sendKey(Solo.DELETE);
        }
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_input_text));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_error));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_disabled));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_hint_text_icon));
        solo.sleep(sleepDuration);

        EditText medtFocusIcon = (EditText) solo.getView(R.id.medt_focus_icon);
        solo.clickOnView(medtFocusIcon);
        solo.sleep(sleepDuration);
        while (!TextUtils.isEmpty(medtFocusIcon.getText())) {
            solo.sendKey(Solo.DELETE);
        }
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_input_text_icon));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_hint_text_label));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_focus_label));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_hint_text_label));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_input_text_label));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_disabled_label));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_hint_text_multi));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_focus_multi));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_input_text_multi));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_normal_with_input_text_label_multi));
        solo.sleep(sleepDuration);

        final EditText medtCharacterCounter = (EditText) solo.getView(R.id.medt_character_counter);
        solo.clickOnView(medtCharacterCounter);
        solo.sleep(sleepDuration);
        solo.scrollViewToSide(medtCharacterCounter, Solo.RIGHT);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                medtCharacterCounter.setSelection(medtCharacterCounter.length());
            }
        });
        solo.sleep(sleepDuration);
        for (int i = 0; i < 10; i++) {
            solo.typeText(medtCharacterCounter, "a");
        }
        solo.sleep(sleepDuration);
        for (int i = 0; i < 10; i++) {
            solo.sendKey(Solo.DELETE);
        }
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_character_counter_exceeding));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_character_counter_multi));
        solo.sleep(sleepDuration);

        solo.clickOnView(solo.getView(R.id.medt_character_counter_exceeding_multi));
        solo.sleep(sleepDuration);
    }
}
