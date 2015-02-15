package fr.erictruong.materialedittext.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AboutActivity extends ActionBarActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();

    @InjectView(R.id.about_tv_version) TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }
        initVersion();
    }

    private void initVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(pInfo.versionName);
            return;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cannot get package info", e);
        }
    }

    @OnClick(R.id.about_btn_github)
    protected void githubClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Pombo/MaterialEditText/"));
        startActivity(intent);
    }

    @OnClick(R.id.about_btn_licenses)
    protected void licensesClicked() {
        Intent intent = new Intent(this, LicensesActivity.class);
        startActivity(intent);
    }
}
