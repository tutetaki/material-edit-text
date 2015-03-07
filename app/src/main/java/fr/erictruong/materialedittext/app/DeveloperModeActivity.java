package fr.erictruong.materialedittext.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.*;
import fr.erictruong.materialedittext.app.preference.AppPrefs;

public class DeveloperModeActivity extends BaseActivity {

    private static final int OFFSET_PROGRESS_TEXT_SIZE = 16;
    private static final int MAX_PROGRESS_TEXT_SIZE = OFFSET_PROGRESS_TEXT_SIZE + 16;
    private static final int MAX_PROGRESS_COLOR = 0xff;
    private static final int MAX_PROGRESS_CHAR_COUNT = 100;

    private static final String TABSPACE = "    ";

    @InjectView(R.id.developer_mode_edt_1) MaterialEditText edt1;

    @InjectView(R.id.developer_mode_seek_text_size) SeekBar seekTextSize;
    @InjectView(R.id.developer_mode_tv_text_size) TextView tvTextSize;

    @InjectView(R.id.developer_mode_cbx_background_color) CheckBox cbxBackgroundColor;
    @InjectView(R.id.developer_mode_seek_background_color_red) SeekBar seekBackgroundColorRed;
    @InjectView(R.id.developer_mode_view_background_color_red) View viewBackgroundColorRed;
    @InjectView(R.id.developer_mode_seek_background_color_green) SeekBar seekBackgroundColorGreen;
    @InjectView(R.id.developer_mode_view_background_color_green) View viewBackgroundColorGreen;
    @InjectView(R.id.developer_mode_seek_background_color_blue) SeekBar seekBackgroundColorBlue;
    @InjectView(R.id.developer_mode_view_background_color_blue) View viewBackgroundColorBlue;

    @InjectView(R.id.developer_mode_cbx_error) CheckBox cbxError;
    @InjectView(R.id.developer_mode_edt_error) MaterialEditText edtError;

    @InjectView(R.id.developer_mode_cbx_error_color) CheckBox cbxErrorColor;
    @InjectView(R.id.developer_mode_seek_error_color_red) SeekBar seekErrorColorRed;
    @InjectView(R.id.developer_mode_view_error_color_red) View viewErrorColorRed;
    @InjectView(R.id.developer_mode_seek_error_color_green) SeekBar seekErrorColorGreen;
    @InjectView(R.id.developer_mode_view_error_color_green) View viewErrorColorGreen;
    @InjectView(R.id.developer_mode_seek_error_color_blue) SeekBar seekErrorColorBlue;
    @InjectView(R.id.developer_mode_view_error_color_blue) View viewErrorColorBlue;

    @InjectView(R.id.developer_mode_cbx_floating_label) CheckBox cbxFloatingLabel;
    @InjectView(R.id.developer_mode_edt_floating_label) MaterialEditText edtFloatingLabel;

    @InjectView(R.id.developer_mode_cbx_char_counter) CheckBox cbxCharCounter;
    @InjectView(R.id.developer_mode_seek_max_characters) SeekBar seekMaxCharacters;
    @InjectView(R.id.developer_mode_tv_max_characters) TextView tvMaxCharacters;

    @InjectView(R.id.developer_mode_edt_result) FullwidthEditText edtResult;

    private int backgroundColorRed;
    private int backgroundColorGreen;
    private int backgroundColorBlue;

    private int errorColorRed;
    private int errorColorGreen;
    private int errorColorBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (theme == THEME_LIGHT) {
            setContentView(R.layout.activity_developer_mode);
        } else {
            setContentView(R.layout.activity_developer_mode_inverse);
        }
        ButterKnife.inject(this);

        seekTextSize.setMax(MAX_PROGRESS_TEXT_SIZE);
        seekTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final int offsetProgress = progress + OFFSET_PROGRESS_TEXT_SIZE;
                AppPrefs.putTextSize(DeveloperModeActivity.this, progress);
                tvTextSize.setText(offsetProgress + "sp");
                edt1.setTextSize(offsetProgress);
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekTextSize.setProgress(AppPrefs.getTextSize(this));

