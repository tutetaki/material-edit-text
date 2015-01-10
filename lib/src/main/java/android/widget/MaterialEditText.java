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

    private  float dimen_1dp;
    private  float dimen_2dp;
    private  float dimen_8dp;
    private  float dimen_16dp;

    private  float dimen_12sp;

    private float lineHeight;
    private Path line;
    private Paint linePaint;

    private CharSequence errorText;
    private boolean drawError;
    private Paint errorTextPaint;

    private boolean floatingLabel;
    private boolean drawLabel;
    private Paint labelTextPaint;

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

        dimen_12sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics);

        setPadding(0, (int) dimen_16dp, 0, (int) dimen_16dp); // Remove API padding
        setIncludeFontPadding(false); // Remove text top/bottom padding)
        setBackground(null);  // Remove API background
        setTextSize(16);

        line = new Path();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(getCurrentHintTextColor());
        linePaint.setStrokeWidth(dimen_1dp);
        if (!isEnabled()) {
            linePaint.setPathEffect(new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0));
        }
        errorTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        errorTextPaint.setColor(getContext().getResources().getColor(R.color.material_red));
        errorTextPaint.setTextSize(dimen_12sp);

        // 0btain XML attributes
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.materialEditText, 0, 0);
            floatingLabel = ta.getBoolean(R.styleable.materialEditText_floatingLabel, false);
        }

        if (floatingLabel) {
            appendPadding(0, (int) (dimen_8dp + dimen_12sp), 0, 0);

            labelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            labelTextPaint.setColor(getCurrentHintTextColor());
            labelTextPaint.setTextSize(dimen_12sp);
        }

        onTextChanged(getText(), 0, getText().length(), getText().length());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawError && !TextUtils.isEmpty(errorText)) {
            lineHeight = getHeight() - (2 * dimen_8dp + dimen_12sp);
            canvas.drawText(errorText, 0, errorText.length(), 0, getHeight() - dimen_8dp, errorTextPaint);
        } else {
            lineHeight = getHeight() - dimen_8dp;
        }

        if (drawLabel && !TextUtils.isEmpty(getHint())) {
            canvas.drawText(getHint(), 0, getHint().length(), 0, dimen_16dp + dimen_12sp, labelTextPaint);
        }

        line.reset();
        line.moveTo(0, lineHeight);
        line.lineTo(getWidth(), lineHeight);
        canvas.drawPath(line, linePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled() && !drawError) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    linePaint.setColor(obtainColorPrimary(getContext()));
                    linePaint.setStrokeWidth(dimen_2dp);
                    if (floatingLabel) {
                        labelTextPaint.setColor(obtainColorPrimary(getContext()));
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (!isFocused()) {
                        linePaint.setColor(getCurrentHintTextColor());
                        linePaint.setStrokeWidth(dimen_1dp);
                        if (floatingLabel) {
                            labelTextPaint.setColor(getCurrentHintTextColor());
                        }
                        invalidate();
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (!drawError) {
            if (focused) {
                linePaint.setColor(obtainColorPrimary(getContext()));
                linePaint.setStrokeWidth(dimen_2dp);
            } else {
                linePaint.setColor(getCurrentHintTextColor());
                linePaint.setStrokeWidth(dimen_1dp);
            }
        }
        if (floatingLabel) {
            if (focused) {
                labelTextPaint.setColor(obtainColorPrimary(getContext()));
            } else {
                labelTextPaint.setColor(getCurrentHintTextColor());
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setError(CharSequence error) {
        if (error == null) {
            drawError = false;
            errorText = null;
            appendPadding(0, 0, 0, (int) -(dimen_8dp + dimen_12sp));

        } else {
            drawError = true;
            errorText = error;
            appendPadding(0, 0, 0, (int) (dimen_8dp + dimen_12sp));

            linePaint.setColor(getContext().getResources().getColor(R.color.material_red));
            linePaint.setStrokeWidth(dimen_2dp);
        }
        invalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (linePaint == null) {
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(getCurrentHintTextColor());
        linePaint.setStrokeWidth(dimen_1dp);
        if (enabled) {
            linePaint.setPathEffect(null);
        } else {
            linePaint.setPathEffect(new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0));
        }

        if (labelTextPaint== null) {
            labelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
        labelTextPaint.setColor(getCurrentHintTextColor());
        labelTextPaint.setTextSize(dimen_12sp);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (floatingLabel) {
            if (lengthAfter > 0 || start > 0) {
                drawLabel = true;
            } else {
                drawLabel = false;
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
