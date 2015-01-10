package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
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

    private float dimen_12sp;

    private float lineHeight;
    private Paint linePaint = new Paint();
    private Path line = new Path();

    private boolean hasError;
    private CharSequence errorText;
    private Paint errorTextPaint = new Paint();

    public MaterialEditText(Context context) {
        super(context);
        init(context);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        Resources r = getResources();
        DisplayMetrics displayMetrics = r.getDisplayMetrics();
        dimen_1dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics);
        dimen_2dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics);
        dimen_8dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
        dimen_16dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics);

        dimen_12sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics);

        setPadding(0, (int) dimen_16dp, 0, (int) dimen_16dp);
        setIncludeFontPadding(false);
        setBackground(null);
        setTextSize(16);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(getCurrentHintTextColor());
        linePaint.setStrokeWidth(dimen_1dp);
        if (!isEnabled()) {
            linePaint.setPathEffect(new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!hasError) {
            lineHeight = getHeight() - dimen_8dp;
        } else {
            lineHeight = getHeight() - (2 * dimen_8dp + dimen_12sp);
            canvas.drawText(errorText, 0, errorText.length(), 0, getHeight() - dimen_8dp, errorTextPaint);
        }

        line.moveTo(0, lineHeight);
        line.lineTo(getWidth(), lineHeight);

        canvas.drawPath(line, linePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled() && !hasError) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    linePaint.setColor(obtainColorPrimary(getContext()));
                    linePaint.setStrokeWidth(dimen_2dp);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    linePaint.setColor(getCurrentHintTextColor());
                    linePaint.setStrokeWidth(dimen_1dp);
                    invalidate();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (!hasError) {
            if (focused) {
                linePaint.setColor(obtainColorPrimary(getContext()));
                linePaint.setStrokeWidth(dimen_2dp);
            } else {
                linePaint.setColor(getCurrentHintTextColor());
                linePaint.setStrokeWidth(dimen_1dp);
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setError(CharSequence error) {
        if (error == null) {
            hasError = false;
            errorText = null;
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) (getPaddingBottom() - (dimen_8dp + dimen_12sp)));

        } else {
            hasError = true;
            errorText = error;
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) (getPaddingBottom() + (dimen_8dp + dimen_12sp)));

            linePaint.setColor(getContext().getResources().getColor(R.color.material_red));
            linePaint.setStrokeWidth(dimen_2dp);
            errorTextPaint.setColor(getContext().getResources().getColor(R.color.material_red));
            errorTextPaint.setTextSize(dimen_12sp);
        }
        invalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (linePaint == null) {
            linePaint = new Paint();
        }

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(getCurrentHintTextColor());
        linePaint.setStrokeWidth(dimen_1dp);

        if (enabled) {
            linePaint.setPathEffect(null);
        } else {
            linePaint.setPathEffect(new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0));
        }
        super.setEnabled(enabled);
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