        cbxBackgroundColor.setChecked(AppPrefs.isBackgroundColorEnabled(this));
        onCbxBackgroundColorChecked(AppPrefs.isBackgroundColorEnabled(this));
        seekBackgroundColorRed.setMax(MAX_PROGRESS_COLOR);
        seekBackgroundColorGreen.setMax(MAX_PROGRESS_COLOR);
        seekBackgroundColorBlue.setMax(MAX_PROGRESS_COLOR);
        seekBackgroundColorRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                backgroundColorRed = progress;
                viewBackgroundColorRed.setBackgroundColor(Color.rgb(backgroundColorRed, 0, 0));
                int color = Color.rgb(backgroundColorRed, backgroundColorGreen, backgroundColorBlue);
                AppPrefs.putBackgroundColor(DeveloperModeActivity.this, color);
                edt1.setBackgroundColor(color);
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBackgroundColorGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                backgroundColorGreen = progress;
                viewBackgroundColorGreen.setBackgroundColor(Color.rgb(0, backgroundColorGreen, 0));
                int color = Color.rgb(backgroundColorRed, backgroundColorGreen, backgroundColorBlue);
                AppPrefs.putBackgroundColor(DeveloperModeActivity.this, color);
                edt1.setBackgroundColor(color);
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBackgroundColorBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                backgroundColorBlue = progress;
                viewBackgroundColorBlue.setBackgroundColor(Color.rgb(0, 0, backgroundColorBlue));
                int color = Color.rgb(backgroundColorRed, backgroundColorGreen, backgroundColorBlue);
                AppPrefs.putBackgroundColor(DeveloperModeActivity.this, color);
                edt1.setBackgroundColor(color);
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        backgroundColorRed = Color.red(AppPrefs.getBackgroundColor(this));
        backgroundColorGreen = Color.green(AppPrefs.getBackgroundColor(this));
        backgroundColorBlue = Color.blue(AppPrefs.getBackgroundColor(this));
        seekBackgroundColorRed.setProgress(backgroundColorRed);
        seekBackgroundColorGreen.setProgress(backgroundColorGreen);
        seekBackgroundColorBlue.setProgress(backgroundColorBlue);

        cbxError.setChecked(AppPrefs.isErrorEnabled(this));
        onCbxErrorChecked(AppPrefs.isErrorEnabled(this));
        edtError.setText(AppPrefs.getErrorText(this));

        cbxErrorColor.setChecked(AppPrefs.getErrorColorEnabled(this));
        onCbxErrorColorChecked(AppPrefs.getErrorColorEnabled(this));
        seekErrorColorRed.setMax(MAX_PROGRESS_COLOR);
        seekErrorColorGreen.setMax(MAX_PROGRESS_COLOR);
        seekErrorColorBlue.setMax(MAX_PROGRESS_COLOR);
        seekErrorColorRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                errorColorRed = progress;
                viewErrorColorRed.setBackgroundColor(Color.rgb(errorColorRed, 0, 0));
                int color = Color.rgb(errorColorRed, errorColorGreen, errorColorBlue);
                AppPrefs.putErrorColor(DeveloperModeActivity.this, color);
                edt1.setErrorColor(color);
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekErrorColorGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                errorColorGreen = progress;
                viewErrorColorGreen.setBackgroundColor(Color.rgb(0, errorColorGreen, 0));
                int color = Color.rgb(errorColorRed, errorColorGreen, errorColorBlue);
                AppPrefs.putErrorColor(DeveloperModeActivity.this, color);
                edt1.setErrorColor(color);
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekErrorColorBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                errorColorBlue = progress;
                viewErrorColorBlue.setBackgroundColor(Color.rgb(0, 0, errorColorBlue));
                int color = Color.rgb(errorColorRed, errorColorGreen, errorColorBlue);
                AppPrefs.putErrorColor(DeveloperModeActivity.this, color);
                edt1.setErrorColor(color);
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        errorColorRed = Color.red(AppPrefs.getErrorColor(this));
        errorColorGreen = Color.green(AppPrefs.getErrorColor(this));
        errorColorBlue = Color.blue(AppPrefs.getErrorColor(this));
        seekErrorColorRed.setProgress(errorColorRed);
        seekErrorColorGreen.setProgress(errorColorGreen);
        seekErrorColorBlue.setProgress(errorColorBlue);

        cbxFloatingLabel.setChecked(AppPrefs.isFloatingLabelEnabled(this));
        onCbxFloatingLabelChecked(AppPrefs.isFloatingLabelEnabled(this));
        edtFloatingLabel.setText(AppPrefs.getFloatingLabelText(this));

