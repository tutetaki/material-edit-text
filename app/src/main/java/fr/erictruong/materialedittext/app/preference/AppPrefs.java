package fr.erictruong.materialedittext.app.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.SeekBar;
import fr.erictruong.materialedittext.app.DeveloperModeActivity;
import fr.erictruong.materialedittext.app.R;

public final class AppPrefs {

    private static final String PREF_TEXT_SIZE = "pref_text_size";
    private static final String PREF_BACKGROUND_COLOR_ENABLED = "pref_background_color_enabled";
    private static final String PREF_BACKGROUND_COLOR = "pref_background_color";
    private static final String PREF_ERROR_ENABLED = "pref_error_enabled";
    private static final String PREF_ERROR_TEXT = "pref_error_text";
    private static final String PREF_ERROR_COLOR_ENABLED = "pref_error_color_enabled";
    private static final String PREF_ERROR_COLOR = "pref_error_color";
    private static final String PREF_FLOATING_LABEL_ENABLED = "pref_floating_label_enabled";
    private static final String PREF_FLOATING_LABEL_TEXT = "pref_floating_label_text";
    private static final String PREF_CHAR_COUNTER_ENABLED = "pref_char_counter_enabled";
    private static final String PREF_MAX_CHARACTERS = "pref_max_characters";

    public static final int PREF_TEXT_SIZE_DEFAULT = 0;
    public static final boolean PREF_BACKGROUND_COLOR_ENABLED_DEFAULT = false;
    public static final int PREF_BACKGROUND_COLOR_DEFAULT = 0xff03a9f4;
    public static final boolean PREF_ERROR_ENABLED_DEFAULT = false;
    public static final String PREF_ERROR_TEXT_DEFAULT = "Error text";
    public static final boolean PREF_ERROR_COLOR_ENABLED_DEFAULT = false;
    public static final int PREF_ERROR_COLOR_DEFAULT = 0xfff44336;
    public static final boolean PREF_FLOATING_LABEL_ENABLED_DEFAULT = false;
    public static final String PREF_FLOATING_LABEL_TEXT_DEFAULT = "Label text";
    public static final boolean PREF_CHAR_COUNTER_ENABLED_DEFAULT = false;
    public static final int PREF_MAX_CHARACTERS_DEFAULT = 0;

    private AppPrefs() {}

    public static void putTextSize(Context context, int size) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_TEXT_SIZE, size).apply();
    }

    public static int getTextSize(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_TEXT_SIZE, PREF_TEXT_SIZE_DEFAULT);
    }

    public static void putBackgroundColorEnabled(Context context, boolean enabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_BACKGROUND_COLOR_ENABLED, enabled).apply();
    }

    public static boolean isBackgroundColorEnabled(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_BACKGROUND_COLOR_ENABLED, PREF_BACKGROUND_COLOR_ENABLED_DEFAULT);
    }

    public static void putBackgroundColor(Context context, int color) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_BACKGROUND_COLOR, color).apply();
    }

    public static int getBackgroundColor(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_BACKGROUND_COLOR, PREF_BACKGROUND_COLOR_DEFAULT);
    }

    public static void putErrorEnabled(Context context, boolean enabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_ERROR_ENABLED, enabled).apply();
    }

    public static boolean isErrorEnabled(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_ERROR_ENABLED, PREF_ERROR_ENABLED_DEFAULT);
    }

    public static void putErrorText(Context context, CharSequence text) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_ERROR_TEXT, text.toString()).apply();
    }

    public static String getErrorText(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_ERROR_TEXT, PREF_ERROR_TEXT_DEFAULT);
    }

    public static void putErrorColorEnabled(Context context, boolean enabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_ERROR_COLOR_ENABLED, enabled).apply();
    }

    public static boolean getErrorColorEnabled(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_ERROR_COLOR_ENABLED, PREF_ERROR_COLOR_ENABLED_DEFAULT);
    }

    public static void putErrorColor(Context context, int color) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_ERROR_COLOR, color).apply();
    }

    public static int getErrorColor(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_ERROR_COLOR, PREF_ERROR_COLOR_DEFAULT);
    }

    public static void putFloatingLabelEnabled(Context context, boolean enabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_FLOATING_LABEL_ENABLED, enabled).apply();
    }

    public static boolean isFloatingLabelEnabled(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_FLOATING_LABEL_ENABLED, PREF_FLOATING_LABEL_ENABLED_DEFAULT);
    }

    public static void putFloatingLabelText(Context context, CharSequence text) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_FLOATING_LABEL_TEXT, text.toString()).apply();
    }

    public static String getFloatingLabelText(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_FLOATING_LABEL_TEXT, PREF_FLOATING_LABEL_TEXT_DEFAULT);
    }

    public static void putCharCounterEnabled(Context context, boolean enabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_CHAR_COUNTER_ENABLED, enabled).apply();
    }

    public static boolean isCharCounterEnabled(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_CHAR_COUNTER_ENABLED, PREF_CHAR_COUNTER_ENABLED_DEFAULT);
    }

    public static void putMaxCharacters(Context context, int size) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_MAX_CHARACTERS, size).apply();
    }

    public static int getMaxCharacters(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_MAX_CHARACTERS, PREF_MAX_CHARACTERS_DEFAULT);
    }
}