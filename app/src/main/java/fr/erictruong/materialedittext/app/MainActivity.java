package fr.erictruong.materialedittext.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.MaterialEditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.main_edt_error) MaterialEditText medtError;
    @InjectView(R.id.img_1) ImageView img1;
    @InjectView(R.id.img_2) ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (isThemeLight()) {
            img1.setImageResource(R.drawable.ic_phone_grey600_24dp);
            img2.setImageResource(R.drawable.ic_phone_grey600_24dp);
        } else {
            img1.setImageResource(R.drawable.ic_phone_white_24dp);
            img2.setImageResource(R.drawable.ic_phone_white_24dp);
        }

        if (savedInstanceState == null) {
            medtError.setError("Username or Password is incorrect.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_developer_mode) {
            startActivity(new Intent(this, DeveloperModeActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