        cbxCharCounter.setChecked(AppPrefs.isCharCounterEnabled(this));
        onCbxCharCounterChecked(AppPrefs.isCharCounterEnabled(this));
        seekMaxCharacters.setMax(MAX_PROGRESS_CHAR_COUNT);
        seekMaxCharacters.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AppPrefs.putMaxCharacters(DeveloperModeActivity.this, progress);
                edt1.setMaxCharacters(progress);
                tvMaxCharacters.setText(String.valueOf(progress));
                updateCodeSnippet();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekMaxCharacters.setProgress(Color.blue(AppPrefs.getMaxCharacters(this)));
    }

    @OnClick(R.id.developer_mode_lin_background_color)
    protected void onLinBackgroundColorClicked() {
        cbxBackgroundColor.performClick();
    }

    @OnClick(R.id.developer_mode_lin_error)
    protected void onLinErrorClicked() {
        cbxError.performClick();
    }

    @OnClick(R.id.developer_mode_lin_error_color)
    protected void onLinErrorColorClicked() {
        cbxErrorColor.performClick();
    }

    @OnClick(R.id.developer_mode_lin_floating_label)
    protected void onLinFloatingLabelClicked() {
        cbxFloatingLabel.performClick();
    }

    @OnClick(R.id.developer_mode_lin_char_counter)
    protected void onLinCharCounterClicked() {
        cbxCharCounter.performClick();
    }

    @OnCheckedChanged(R.id.developer_mode_cbx_background_color)
    protected void onCbxBackgroundColorChecked(boolean checked) {
        AppPrefs.putBackgroundColorEnabled(this, checked);
        seekBackgroundColorRed.setEnabled(checked);
        seekBackgroundColorGreen.setEnabled(checked);
        seekBackgroundColorBlue.setEnabled(checked);
        if (checked) {
            edt1.setBackgroundColor(Color.rgb(backgroundColorRed, backgroundColorGreen, backgroundColorBlue));
        } else {
            edt1.setBackgroundColor(AppPrefs.PREF_BACKGROUND_COLOR_DEFAULT);
        }
        updateCodeSnippet();
    }

    @OnCheckedChanged(R.id.developer_mode_cbx_error)
    protected void onCbxErrorChecked(boolean checked) {
        AppPrefs.putErrorEnabled(this, checked);
        edtError.setEnabled(checked);
        if (checked) {
            edt1.setError(edtError.getText());
        } else {
            edt1.setError(null);
        }
        updateCodeSnippet();
    }

    @OnCheckedChanged(R.id.developer_mode_cbx_error_color)
    protected void onCbxErrorColorChecked(boolean checked) {
        AppPrefs.putErrorColorEnabled(this, checked);
        seekErrorColorRed.setEnabled(checked);
        seekErrorColorGreen.setEnabled(checked);
        seekErrorColorBlue.setEnabled(checked);
        if (checked) {
            edt1.setErrorColor(Color.rgb(errorColorRed, errorColorGreen, errorColorBlue));
        } else {
            edt1.setErrorColor(AppPrefs.PREF_ERROR_COLOR_DEFAULT);
        }
        updateCodeSnippet();
    }

    @OnCheckedChanged(R.id.developer_mode_cbx_floating_label)
    protected void onCbxFloatingLabelChecked(boolean checked) {
        AppPrefs.putFloatingLabelEnabled(this, checked);
        edtFloatingLabel.setEnabled(checked);
        edt1.setFloatingLabel(checked);
        if (checked) {
            edt1.setHint(edtFloatingLabel.getText());
        }
        updateCodeSnippet();
    }

    @OnCheckedChanged(R.id.developer_mode_cbx_char_counter)
    protected void onCbxCharCounterChecked(boolean checked) {
        AppPrefs.putCharCounterEnabled(this, checked);
        seekMaxCharacters.setEnabled(checked);
        if (checked) {
            edt1.setMaxCharacters(seekMaxCharacters.getProgress());
        } else {
            edt1.setMaxCharacters(0);
        }
        updateCodeSnippet();
    }

    @OnTextChanged(R.id.developer_mode_edt_1)
    protected void onEdt1TextChanged(CharSequence text) {
        updateCodeSnippet();
    }

    @OnTextChanged(R.id.developer_mode_edt_error)
    protected void onEdtErrorTextChanged(CharSequence text) {
        AppPrefs.putErrorText(this, text);
        if (cbxError.isChecked()) {
            edt1.setError(text);
        }
    }

    @OnTextChanged(R.id.developer_mode_edt_floating_label)
    protected void onEdtFloatingLabeTextChanged(CharSequence text) {
        AppPrefs.putFloatingLabelText(this, text);
        edt1.setHint(text);
        updateCodeSnippet();
    }

    private void updateCodeSnippet() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<MaterialEditText")
                .append("\n").append(TABSPACE).append("android:layout_width=\"wrap_content\"")
                .append("\n").append(TABSPACE).append("android:layout_height=\"wrap_content\"")
                .append("\n").append(TABSPACE).append("android:textSize=\"").append(OFFSET_PROGRESS_TEXT_SIZE + AppPrefs.getTextSize(this)).append("sp\"")
                .append("\n").append(TABSPACE).append("android:text=\"").append(edt1.getText()).append("\"")
                .append("\n").append(TABSPACE).append("android:hint=\"").append(AppPrefs.getFloatingLabelText(this)).append("\"");

        if (AppPrefs.isBackgroundColorEnabled(this)) {
            stringBuilder.append("\n").append(TABSPACE).append("app:backgroundColor=\"").append(formatColor(AppPrefs.getBackgroundColor(this))).append("\"");
        }
        if (AppPrefs.isErrorEnabled(this)) {
            stringBuilder.append("\n").append(TABSPACE).append("app:errorColor=\"").append(formatColor(AppPrefs.getErrorColor(this))).append("\"");
        }
        if (AppPrefs.isFloatingLabelEnabled(this)) {
            stringBuilder.append("\n").append(TABSPACE).append("app:floatingLabel=\"").append(AppPrefs.isFloatingLabelEnabled(this)).append("\"");
        }
        if (AppPrefs.isCharCounterEnabled(this)) {
            stringBuilder.append("\n").append(TABSPACE).append("app:maxCharacters=\"").append(AppPrefs.getMaxCharacters(this)).append("\"");
        }

        stringBuilder.append("/>");
        edtResult.setText(stringBuilder);
    }

    private String formatColor(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }
}
