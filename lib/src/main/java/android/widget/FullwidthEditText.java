package android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import fr.erictruong.materialedittext.lib.R;

import java.lang.reflect.Field;

public class FullwidthEditText extends EditText {

    private final float DIMEN_8_DP  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    private final float DIMEN_20_DP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
    private final float DIMEN_12_SP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());

    private final int COLOR_MATERIAL_RED_500 = 0xfff44336;
    private final int COLOR_HINT;

    // Paddings
    private float basePaddingLeft;
    private float basePaddingTop;
    private float basePaddingRight;
    private float basePaddingBottom;
    private float paddingLeft;
    private float paddingTop;
    private float paddingRight;
    private float paddingBottom;

    private int errorColor;

    private int maxCharacters;
    private int charCount;
    private float charCountTextHeight;
    private TextPaint charCountTextPaint;
    private int charCountTextColor;

    private float inlineCountTextOffset;

    public FullwidthEditText(Context context) {
        this(context, null);
    }

    public FullwidthEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }

    public FullwidthEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public FullwidthEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Override API padding
        basePaddingLeft = 0;
        basePaddingTop = DIMEN_20_DP;
        basePaddingRight = 0;
        basePaddingBottom = DIMEN_20_DP;

        // Remove text top/bottom padding)
        setIncludeFontPadding(false);

        // Remove API background
        setBackgroundDrawable(null);

        // 0btain XML attributes
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.fullwidthEditText, 0, 0);
            errorColor = ta.getColor(R.styleable.fullwidthEditText_errorColor, COLOR_MATERIAL_RED_500);
            maxCharacters = ta.getInteger(R.styleable.fullwidthEditText_maxCharacters, 0);
            ta.recycle();
        }

        charCountTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        charCountTextPaint.setTextSize(DIMEN_12_SP);
        charCountTextPaint.setTextAlign(Paint.Align.RIGHT);

        if (maxCharacters > 0 && getMaxLines() > 1) {
            basePaddingBottom += DIMEN_8_DP + DIMEN_12_SP;
        }

        updatePadding();
    }

    private void updatePadding() {
        super.setPadding((int) (basePaddingLeft + paddingLeft), (int) (basePaddingTop + paddingTop), (int) (basePaddingRight + paddingRight), (int) (basePaddingBottom + paddingBottom));
    }

    public void setBasePadding(int left, int top, int right, int bottom) {
        basePaddingLeft = left;
        basePaddingTop = top;
        basePaddingRight = right;
        basePaddingBottom = bottom;
        super.setPadding((int) paddingLeft + left, (int) paddingTop + top, (int) paddingRight + right, (int) paddingBottom + bottom);
    }

    public float getBasePaddingLeft() {
        return basePaddingLeft;
    }

    public float getBasePaddingTop() {
        return basePaddingTop;
    }

    public float getBasePaddingRight() {
        return basePaddingRight;
    }

    public float getBasePaddingBottom() {
        return basePaddingBottom;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
        super.setPadding((int) basePaddingLeft + left, (int) basePaddingTop + top, (int) basePaddingRight + right, (int) basePaddingBottom + bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        charCountTextColor = COLOR_HINT;

        if (maxCharacters > 0) {
            if (charCount > maxCharacters) {
                charCountTextColor = errorColor;
            }

            if (charCount > maxCharacters || isFocused()) {
                charCountTextPaint.setColor(charCountTextColor);
                String text = charCount + " / " + maxCharacters;
                if (getMaxLines() > 1) {
                    charCountTextHeight = getHeight() - DIMEN_8_DP;
                    inlineCountTextOffset = 0;
                } else {
                    charCountTextHeight = getBaseline();
                    inlineCountTextOffset = charCountTextPaint.measureText(text) + DIMEN_8_DP;
                }
                canvas.drawText(text, 0, text.length(), getScrollX() + getWidth(), charCountTextHeight, charCountTextPaint);
            }

            super.setPadding((int) (basePaddingLeft + paddingLeft), (int) (basePaddingTop + paddingTop), (int) (basePaddingRight + paddingRight + inlineCountTextOffset), (int) (basePaddingBottom + paddingBottom));
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        charCount = text.length();
    }

    @SuppressLint("NewApi")
    public int getMaxLines() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return super.getMaxLines();
        } else {
            Field mMaxModeField = null;
            Field mMaximumField = null;
            try {
                mMaxModeField = this.getClass().getDeclaredField("mMaxMode");
                mMaximumField = this.getClass().getDeclaredField("mMaximum");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            if (mMaxModeField != null && mMaximumField != null) {
                mMaxModeField.setAccessible(true);
                mMaximumField.setAccessible(true);

                try {
                    final int mMaxMode = mMaxModeField.getInt(this); // Maximum mode value
                    final int mMaximum = mMaximumField.getInt(this); // Maximum value

                    if (mMaxMode == 1) { // LINES is 1
                        return mMaximum;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}
