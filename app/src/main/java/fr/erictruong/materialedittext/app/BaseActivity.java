package fr.erictruong.materialedittext.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends ActionBarActivity {

    public static final int THEME_DARK = 0;
    public static final int THEME_LIGHT = 1;

    protected int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = getIntent().getIntExtra("theme", -1);
        if (theme == THEME_LIGHT) {
            setTheme(R.style.AppTheme_Light_DarkActionBar);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_switch_theme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_switch_theme) {
            finish();
            Intent intent = new Intent(this, getClass());
            intent.putExtra("theme", theme == THEME_LIGHT ? THEME_DARK : THEME_LIGHT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean isThemeDark() {
        return theme == THEME_DARK;
    }

    protected boolean isThemeLight() {
        return theme == THEME_LIGHT;
    }
}
