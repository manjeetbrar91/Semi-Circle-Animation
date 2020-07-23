package com.mj.semicircleanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

public class SemiCircleAnimationView extends FrameLayout {
    private final int POSITION_TOP_LEFT = 1;
    private final int POSITION_TOP_RIGHT = 2;
    private final int POSITION_BOTTOM_LEFT = 3;
    private final int POSITION_BOTTOM_RIGHT = 4;

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private Paint circlePaint;
    private Paint innerPaint;
    private Context context;
    private int position = POSITION_TOP_RIGHT;

    public SemiCircleAnimationView(Context context) {
        super(context);
        this.context = context;

    }

    public SemiCircleAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public SemiCircleAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public SemiCircleAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);

    }

    private float strokeWidth = 5f;
    private int circleBorderColor = Color.BLACK;
    private int innerCircleColor = Color.GREEN;
    private boolean reverse = false;
    private boolean repeat = false;

    public void setCircleBorderColor(int circleBorderColor) {
        this.circleBorderColor = circleBorderColor;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    private boolean readStyleParameters(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.SemiCircleAnimation);
        try {
            strokeWidth = a.getDimension(R.styleable.SemiCircleAnimation_primary_circle_border_width, dpToPx(1));
            duration = a.getInt(R.styleable.SemiCircleAnimation_anim_duration, 2000);
            subViewWidth = (int) a.getDimension(R.styleable.SemiCircleAnimation_animated_circle_width, dpToPx(16));
            subViewColor = a.getColor(R.styleable.SemiCircleAnimation_animated_circle_color, Color.argb(255, 152, 0, 1));

            position = a.getInt(R.styleable.SemiCircleAnimation_position, POSITION_TOP_RIGHT);
            circleBorderColor = a.getColor(R.styleable.SemiCircleAnimation_primary_circle_border_color, Color.BLACK);
            innerCircleColor = a.getColor(R.styleable.SemiCircleAnimation_inner_circle_color, Color.BLACK);
            reverse = a.getBoolean(R.styleable.SemiCircleAnimation_anim_reverse, false);
            repeat = a.getBoolean(R.styleable.SemiCircleAnimation_anim_repeat, false);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            a.recycle();
        }
    }

    void init(AttributeSet attrs) {

        if (!readStyleParameters(context, attrs)) {
            strokeWidth = dpToPx(1);
            subViewWidth = dpToPx(16);
        }

        this.setWillNotDraw(false);
        circlePaint = new Paint();
        innerPaint = new Paint();
        circlePaint.setStrokeWidth(strokeWidth);
        circlePaint.setColor(circleBorderColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        innerPaint.setColor(innerCircleColor);
        innerPaint.setAntiAlias(true);
        innerPaint.setStyle(Paint.Style.FILL);
        addSubView();
    }

    /**
     * float left = 0 + strokeWidth + (subViewWidth / 2);
     * float top = -getHeight();
     * float right = getWidth() + getWidth();
     * float bottom = getHeight() - strokeWidth - ((subViewWidth / 2));
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOuterCircle(canvas);


    }

    private void drawInnerCircle() {


//        canvas.drawArc(innerLeft, innerTop, innerRight, innerBottom, 0, 360, false, innerPaint);
    }

    private void drawOuterCircle(Canvas canvas) {
        float left = 0, top = 0, right = 0, bottom = 0;
        float innerLeft = 0, innerTop = 0, innerRight = 0, innerBottom = 0;
        if (position == POSITION_TOP_LEFT) {
            left = -getWidth();
            top = -getHeight();
            right = getWidth() - (0 + strokeWidth + (subViewWidth / 2));
            bottom = getHeight() - strokeWidth - ((subViewWidth / 2));


            innerLeft = left + (getWidth() / 6);
            innerTop = (getWidth() / 6) - getHeight();
            innerRight = right - (getWidth() / 6);
            innerBottom = bottom - (getHeight() / 6);//

        }
        if (position == POSITION_TOP_RIGHT) {
            left = 0 + strokeWidth + (subViewWidth / 2);
            top = -getHeight();
            right = getWidth() + getWidth();
            bottom = getHeight() - strokeWidth - ((subViewWidth / 2));

            innerLeft = left + (getWidth() / 6);
            innerTop = (getWidth() / 6) - getHeight();
            innerRight = right - (getWidth() / 6);
            innerBottom = bottom - (getHeight() / 6);//
        }

        if (position == POSITION_BOTTOM_LEFT) {
            left = -getWidth();
            top = 0 + (strokeWidth + ((subViewWidth / 2)));
            right = getWidth() - (0 + strokeWidth + (subViewWidth / 2));
            bottom = getHeight() + getHeight();


            innerLeft = left + (getWidth() / 6);
            innerTop = top + (getWidth() / 6);
            innerRight = right - (getWidth() / 6);
            innerBottom = bottom - (getHeight() / 6);//
        }
        canvas.drawArc(left, top, right, bottom, startAngle(), endAngle(), false, circlePaint);
        canvas.drawArc(innerLeft, innerTop, innerRight, innerBottom, 0, 360, false, innerPaint);
    }

    private Path getAnimationPath() {
        float left = strokeWidth;
        float top = -getHeight();
        float right = getWidth() + getWidth();
        float bottom = getHeight() - strokeWidth - (subViewWidth);

        if (position == POSITION_TOP_LEFT) {
            left = -getWidth();
            top = -getHeight();
            right = getWidth() - strokeWidth - (subViewWidth);//+ getWidth();
            bottom = getHeight() - strokeWidth - strokeWidth - (subViewWidth);
        }
        if (position == POSITION_TOP_RIGHT) {
            left = strokeWidth;
            top = -getHeight();
            right = getWidth() + getWidth() - strokeWidth - (subViewWidth);
            bottom = getHeight() - strokeWidth - strokeWidth - (subViewWidth);
        }
        if (position == POSITION_BOTTOM_LEFT) {
            left = -getWidth();
            top = strokeWidth;
            right = getWidth() - strokeWidth - strokeWidth - (subViewWidth);//+ getWidth();
            bottom = getHeight() + getHeight() - strokeWidth - subViewWidth;
        }
        Path path = new Path();
        path.arcTo(left, top, right, bottom, startAngle(), endAngle(), true); //with first four parameters you determine four edge of a rectangle by pixel , and fifth parameter is the path'es start point from circle 360 degree and sixth parameter is end point of path in that circle
        return path;
    }

    int startAngle() {
        switch (position) {
            case POSITION_TOP_LEFT: {
                if (reverse) {
                    return 90;
                }
                return 0;
            }
            case POSITION_TOP_RIGHT: {
                if (reverse) {
                    return 90;
                }
                return 180;
            }
            case POSITION_BOTTOM_LEFT: {
                if (reverse) {
                    return 360;
                }
                return 270;

            }
            case POSITION_BOTTOM_RIGHT: {
                break;
            }
        }

        return 0;
    }

    int endAngle() {
        switch (position) {
            case POSITION_TOP_LEFT: {
                if (reverse) {
                    return -90;
                }
                return 90;
            }
            case POSITION_TOP_RIGHT: {
                if (reverse) {
                    return 90;
                }
                return -90;
            }
            case POSITION_BOTTOM_LEFT: {
                if (reverse) {
                    return -90;
                }
                return 90;

            }
            case POSITION_BOTTOM_RIGHT: {
                break;
            }
        }

        return 0;
    }


    View v;
    private int subViewWidth = 50;
    private int subViewColor = Color.argb(255, 152, 0, 1);


    private void addSubView() {
        v = new View(context);
        Drawable drawable = context.getResources().getDrawable(R.drawable.circel_bg);
        drawable.setTint(subViewColor);
        v.setBackgroundResource(R.drawable.circel_bg);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subViewWidth, subViewWidth);
        v.setLayoutParams(params);
        this.addView(v);


    }

    private ObjectAnimator animator;
    private int duration = 2000;

    public void start() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float left = strokeWidth;
                float top = -getHeight();
                float right = getWidth() + getWidth();
                float bottom = getHeight() - strokeWidth - (subViewWidth);

                Path path = getAnimationPath();//new Path();
//                path.arcTo(left, top, right, bottom, startAngle(), endAngle(), true); //with first four parameters you determine four edge of a rectangle by pixel , and fifth parameter is the path'es start point from circle 360 degree and sixth parameter is end point of path in that circle
                animator = ObjectAnimator.ofFloat(v, View.X, View.Y, path); //at first parameter (view) put the target view
                animator.setDuration(duration);
                animator.setRepeatCount(9999999);
                if (repeat) {
                    animator.setRepeatMode(ValueAnimator.REVERSE);
                }
//animator.setRepeatMode(ValueAnimator.RESTART);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        int repeatCount = animator.getRepeatCount();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        int repeatCount = animator.getRepeatCount();
                    }
                });
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    }
                });
                animator.start();
            }
        });

    }

    public void stop() {
        if (animator != null && animator.isRunning()) {
            animator.setRepeatCount(0);
            try {
                animator.cancel();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            stop();
        } catch (Exception ignored) {
        }
    }

}
