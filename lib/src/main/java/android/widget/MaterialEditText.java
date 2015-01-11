package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import com.pombo.materialedittext.lib.R;

public class MaterialEditText extends EditText {

    private float dimen_1dp;
    private float dimen_2dp;
    private float dimen_8dp;
    private float dimen_16dp;
    private float dimen_20dp;
    private float dimen_12sp;

    private DashPathEffect pathEffect;
    private float lineThickness;
    private float lineHeight;
    private int lineColor;
    private Paint linePaint;
    private Path line;

    private CharSequence errorText;
    private boolean drawError;
    private int errorTextColor;
    private TextPaint errorTextPaint;

    private boolean floatingLabel;
    private boolean drawLabel;
    private int labelTextColor;
    private TextPaint labelTextPaint;

    private int charCount;
    private int maxCharCount;
    private boolean drawCharCounter;
    private int charCountTextColor;
    private TextPaint charCountTextPaint;

//    private boolean fullWidth;

    public MaterialEditText(Context context) {
        this(context, null);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        Resources r = getResources();
        DisplayMetrics displayMetrics = r.getDisplayMetrics();
        dimen_1dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics);
        dimen_2dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics);
        dimen_8dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
        dimen_16dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);
        dimen_20dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
        dimen_12sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics);

        lineColor = getCurrentHintTextColor();
        errorTextColor = getCurrentHintTextColor();
        labelTextColor = getCurrentHintTextColor();

        if (!isEnabled()) {
            pathEffect = new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0);
            lineColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
            labelTextColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        }
        lineThickness = dimen_1dp;
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        line = new Path();
        errorTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        // 0btain XML attributes
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.materialEditText, 0, 0);
            floatingLabel = ta.getBoolean(R.styleable.materialEditText_floatingLabel, false);
            maxCharCount = ta.getInteger(R.styleable.materialEditText_maxCharacters, 0);
//            fullWidth = ta.getBoolean(R.styleable.materialEditText_fullWidth, false);
        }

        // Override API padding
//        if (fullWidth) {
//            setPadding((int) dimen_16dp, (int) dimen_20dp, (int) dimen_16dp, (int) dimen_20dp);
//        } else {
        setPadding(0, (int) dimen_16dp, 0, (int) dimen_16dp);
        if (maxCharCount > 0) {
            appendPadding(0, 0, 0, (int) (dimen_8dp + dimen_12sp));
            charCountTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
        if (floatingLabel) {
            appendPadding(0, (int) (dimen_8dp + dimen_12sp), 0, 0);
            labelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
//        }
        setIncludeFontPadding(false); // Remove text top/bottom padding)
        setBackground(null);  // Remove API background
        setTextSize(16);

        onTextChanged(getText(), 0, getText().length(), getText().length()); // Refresh label visibility
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawError && !TextUtils.isEmpty(errorText)) {
            lineHeight = getHeight() - (2 * dimen_8dp + dimen_12sp);
            errorTextPaint.setColor(errorTextColor);
            errorTextPaint.setTextSize(dimen_12sp);
            canvas.drawText(errorText, 0, errorText.length(), getScrollX(), getHeight() - dimen_8dp, errorTextPaint);
        } else if (maxCharCount > 0) {
            lineHeight = getHeight() - (2 * dimen_8dp + dimen_12sp);
            if (drawCharCounter) {
                charCountTextPaint.setColor(charCountTextColor);
                charCountTextPaint.setTextSize(dimen_12sp);
                charCountTextPaint.setTextAlign(Paint.Align.RIGHT);
                String text = charCount + " / " + maxCharCount;
                canvas.drawText(text, 0, text.length(), getScrollX() + getWidth(), getHeight() - dimen_8dp, charCountTextPaint);
            }
        } else {
            lineHeight = getHeight() - dimen_8dp;
        }
        if (drawLabel && !TextUtils.isEmpty(getHint())) {
            labelTextPaint.setColor(labelTextColor);
            labelTextPaint.setTextSize(dimen_12sp);
            canvas.drawText(getHint(), 0, getHint().length(), getScrollX(), dimen_16dp + dimen_12sp, labelTextPaint);
        }

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineThickness);
        linePaint.setPathEffect(pathEffect);
        line.reset();
        line.moveTo(getScrollX(), lineHeight);
        line.lineTo(getScrollX() + getWidth(), lineHeight);
        canvas.drawPath(line, linePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lineColor = obtainColorPrimary(getContext());
                    labelTextColor = obtainColorPrimary(getContext());
                    lineThickness = dimen_2dp;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isFocused()) {
                        if (drawError || (drawCharCounter && charCount > maxCharCount)) {
                            lineColor = getContext().getResources().getColor(R.color.material_red);
                        }
                    } else {
                        if (drawError ) {
                            lineColor = getContext().getResources().getColor(R.color.material_red);
                            lineThickness = dimen_2dp;
                        } else {
                            lineColor = getCurrentHintTextColor();
                            lineThickness = dimen_1dp;
                        }
                        labelTextColor = getCurrentHintTextColor();
                    }
                    invalidate();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (maxCharCount > 0) {
            if (focused) {
                drawCharCounter = true;
            } else {
                drawCharCounter = false;
            }
        }
        if (!drawError) {
            if (focused) {
                if (drawCharCounter && charCount > maxCharCount) {
                    lineColor = getContext().getResources().getColor(R.color.material_red);
                } else {
                    lineColor = obtainColorPrimary(getContext());
                }
                labelTextColor = obtainColorPrimary(getContext());
                lineThickness = dimen_2dp;
            } else {
                lineColor = getCurrentHintTextColor();
                labelTextColor = getCurrentHintTextColor();
                lineThickness = dimen_1dp;
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setError(CharSequence error) {
        if (error != null) {
            errorText = error;
            drawError = true;
            appendPadding(0, 0, 0, (int) (dimen_8dp + dimen_12sp));
            lineColor = getContext().getResources().getColor(R.color.material_red);
            errorTextColor = getContext().getResources().getColor(R.color.material_red);
            lineThickness = dimen_2dp;
        } else {
            errorText = null;
            drawError = false;
            appendPadding(0, 0, 0, (int) -(dimen_8dp + dimen_12sp));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            pathEffect = null;
//            currentColor = getCurrentHintTextColor();
        } else {
            pathEffect = new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0);
//            currentColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        }
        super.setEnabled(enabled);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (floatingLabel) {
            if (text.length() > 0) {
                drawLabel = true;
            } else {
                drawLabel = false;
            }
        }
        if (maxCharCount > 0) {
            charCount = text.length();
            if (drawCharCounter) {
                if (charCount > maxCharCount) {
                    lineColor = getContext().getResources().getColor(R.color.material_red);
                } else {
                    lineColor = obtainColorPrimary(getContext());
                }
            }
            if (charCount > maxCharCount) {
                charCountTextColor = getContext().getResources().getColor(R.color.material_red);
            } else {
                charCountTextColor = getCurrentHintTextColor();
            }
        }
    }

    private void appendPadding(int left, int top, int right, int bottom) {
        setPadding(getPaddingLeft() + left, getPaddingTop() + top, getPaddingRight() + right, getPaddingBottom() + bottom);
    }

    private int obtainColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.getTheme().obtainStyledAttributes(typedValue.data, new int[]{ R.attr.colorPrimary });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    private int obtainColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.getTheme().obtainStyledAttributes(typedValue.data, new int[]{ R.attr.colorAccent });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}
