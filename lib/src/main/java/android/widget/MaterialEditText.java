package android.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.pombo.materialedittext.lib.R;

public class MaterialEditText extends EditText {

    private float dimen_1dp, dimen_2dp, dimen_8dp, dimen_16dp, dimen_20dp;
    private float dimen_12sp, dimen_16sp;

    private int highlightColor;
    private int hintColor;

    private DashPathEffect lineEffect;
    private float lineHeight;
    private float lineThickness;
    private Paint linePaint;
    private Paint defaultLinePaint;
    private Path line;
    private Path defaultLine;
    private AnimatorSet lineAnimation;
    private float xA;
    private float xB;

    private CharSequence errorText;
    private TextPaint errorTextPaint;

    private boolean floatingLabel;
    private TextPaint labelTextPaint;
    private AnimatorSet labelAnimation;
    private float labelX;
    private float labelY;
    private int labelTextColor;
    private float labelTextSize;
    private long labelDurationOffset;

    private int charCount;
    private int maxCharCount;
    private boolean drawCharCounter;
    private int charCountTextColor;
    private TextPaint charCountTextPaint;

    private int iconResId;

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
        dimen_16sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                xA = 0;
                xB = getWidth();

                if (getText().length() > 0) {
                    labelX = getScrollX();
                    labelY = dimen_16dp + dimen_12sp;
                    labelTextColor = hintColor;
                    labelTextSize = dimen_12sp;
                }
            }
        });

        highlightColor = Color.TRANSPARENT;
        hintColor = getCurrentHintTextColor();
        charCountTextColor = getCurrentHintTextColor();

        if (!isEnabled()) {
            lineEffect = new DashPathEffect(new float[]{ Math.round(dimen_1dp), Math.round(dimen_2dp) }, 0);
            highlightColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
            hintColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        }
        lineThickness = dimen_1dp;
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        line = new Path();
        defaultLine = new Path();
        errorTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        // 0btain XML attributes
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.materialEditText, 0, 0);
            floatingLabel = ta.getBoolean(R.styleable.materialEditText_floatingLabel, false);
            maxCharCount = ta.getInteger(R.styleable.materialEditText_maxCharacters, 0);
            iconResId = ta.getResourceId(R.styleable.materialEditText_withIcon, 0);
        }

        // Override API padding
        setPadding(0, (int) dimen_16dp, 0, (int) dimen_16dp);
        if (maxCharCount > 0) {
            appendPadding(0, 0, 0, (int) (dimen_8dp + dimen_12sp));
            charCountTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
        if (floatingLabel) {
            appendPadding(0, (int) (dimen_8dp + dimen_12sp), 0, 0);
            labelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        }
        setIncludeFontPadding(false); // Remove text top/bottom padding)
        setBackground(null);  // Remove API background
        setTextSize(16);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (floatingLabel && !TextUtils.isEmpty(getHint())) {
            labelTextPaint.setColor(labelTextColor);
            labelTextPaint.setTextSize(labelTextSize);
            canvas.drawText(getHint(), 0, getHint().length(), getScrollX() + labelX, labelY, labelTextPaint);
        }

        if (!TextUtils.isEmpty(getError())) {
            lineHeight = getHeight() - (2 * dimen_8dp + dimen_12sp);
            errorTextPaint.setColor(highlightColor);
            errorTextPaint.setTextSize(dimen_12sp);
            canvas.drawText(getError(), 0, getError().length(), getScrollX(), getHeight() - dimen_8dp, errorTextPaint);
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

        // Draw the unselected line
        defaultLinePaint.setStyle(Paint.Style.STROKE);
        if (!TextUtils.isEmpty(getError()) || (maxCharCount > 0 && charCount > maxCharCount)) {
            defaultLinePaint.setColor(labelTextColor);
        } else {
            defaultLinePaint.setColor(hintColor);
        }
        defaultLinePaint.setStrokeWidth(dimen_1dp);
        defaultLinePaint.setPathEffect(lineEffect);
        defaultLine.reset();
        defaultLine.moveTo(getScrollX(), lineHeight);
        defaultLine.lineTo(getScrollX() + getWidth(), lineHeight);
        canvas.drawPath(defaultLine, defaultLinePaint);

        // Draw the highlighted line
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(highlightColor);
        linePaint.setStrokeWidth(lineThickness);
        linePaint.setPathEffect(lineEffect);
        line.reset();
        line.moveTo(getScrollX() + xA, lineHeight);
        line.lineTo(getScrollX() + xB, lineHeight);
        canvas.drawPath(line, linePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (TextUtils.isEmpty(getError())) {
                        if (maxCharCount > 0 && charCount > maxCharCount) {
                            highlightColor = getContext().getResources().getColor(R.color.material_red);
                            if (labelY != getBaseline()) {
                                labelTextColor = getContext().getResources().getColor(R.color.material_red);
                            }
                        } else {
                            highlightColor = obtainColorPrimary(getContext());
                            if (labelY != getBaseline()) {
                                labelTextColor = obtainColorPrimary(getContext());
                            }
                        }
                    } else {
                        labelTextColor = getContext().getResources().getColor(R.color.material_red);
                    }
                    lineThickness = dimen_2dp;
                    if (!isFocused()) {
                        float x = event.getX();
                        lineAnimation = createLineAnimation(x, x, 0, getWidth());
                        lineAnimation.start();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (!isFocused()) {
                        if (TextUtils.isEmpty(getError())) {
                            highlightColor = Color.TRANSPARENT;
                            labelTextColor = hintColor;
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
                if (maxCharCount > 0 && charCount > maxCharCount) {
                    highlightColor = getContext().getResources().getColor(R.color.material_red);
                } else {
                    highlightColor = obtainColorPrimary(getContext());
                }
            }
            lineThickness = dimen_2dp;
            if (floatingLabel && getText().length() == 0) {
                labelAnimation = createFloationgLabelAnimation(getScrollX(), getBaseline(), getScrollX(), dimen_16dp + dimen_12sp, dimen_16sp, dimen_12sp, hintColor, highlightColor, 0);
                labelAnimation.start();
            } else {
                labelTextColor = highlightColor;
            }
            if (maxCharCount > 0) {
                drawCharCounter = true;
                if (charCount > maxCharCount) {
                    charCountTextColor = getContext().getResources().getColor(R.color.material_red);
                } else {
                    charCountTextColor = hintColor;
                }
            }
            if (iconResId > 0) {
                View view = getRootView().findViewById(iconResId);
                if (ImageView.class.isInstance(view)) {
                    ((ImageView)view).setColorFilter(highlightColor);
                }
            }
        } else {
            if (TextUtils.isEmpty(getError())) {
                if (maxCharCount > 0 && charCount > maxCharCount) {
                    highlightColor = getContext().getResources().getColor(R.color.material_red);
                } else {
                    highlightColor = Color.TRANSPARENT;
                }
            }
            lineThickness = dimen_1dp;
            if (floatingLabel && getText().length() == 0) {
                if (labelAnimation != null) {
                    labelAnimation.cancel();
                    labelAnimation = createFloationgLabelAnimation(labelX, labelY, getScrollX(), getBaseline(), labelTextSize, dimen_16sp, labelTextColor, hintColor, labelDurationOffset);
                    labelAnimation.start();
                } else {
                    labelAnimation = createFloationgLabelAnimation(getScrollX(), dimen_16dp + dimen_12sp, getScrollX(), getBaseline(), dimen_12sp, dimen_16sp, highlightColor, hintColor, 0);
                    labelAnimation.start();
                }
            } else {
                if (maxCharCount > 0 && charCount > maxCharCount) {
                    labelTextColor = getContext().getResources().getColor(R.color.material_red);
                } else {
                    labelTextColor = hintColor;
                }
            }
            if (maxCharCount > 0) {
                drawCharCounter = false;
            }
            if (iconResId > 0) {
                View view = getRootView().findViewById(iconResId);
                if (ImageView.class.isInstance(view)) {
                    ((ImageView)view).clearColorFilter();
                }
            }
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
            labelTextColor = getContext().getResources().getColor(R.color.material_red);
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

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        charCount = text.length();
        if (maxCharCount > 0) {
            if (drawCharCounter) {
                if (charCount > maxCharCount) {
                    highlightColor = getContext().getResources().getColor(R.color.material_red);
                    labelTextColor = getContext().getResources().getColor(R.color.material_red);
                } else {
                    highlightColor = obtainColorPrimary(getContext());
                    labelTextColor = obtainColorPrimary(getContext());
                }
            }
            if (charCount > maxCharCount) {
                charCountTextColor = getContext().getResources().getColor(R.color.material_red);
            } else {
                charCountTextColor = hintColor;
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

    private AnimatorSet createLineAnimation(float startA, float startB, float targetA, float targetB) {
        ValueAnimator offsetAnim = ValueAnimator.ofFloat(startB, targetB);
        offsetAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xA = (Float) animation.getAnimatedValue();
            }
        });
        ValueAnimator widthAnim = ValueAnimator.ofFloat(startA, targetA);
        widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xB = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(offsetAnim, widthAnim );
        return set;
    }

    private AnimatorSet createFloationgLabelAnimation(float startX, float startY, float targetX, float targetY, float startTextSize, float targetTextSize, int startColor, int targetColor, long durationOffset) {
        ValueAnimator xAnim = ValueAnimator.ofFloat(startX, targetX);
        xAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                labelX = (Float) animation.getAnimatedValue();
            }
        });
        ValueAnimator yAnim = ValueAnimator.ofFloat(startY, targetY);
        yAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                labelY = (Float) animation.getAnimatedValue();
            }
        });
        ValueAnimator textSizeAnim = ValueAnimator.ofFloat(startTextSize, targetTextSize);
        textSizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                labelTextSize = (Float) animation.getAnimatedValue();
                labelDurationOffset = animation.getCurrentPlayTime();
            }
        });
        ValueAnimator textColorAnim = ValueAnimator.ofInt(startColor, targetColor);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                labelTextColor = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(Math.max(0, 200 - durationOffset)); // Assure positive duration
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(xAnim, yAnim, textSizeAnim, textColorAnim);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setHintTextColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                labelDurationOffset = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        return set;
    }

    public int getCharCount() {
        return charCount;
    }

    public int getMaxCharCount() {
        return maxCharCount;
    }
}
