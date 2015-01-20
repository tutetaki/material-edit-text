package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import fr.erictruong.materialedittext.lib.R;

public class FullwidthEditText extends EditText {

    private float dimen_8dp, dimen_20dp;
    private float dimen_12sp;

    private float basePaddingLeft, basePaddingTop, basePaddingRight, basePaddingBottom;

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
        this(context, attrs, defStyleAttr, 0);
    }

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
        }

        charCountTextColor = getCurrentHintTextColor();
        if (!isEnabled()) {
            hintColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        } else {
            hintColor = getCurrentHintTextColor();
        }

        // Override API padding
        basePaddingLeft = 0;
        basePaddingTop = dimen_20dp;
        basePaddingRight = 0;
        basePaddingBottom = dimen_20dp;
        if (maxCharCount > 0) {
            if (getMaxLines() > 1) {
                basePaddingBottom += dimen_8dp + dimen_12sp;
            }
            charCountTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
        setPadding(0, 0, 0, 0);
        setIncludeFontPadding(false); // Remove text top/bottom padding)
        setBackground(null);  // Remove API background
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
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

            setPadding(0, 0, (int) inlineCountTextOffset, 0);
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
}
