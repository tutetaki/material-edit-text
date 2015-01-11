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
    private float dimen_2sp;
    private float dimen_12sp;
    private float dimen_16sp;

    private int highlightColor;

    private DashPathEffect lineEffect;
    private float lineWidth;
    private float lineHeight;
    private float lineThickness;
    private Paint linePaint;
    private Path line;

    private CharSequence errorText;
    private TextPaint errorTextPaint;

    private boolean floatingLabel;
    private TextPaint labelTextPaint;

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
        dimen_2sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 2, displayMetrics);
        dimen_12sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics);
        dimen_16sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics);

        highlightColor = getCurrentHintTextColor();

        if (!isEnabled()) {
            lineEffect = new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0);
            highlightColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        }
        lineThickness = dimen_1dp;
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        line = new Path();
        errorTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        // 0btain XML attributes
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.materialEditText, 0, 0);
            floatingLabel = ta.getBoolean(R.styleable.materialEditText_floatingLabel, false);
        }

        // Override API padding
        setPadding(0, (int) dimen_16dp, 0, (int) dimen_16dp);
        if (floatingLabel) {
            appendPadding(0, (int) (dimen_8dp + dimen_12sp), 0, 0);
            labelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
        setIncludeFontPadding(false); // Remove text top/bottom padding)
        setBackground(null);  // Remove API background
        setTextSize(16);

        onTextChanged(getText(), 0, getText().length(), getText().length()); // Refresh label visibility
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (floatingLabel && !TextUtils.isEmpty(getHint())) {
            labelTextPaint.setColor(highlightColor);
            labelTextPaint.setTextSize(dimen_12sp);
            canvas.drawText(getHint(), 0, getHint().length(), getScrollX(), dimen_16dp + dimen_12sp, labelTextPaint);
        }

        if (!TextUtils.isEmpty(getError())) {
            lineHeight = getHeight() - (2 * dimen_8dp + dimen_12sp);
            errorTextPaint.setColor(highlightColor);
            errorTextPaint.setTextSize(dimen_12sp);
            canvas.drawText(getError(), 0, getError().length(), getScrollX(), getHeight() - dimen_8dp, errorTextPaint);
        } else {
            lineHeight = getHeight() - dimen_8dp;
        }

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(highlightColor);
        linePaint.setStrokeWidth(lineThickness);
        linePaint.setPathEffect(lineEffect);
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
                    if (TextUtils.isEmpty(getError())) {
                        highlightColor = obtainColorPrimary(getContext());
                    }
                    lineThickness = dimen_2dp;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (!isFocused()) {
                        if (TextUtils.isEmpty(getError())) {
                            highlightColor = getCurrentHintTextColor();
                        }
                        lineThickness = dimen_1dp;
                        invalidate();
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            if (TextUtils.isEmpty(getError())) {
                highlightColor = obtainColorPrimary(getContext());
            }
            lineThickness = dimen_2dp;
        } else {
            if (TextUtils.isEmpty(getError())) {
                highlightColor = getCurrentHintTextColor();
            }
            lineThickness = dimen_1dp;
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setError(CharSequence error) {
        errorText = error;
        if (TextUtils.isEmpty(error)) {
            appendPadding(0, 0, 0, (int) -(dimen_8dp + dimen_12sp));
        } else {
            appendPadding(0, 0, 0, (int) (dimen_8dp + dimen_12sp));
            highlightColor = getContext().getResources().getColor(R.color.material_red);
        }
    }

    @Override
    public CharSequence getError() {
        return errorText;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            lineEffect = null;
        } else {
            lineEffect = new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0);
        }
        super.setEnabled(enabled);
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
