package fr.erictruong.materialedittext.app;


import android.os.Bundle;
import butterknife.ButterKnife;

public class DeveloperModeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_mode);
        ButterKnife.inject(this);
    }
}
