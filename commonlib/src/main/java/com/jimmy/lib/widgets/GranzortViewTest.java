package com.jimmy.lib.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jimmy.lib.R;
import com.jimmy.lib.utils.LogUtils;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

/**
 * Created by chenjiaming1 on 2017/9/13.
 */

public class GranzortViewTest extends View implements QMUIPullRefreshLayout.IRefreshView {

    private Paint paint;

    private Path innerCircle;//内圆 path
    private Path outerCircle;//外圆 path
    private Path trangle1;//第一个三角形的 Path
    private Path trangle2;//第二个三角形的 Path
    private Path drawPath;//用于截取路径的 Path

    private PathMeasure pathMeasure;

    private float mViewWidth;
    private float mViewHeight;

    private long duration = 1500;
    private ValueAnimator valueAnimator;

    private Handler mHanlder;

    private float distance;//当前动画执行的百分比取值为0-1
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener;
    private Animator.AnimatorListener animatorListener;

    private State mCurrentState = State.CIRCLE_STATE;

    private static final int CIRCLE_DIAMETER_LARGE = 56;

    //三个阶段的枚举
    private enum State {
        INIT_STATE,
        CIRCLE_STATE,
        TRANGLE_STATE,
        FINISH_STATE
    }

    public GranzortViewTest(Context context) {
        this(context, null);
    }

    public GranzortViewTest(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GranzortViewTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(160, 160);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        LogUtils.e("mViewWidth", mViewWidth);
        LogUtils.e("mViewHeight", mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(getResources().getColor(R.color.colorPrimary));
        canvas.save();
//        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        canvas.translate(80, 80);
        switch (mCurrentState) {
            case CIRCLE_STATE:
                drawPath.reset();
                pathMeasure.setPath(innerCircle, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                pathMeasure.setPath(outerCircle, false);
                drawPath.reset();
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                break;
            case TRANGLE_STATE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);
                drawPath.reset();
                pathMeasure.setPath(trangle1, false);
                float stopD = distance * pathMeasure.getLength();
                float startD = stopD - (0.5f - Math.abs(0.5f - distance)) * 200;
                pathMeasure.getSegment(startD, stopD, drawPath, true);
                canvas.drawPath(drawPath, paint);
                drawPath.reset();
                pathMeasure.setPath(trangle2, false);
                pathMeasure.getSegment(startD, stopD, drawPath, true);
                canvas.drawPath(drawPath, paint);
                break;
            case FINISH_STATE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);
                drawPath.reset();
                pathMeasure.setPath(trangle1, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                drawPath.reset();
                pathMeasure.setPath(trangle2, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                break;

        }

        canvas.restore();

    }

    private void init() {

        initPaint();

        initPath();

        initHandler();

        initAnimatorListener();

        initAnimator();

        mCurrentState = State.INIT_STATE;
//        valueAnimator.start();

    }

    private void initHandler() {
        mHanlder = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (mCurrentState) {
//                    case CIRCLE_STATE:
//                        mCurrentState = State.TRANGLE_STATE;
//                        valueAnimator.start();
//                        break;
                    case TRANGLE_STATE:
                        mCurrentState = State.FINISH_STATE;
                        valueAnimator.start();
                        break;
                }
            }
        };
    }

    public void setOffset(float dis) {
        mCurrentState = State.CIRCLE_STATE;
        distance = dis;

        invalidate();
    }

    public void start() {
        mCurrentState = State.TRANGLE_STATE;
        valueAnimator.start();
    }

    private void initAnimatorListener() {
        animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("star:", mCurrentState + "_");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("end:", mCurrentState + "_");
                mHanlder.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);

        valueAnimator.addUpdateListener(animatorUpdateListener);

        valueAnimator.addListener(animatorListener);
    }

    private void initPath() {
        innerCircle = new Path();
        outerCircle = new Path();
        trangle1 = new Path();
        trangle2 = new Path();
        drawPath = new Path();

        pathMeasure = new PathMeasure();

        RectF innerRect = new RectF(-55, -55, 55, 55);
        RectF outerRect = new RectF(-70, -70, 70, 70);
        innerCircle.addArc(innerRect, 150, -359.9F);     // 不能取360f，否则可能造成测量到的值不准确
        outerCircle.addArc(outerRect, 60, -359.9F);

        pathMeasure.setPath(innerCircle, false);

        float[] pos = new float[2];
        pathMeasure.getPosTan(0, pos, null);        // 获取开始位置的坐标
        trangle1.moveTo(pos[0], pos[1]);
        pathMeasure.getPosTan((1f / 3f) * pathMeasure.getLength(), pos, null);
        System.out.println("pos : " + pos[0] + "  " + pos[1]);

        trangle1.lineTo(pos[0], pos[1]);
        pathMeasure.getPosTan((2f / 3f) * pathMeasure.getLength(), pos, null);
        trangle1.lineTo(pos[0], pos[1]);
        trangle1.close();

        pathMeasure.getPosTan((2f / 3f) * pathMeasure.getLength(), pos, null);
        Matrix matrix = new Matrix();
        matrix.postRotate(-180);
        trangle1.transform(matrix, trangle2);
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.5f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setShadowLayer(4.5f, 0, 0, getResources().getColor(R.color.colorPrimary));//白色光影效果
    }

    @Override
    public void stop() {

    }

    @Override
    public void doRefresh() {
        start();
    }

    @Override
    public void onPull(int offset, int total, int overPull) {
        float a = (float) offset / (float)total;
        setOffset(a);
    }
}
