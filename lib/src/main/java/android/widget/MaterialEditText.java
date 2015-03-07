package android.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import fr.erictruong.materialedittext.lib.R;

public class MaterialEditText extends EditText {

    private final float DIMEN_1_DP  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
    private final float DIMEN_2_DP  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
    private final float DIMEN_8_DP  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
    private final float DIMEN_16_DP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
    private final float DIMEN_12_SP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    private final long DURATION_SHORT = getResources().getInteger(android.R.integer.config_shortAnimTime);

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

    // Unselected line
    private Paint unselectedLinePaint;
    private Path unselectedLine;
    private int unselectedColor;

    // Highlighted line
    private Paint highlightedLinePaint;
    private Path highlightedLine;
    private float highlightedLineThickness;
    private int highlightedColor;

    // Common line fields
    private PathEffect lineEffect;
    private DashPathEffect dashLineEffect;
    private float lineHeight;
    private AnimatorSet lineAnimation;
    private float lineLeftX, lineRightX;

    private int backgroundColor;

    // Error text
    private int errorColor;
    private CharSequence errorText;
    private TextPaint errorTextPaint;

    // Floating label
    private boolean floatingLabel;
    private TextPaint labelTextPaint;
    private float labelTextSize;
    private int labelTextColor;
    private AnimatorSet labelAnimation;
    private long labelAnimationElapsedDuration;
    private float labelX, labelY;

    // Character counter
    private int maxCharacters;
    private int charCount;
    private TextPaint charCountTextPaint;
    private int charCountTextColor;

    // Icon
    private int iconResId;
    private ImageView imgIcon;

