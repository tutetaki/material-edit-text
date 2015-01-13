package com.pombo.materialedittext.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MaterialEditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FieldStateActivity extends ActionBarActivity {

    @InjectView(R.id.edt_api)
    EditText edtApi;
    @InjectView(R.id.medt_normal_with_hint_text)
    MaterialEditText medtNormalWithHintText;
    @InjectView(R.id.medt_press)
    MaterialEditText medtPress;
    @InjectView(R.id.medt_focus)
    MaterialEditText medtFocus;
    @InjectView(R.id.medt_normal_with_input_text)
    MaterialEditText medtNormalWithInputText;
    @InjectView(R.id.medt_error)
    MaterialEditText medtError;
    @InjectView(R.id.medt_disabled)
    MaterialEditText medtDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        ButterKnife.inject(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            medtError.setError("Username or Password is incorrect.");
        }
    }
}
