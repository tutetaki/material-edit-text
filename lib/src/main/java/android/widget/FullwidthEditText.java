package android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import fr.erictruong.materialedittext.lib.R;

import java.lang.reflect.Field;

public class FullwidthEditText extends EditText {

    private float dimen_8dp, dimen_20dp;
    private float dimen_12sp;

    private float basePaddingLeft, basePaddingTop, basePaddingRight, basePaddingBottom;
    private float paddingLeft, paddingTop, paddingRight, paddingBottom;

    private int hintColor;

    private int charCount;
    private int maxCharCount;
    private boolean drawCharCounter;
    private float charCountTextHeight;
    private int charCountTextColor;
    private TextPaint charCountTextPaint;

    private float inlineCountTextOffset;

    public FullwidthEditText(Context context) {
        this(context, null);
    }

    public FullwidthEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FullwidthEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public FullwidthEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources r = getResources();
        DisplayMetrics displayMetrics = r.getDisplayMetrics();
        dimen_8dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
        dimen_20dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
        dimen_12sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics);

        // 0btain XML attributes
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.fullwidthEditText, 0, 0);
            maxCharCount = ta.getInteger(R.styleable.fullwidthEditText_maxCharacters, 0);
            ta.recycle();
        }

        charCountTextColor = getCurrentHintTextColor();
        if (!isEnabled()) {
            hintColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        } else {
            hintColor = getCurrentHintTextColor();
        }

        // Override API padding
        basePaddingTop = dimen_20dp;
        basePaddingBottom = dimen_20dp;
        if (maxCharCount > 0) {
            if (getMaxLines() > 1) {
                basePaddingBottom += dimen_8dp + dimen_12sp;
            }
            charCountTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
        updatePadding();
        setIncludeFontPadding(false); // Remove text top/bottom padding)
        setBackgroundDrawable(null);  // Remove API background
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

        if (maxCharCount > 0 && drawCharCounter) {
            charCountTextPaint.setColor(charCountTextColor);
            charCountTextPaint.setTextSize(dimen_12sp);
            charCountTextPaint.setTextAlign(Paint.Align.RIGHT);
            String text = charCount + " / " + maxCharCount;
            if (getMaxLines() > 1) {
                charCountTextHeight = getHeight() - dimen_8dp;
                inlineCountTextOffset = 0;
            } else {
                charCountTextHeight = getBaseline();
                inlineCountTextOffset = charCountTextPaint.measureText(text) + dimen_8dp;
            }
            canvas.drawText(text, 0, text.length(), getScrollX() + getWidth(), charCountTextHeight, charCountTextPaint);

            super.setPadding((int) (basePaddingLeft + paddingLeft), (int) (basePaddingTop + paddingTop), (int) (basePaddingRight + paddingRight + inlineCountTextOffset), (int) (basePaddingBottom + paddingBottom));
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (isEnabled()) {
            if (focused) {
                if (maxCharCount > 0) {
                    drawCharCounter = true;
                    if (charCount > maxCharCount) {
                        charCountTextColor = getContext().getResources().getColor(R.color.material_red);
                    } else {
                        charCountTextColor = hintColor;
                    }
                }
            } else {
                if (maxCharCount > 0) {
                    drawCharCounter = false;
                }
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        charCount = text.length();
        if (maxCharCount > 0) {
            if (charCount > maxCharCount) {
                charCountTextColor = getContext().getResources().getColor(R.color.material_red);
            } else {
                charCountTextColor = hintColor;
            }
        }
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