    public MaterialEditText(Context context) {
        this(context, null);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        COLOR_HINT = getCurrentHintTextColor();
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Override API padding
        basePaddingLeft = 0;
        basePaddingTop = DIMEN_16_DP;
        basePaddingRight = 0;
        basePaddingBottom = DIMEN_16_DP;

        // Remove text top/bottom padding)
        setIncludeFontPadding(false);

        // Remove API background
        setBackgroundDrawable(null);

        // Initialize the unselected line
        unselectedLinePaint = new Paint();
        unselectedLinePaint.setStyle(Paint.Style.STROKE);
        unselectedLine = new Path();

        // Initialize the highlighted line
        highlightedLinePaint = new Paint();
        highlightedLinePaint.setStyle(Paint.Style.STROKE);
        highlightedLine = new Path();

        dashLineEffect = new DashPathEffect(new float[]{ Math.round(DIMEN_1_DP), Math.round(DIMEN_2_DP) }, 0);

        // 0btain XML attributes
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.materialEditText, 0, 0);
            backgroundColor = ta.getColor(R.styleable.materialEditText_backgroundColor, obtainColorAccent());
            errorColor = ta.getColor(R.styleable.materialEditText_errorColor, COLOR_MATERIAL_RED_500);
            floatingLabel = ta.getBoolean(R.styleable.materialEditText_floatingLabel, false) && !TextUtils.isEmpty(getHint());
            maxCharacters = ta.getInteger(R.styleable.materialEditText_maxCharacters, 0);
            iconResId = ta.getResourceId(R.styleable.materialEditText_withIcon, 0);
            ta.recycle();
        }

        errorTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        errorTextPaint.setTextSize(DIMEN_12_SP);

        labelTextSize = DIMEN_12_SP;
        labelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        if (floatingLabel) {
            basePaddingTop += DIMEN_8_DP + DIMEN_12_SP;
            labelX = 0;
            labelY = DIMEN_16_DP + DIMEN_12_SP;
        }

        charCountTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        charCountTextPaint.setTextSize(DIMEN_12_SP);
        charCountTextPaint.setTextAlign(Paint.Align.RIGHT);
        if (maxCharacters > 0) {
            basePaddingBottom += DIMEN_8_DP + DIMEN_12_SP;
        }

        updatePadding();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                lineLeftX = 0;
                lineRightX = getWidth();
            }
        });
    }

    private int obtainColorAccent() {
        Context context = getContext();
        int colorAccentId = getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        if (colorAccentId > 0) {
            TypedValue typedValue = new TypedValue();
            TypedArray a = context.getTheme().obtainStyledAttributes(typedValue.data, new int[]{ colorAccentId });
            int color = a.getColor(0, 0);
            a.recycle();
            return color;
        }
        return Color.BLACK;
    }

    private void updatePadding() {
        super.setPadding((int) (basePaddingLeft + paddingLeft), (int) (basePaddingTop + paddingTop), (int) (basePaddingRight + paddingRight), (int) (basePaddingBottom + paddingBottom));
    }

    private void updatePadding(int left, int top, int right, int bottom) {
        super.setPadding((int) (basePaddingLeft + paddingLeft) + left, (int) (basePaddingTop + paddingTop) + top, (int) (basePaddingRight + paddingRight) + right, (int) (basePaddingBottom + paddingBottom) + bottom);
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

        lineHeight = getHeight() - DIMEN_8_DP;
        charCountTextColor = COLOR_HINT;

        if (isFocused()) {
            highlightedLineThickness = DIMEN_2_DP;
            highlightedColor = backgroundColor;
            labelTextColor = backgroundColor;
            if (imgIcon != null) {
                imgIcon.setColorFilter(highlightedColor);
            }
        } else {
            highlightedLineThickness = DIMEN_1_DP;
            highlightedColor = Color.TRANSPARENT;
            labelTextColor = COLOR_HINT;
            if (imgIcon != null) {
                imgIcon.clearColorFilter();
            }
        }
        if (isEnabled()) {
            lineEffect = null;
            unselectedColor = COLOR_HINT;
        } else {
            lineEffect = dashLineEffect;
            unselectedColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
            labelTextColor = getTextColors().getColorForState(new int[]{ -android.R.attr.state_enabled }, 0);
        }

        if (!TextUtils.isEmpty(getError())) {
            lineHeight = getHeight() - (2 * DIMEN_8_DP + DIMEN_12_SP);
            errorTextPaint.setColor(errorColor);
            canvas.drawText(getError(), 0, getError().length(), getScrollX(), getHeight() - DIMEN_8_DP, errorTextPaint);

            unselectedColor = errorColor;
            highlightedColor = errorColor;
            labelTextColor = errorColor;
            charCountTextColor = errorColor;
        } else if (maxCharacters > 0) {
            lineHeight = getHeight() - (2 * DIMEN_8_DP + DIMEN_12_SP);

            if (charCount > maxCharacters) {
                unselectedColor = errorColor;
                highlightedColor = errorColor;
                labelTextColor = errorColor;
                charCountTextColor = errorColor;
            }

            if (charCount > maxCharacters || isFocused()) {
                charCountTextPaint.setColor(charCountTextColor);
                String text = charCount + " / " + maxCharacters;
                canvas.drawText(text, 0, text.length(), getScrollX() + getWidth(), getHeight() - DIMEN_8_DP, charCountTextPaint);
            }
        }

        // Draw the unselected line
        unselectedLinePaint.setColor(unselectedColor);
        unselectedLinePaint.setStrokeWidth(DIMEN_1_DP);
        unselectedLinePaint.setPathEffect(lineEffect);
        unselectedLine.reset();
        unselectedLine.moveTo(getScrollX(), lineHeight);
        unselectedLine.lineTo(getScrollX() + getWidth(), lineHeight);
        canvas.drawPath(unselectedLine, unselectedLinePaint);

        // Draw the highlighted line
        highlightedLinePaint.setColor(highlightedColor);
        highlightedLinePaint.setStrokeWidth(highlightedLineThickness);
        highlightedLinePaint.setPathEffect(lineEffect);
        highlightedLine.reset();
        highlightedLine.moveTo(getScrollX() + lineLeftX, lineHeight);
        highlightedLine.lineTo(getScrollX() + (lineAnimation != null && lineAnimation.isRunning() ? lineRightX : getWidth()), lineHeight);
        canvas.drawPath(highlightedLine, highlightedLinePaint);

        if (floatingLabel && (isFocused() || getText().length() > 0 || labelAnimation != null)) {
            labelTextPaint.setColor(labelTextColor);
            labelTextPaint.setTextSize(labelTextSize);
            canvas.drawText(getHint(), 0, getHint().length(), getScrollX() + labelX, labelY, labelTextPaint);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    @Override
    public void setError(CharSequence error) {
        errorText = error;
        if (!TextUtils.isEmpty(getError()) || maxCharacters > 0) {
            updatePadding(0, 0, 0, (int) (DIMEN_8_DP + DIMEN_12_SP));
        } else {
            setError(null, null);
            updatePadding();
        }
    }

    @Override
    public CharSequence getError() {
        return errorText;
    }

    @Override
    public CharSequence getHint() {
        // Hides the hint on view focused
        if (floatingLabel && isFocused()) {
            setHintTextColor(Color.TRANSPARENT);
        }
        return super.getHint();
    }

    public int getCharCount() {
        return charCount;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
        if (!TextUtils.isEmpty(getError()) || maxCharacters > 0) {
            updatePadding(0, 0, 0, (int) (DIMEN_8_DP + DIMEN_12_SP));
        } else {
            updatePadding();
        }
    }

    public int getMaxCharCount() {
        return maxCharacters;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isFocused()) {
                        float x = event.getX();
                        lineAnimation = createLineAnimation(x, x, 0, getWidth());
                        lineAnimation.start();
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            if (floatingLabel && getText().length() == 0) {
                labelAnimation = createLabelAnimation(getScrollX(), getBaseline(), getScrollX(), DIMEN_16_DP + DIMEN_12_SP, getTextSize(), DIMEN_12_SP, COLOR_HINT, highlightedColor, 0);
                labelAnimation.start();
            }
        } else {
            if (floatingLabel && getText().length() == 0) {
                if (labelAnimation != null) {
                    labelAnimation.cancel();
                    labelAnimation = createLabelAnimation(labelX, labelY, getScrollX(), getBaseline(), labelTextSize, getTextSize(), labelTextColor, COLOR_HINT, labelAnimationElapsedDuration);
                    labelAnimation.start();
                } else {
                    labelAnimation = createLabelAnimation(getScrollX(), DIMEN_16_DP + DIMEN_12_SP, getScrollX(), getBaseline(), DIMEN_12_SP, getTextSize(), backgroundColor, COLOR_HINT, 0);
                    labelAnimation.start();
                }
            }
        }
        if (imgIcon == null) {
            imgIcon = (ImageView) getRootView().findViewById(iconResId);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        charCount = text.length();
    }

    private AnimatorSet createLineAnimation(float startA, float startB, float targetA, float targetB) {
        ValueAnimator leftAnim = ValueAnimator.ofFloat(startA, targetA);
        leftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lineLeftX = (Float) animation.getAnimatedValue();
            }
        });
        ValueAnimator rightAnim = ValueAnimator.ofFloat(startB, targetB);
        rightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lineRightX = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION_SHORT);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(leftAnim, rightAnim);
        return set;
    }

    private AnimatorSet createLabelAnimation(float startX, float startY, float targetX, float targetY, float startTextSize, float targetTextSize, int startColor, int targetColor, long durationOffset) {
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
                labelAnimationElapsedDuration = animation.getCurrentPlayTime();
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
        set.setDuration(Math.max(0, DURATION_SHORT - durationOffset)); // Ensure positive duration
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(xAnim, yAnim, textSizeAnim, textColorAnim);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                labelAnimationElapsedDuration = 0;
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

    public void setErrorColor(int color) {
        this.errorColor = color;
    }

    public void setFloatingLabel(boolean enabled) {
        if (!floatingLabel && enabled) {
            basePaddingTop += DIMEN_8_DP + DIMEN_12_SP;
            labelX = 0;
            labelY = DIMEN_16_DP + DIMEN_12_SP;
        } else if (floatingLabel && !enabled) {
            basePaddingTop -= DIMEN_8_DP + DIMEN_12_SP;
        }
        this.floatingLabel = enabled;
        if (!TextUtils.isEmpty(getError()) || maxCharacters > 0) {
            updatePadding(0, 0, 0, (int) (DIMEN_8_DP + DIMEN_12_SP));
        } else {
            updatePadding();
        }
    }
}
