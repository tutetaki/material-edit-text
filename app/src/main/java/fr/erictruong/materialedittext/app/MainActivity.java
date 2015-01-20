package fr.erictruong.materialedittext.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MaterialEditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {

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
    @InjectView(R.id.img_1)
    ImageView img1;
    @InjectView(R.id.img_2)
    ImageView img2;
    @InjectView(R.id.img_3)
    ImageView img3;

    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = getIntent().getIntExtra("theme", -1);

        if (theme == 1) {
            setTheme(R.style.AppTheme_Light_DarkActionBar);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (theme == 1) {
            img1.setImageResource(R.drawable.ic_phone_grey600_24dp);
            img2.setImageResource(R.drawable.ic_phone_grey600_24dp);
            img3.setImageResource(R.drawable.ic_phone_grey600_24dp);
        } else {
            img1.setImageResource(R.drawable.ic_phone_white_24dp);
            img2.setImageResource(R.drawable.ic_phone_white_24dp);
            img3.setImageResource(R.drawable.ic_phone_white_24dp);
        }

        if (savedInstanceState == null) {
            medtError.setError("Username or Password is incorrect.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_switch_theme) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("theme", theme == 1 ? 0 : 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
