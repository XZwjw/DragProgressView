package com.xingzhe.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by JiaWangWang(行者) on 2019-07-05.
 * Email: 15829348578@163.com
 */
public class DrawProgressCircleView extends View {

    //图片宽
    private int mImgWidth;
    //默认图片宽
    private int mImgDefaultWidth;

    //图片id
    private int mDrawableId;

    //图片高
    private int mImgHeight;
    //默认图片高
    private int mImgDefaultHeight;

    //外边缘画笔
    private Paint mOutEdgePaint;
    //外边缘宽度
    private int mOutEdgeWidth;
    //外边缘默认宽度
    private int mOutEdgeDefaultWidth;

    //已加载进度条画笔
    private Paint mProgressLoadedPaint;

    //未加载进度条画笔
    private Paint mProgressUnLoadedPaint;

    //image与进度条内边缘之间的间距画笔
    private Paint mBetweenPaint;
    //image与进度条内边缘之间的间距
    private int mBetweenWidth;
    //image与进度条内边缘之间的默认间距
    private int mBetweenDefaultWidth;

    //外边缘画笔颜色
    private int mOutEdgeColor;
    //外边缘画笔默认颜色
    private int mOutEdgeDefaultColor;

    //已加载进度条画笔颜色
    private int mProgressLoadedColor;
    //已加载进度条画笔默认颜色
    private int mProgressLoadedDefaultColor;

    //未加载进度条画笔颜色
    private int mProgressUnLoadedColor;
    //未加载进度条画笔默认颜色
    private int mProgressUnLoadedDefaultColor;


    //image与进度条内边缘之间的间距画笔颜色
    private int mBetweenColor;
    //image与进度条内边缘之间的间距画笔默认颜色
    private int mBetweenDefaultColor;

    //进度条功能参数
    //进度条宽度
    private int mProgressWidth;
    private int mProgressDefaultWidth;
    private float mStartAngle;
    private static float mSweepAngle;

    private int mStartDefaultAngle = -90;
    private int mSweepDefaultAngle = 0;
    private int mDefaultProgressTime = 60;       //默认60s转一圈
    private float mOnePiece = 360f / mDefaultProgressTime;

    private Runnable mRunnable;
    private final Handler mHandler = new Handler();
    private int mStatus;    //进度条状态：加载中、停止
    private static final int STATUS_PENDING = 1;
    private static final int STATUS_LOAD = 2;
    private static final int STATUS_STOP = 3;

    //移动view
    float startX = 0;
    float startY = 0;
    float offsetX;
    float offsetY;

    private int mWidth;   //自定义view宽度

    private int mHeight;  //自定义view高度

    private int mMaxWidth;
    private int mMaxHeight;

    private int mStatusBarHeight;       //状态栏高
    private int mTitleBarHeight;        //标题栏高

    private int mStatusBarDefaultHeight;    //默认状态栏高
    private int mTitleBarDefaultHeight;     //默认标题栏高
    private boolean mHasTitleBar;       //  是否拥有标题栏

    public DrawProgressCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDefaultPaintColor();
        initCustomParams(context, attrs);
        initPaint();
        mStatus = STATUS_PENDING;

    }

    private void initCustomParams(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DrawProgressCircleView);

        if (array != null) {

            //view视图
            mImgWidth = array.getInteger(R.styleable.DrawProgressCircleView_imageWidth, mImgDefaultWidth);
            mImgHeight = array.getInteger(R.styleable.DrawProgressCircleView_imageHeight, mImgDefaultHeight);
            mBetweenWidth = array.getInteger(R.styleable.DrawProgressCircleView_betweenWidth, mBetweenDefaultWidth);
            mProgressWidth = array.getInteger(R.styleable.DrawProgressCircleView_progressWidth, mProgressDefaultWidth);
            mOutEdgeWidth = array.getInteger(R.styleable.DrawProgressCircleView_outEdgeWidth, mOutEdgeDefaultWidth);
            mDrawableId = array.getResourceId (R.styleable.DrawProgressCircleView_image, 0);
            //view进度条
            mStartAngle = array.getFloat(R.styleable.DrawProgressCircleView_startAngle, mStartDefaultAngle);
            mSweepAngle = array.getFloat(R.styleable.DrawProgressCircleView_sweepAngle, mSweepDefaultAngle);
            mBetweenColor = array.getColor(R.styleable.DrawProgressCircleView_betweenColor, mBetweenDefaultColor);
            mProgressLoadedColor = array.getColor(R.styleable.DrawProgressCircleView_progressLoadColor, mProgressLoadedDefaultColor);
            mProgressUnLoadedColor = array.getColor(R.styleable.DrawProgressCircleView_progressUnLoaderColor, mProgressUnLoadedDefaultColor);

            array.recycle();
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mMaxWidth = getMaxWidth(getContext());
        if (mHasTitleBar) {
            mMaxHeight = getMaxHeight(getContext()) - getStatusBarHeight() - getTitleBarHeight();
        } else {
            mMaxHeight = getMaxHeight(getContext()) - getStatusBarHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWidth = getWidth();
        mHeight = getHeight();

        canvas.translate(mWidth / 2,mHeight / 2);

        drawBetweenImageAndProgress(canvas);
        drawImage(canvas);
        drawProgress(canvas);
        drawOuterEdge(canvas);
    }

    private void initDefaultPaintColor() {
        mOutEdgeDefaultColor = Color.WHITE;
        mProgressLoadedDefaultColor = Color.RED;
        mProgressUnLoadedDefaultColor = Color.LTGRAY;
        mBetweenDefaultColor = Color.WHITE;
        mImgDefaultWidth = 100;
        mImgDefaultHeight =100;
        mBetweenDefaultWidth = 20;
        mProgressDefaultWidth = 10;
        mOutEdgeDefaultWidth = 5;

        mStartDefaultAngle = -90;
        mSweepDefaultAngle = 0;

        mStatusBarDefaultHeight = dpToPx(30);
        mTitleBarDefaultHeight = dpToPx(50);
        mHasTitleBar = true;
    }
    //start_____________初始化画笔____________

    private void initPaint() {
        initOuterEdgePaint();
        initProgressLoadedPaint();
        initProgressUnLoadedPaint();
        initDistanceBetweenImageAndProgressPaint();
    }

    private void initDistanceBetweenImageAndProgressPaint() {
        mBetweenPaint = new Paint();
        mBetweenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBetweenPaint.setStrokeWidth(mBetweenWidth);
        mBetweenPaint.setColor(mBetweenColor == 0 ? mBetweenDefaultColor : mBetweenColor);
    }

    private void initProgressUnLoadedPaint() {
        mProgressUnLoadedPaint = new Paint();
        mProgressUnLoadedPaint.setStyle(Paint.Style.STROKE);
        mProgressUnLoadedPaint.setStrokeWidth(mProgressWidth);
        mProgressUnLoadedPaint.setColor(mProgressUnLoadedColor == 0 ? mProgressUnLoadedDefaultColor : mProgressUnLoadedColor);
    }

    private void initProgressLoadedPaint() {
        mProgressLoadedPaint = new Paint();
        mProgressLoadedPaint.setStyle(Paint.Style.STROKE);
        mProgressLoadedPaint.setStrokeWidth(mProgressWidth);
        mProgressLoadedPaint.setColor(mProgressLoadedColor == 0 ? mProgressLoadedDefaultColor : mProgressLoadedColor);
    }


    private void initOuterEdgePaint() {
        mOutEdgePaint = new Paint();
        mOutEdgePaint.setStyle(Paint.Style.STROKE);
        mOutEdgePaint.setStrokeWidth(mOutEdgeWidth);
        mOutEdgePaint.setColor(mOutEdgeColor == 0 ? mOutEdgeDefaultColor : mOutEdgeColor);

    }

    //end_____________初始化画笔______________


    private void drawImage(Canvas canvas) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_draw_progress_circle);
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), mDrawableId);
        if (bitmap != null) {
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect(-mImgWidth / 2, -mImgHeight / 2, mImgWidth / 2, mImgHeight / 2);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(bitmap, src, dst, new Paint());
        }

    }

    //绘制图片与进度条之间的背景
    private void drawBetweenImageAndProgress(Canvas canvas) {
        float length = mImgWidth / 2f + mBetweenWidth / 2f;
        RectF rectF = new RectF(-length, -length, length, length);
        canvas.drawOval(rectF, mBetweenPaint);
    }

    private void drawProgress(Canvas canvas) {
        drawProgressUnLoaded(canvas);
        drawProgressLoaded(canvas);
    }

    private void drawProgressUnLoaded(Canvas canvas) {
        float length = mImgWidth / 2f + mBetweenWidth + mProgressWidth / 2f;
        RectF rectF = new RectF(-length, -length, length, length);

        canvas.drawOval(rectF, mProgressUnLoadedPaint);
    }

    private void drawProgressLoaded(Canvas canvas) {
        float length = mImgWidth / 2f + mBetweenWidth + mProgressWidth / 2f;
        RectF rectF = new RectF(-length, -length, length, length);
        Path path = new Path();
        path.addArc(rectF, mStartAngle, mSweepAngle);
        canvas.drawPath(path, mProgressLoadedPaint);
    }

    private void drawOuterEdge(Canvas canvas) {
        float length = mImgWidth / 2f + mBetweenWidth + mProgressWidth + mOutEdgeWidth / 2f;
        RectF rectF = new RectF(-length, -length, length, length);
        canvas.drawOval(rectF, mOutEdgePaint);
    }

    //start_________________进度条功能______________

    private void progressInit() {
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    mSweepAngle = (mSweepAngle + mOnePiece) % 360;
                    if (mStatus == STATUS_LOAD)  {
                        mHandler.postDelayed(this, 1000);
                        invalidate();
                    }
                }
            };
        }
    }

    public void progressContinue() {
        if (mStatus == STATUS_STOP) {
            mStatus = STATUS_LOAD;
            mHandler.post(mRunnable);
        }
    }

    public void restart() {
        mSweepAngle = 0;
        if (mStatus == STATUS_PENDING) {
            start();
        } else if (mStatus == STATUS_STOP) {
            mHandler.post(mRunnable);
            mStatus = STATUS_LOAD;
        }

    }

    public void start() {
        if (mStatus == STATUS_PENDING) {
            mStatus = STATUS_LOAD;
            progressInit();
            mHandler.post(mRunnable);
        } else if (mStatus == STATUS_STOP) {
            progressInit();
            progressContinue();
        }
    }

    public void stop() {
        mStatus = STATUS_STOP;
    }


    //end_________________进度条功能______________



    //start______________drag移动______________

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - startX;
                offsetY = event.getY() - startY;
                if (Math.abs(offsetX) > 3 || Math.abs(offsetY) > 3) {
                    int l,t,r,b;
                    l = (int) (getLeft() + offsetX);
                    r = l + mWidth;
                    t = (int) (getTop() + offsetY);
                    b = t + mHeight;

                    if (l < 0) {
                        l = 0;
                        r = l + mWidth;
                    }
                    if (r > mMaxWidth) {
                        r = mMaxWidth;
                        l = mMaxWidth - mWidth;
                    }

                    if (t < 0) {
                        t = 0;
                        b = t + mHeight;
                    }

                    if (b > mMaxHeight) {
                        b = mMaxHeight;
                        t = mMaxHeight - mHeight;
                    }

                    this.layout(l, t, r, b);
                }
               break;

        }
        return true;
    }

    private int getMaxWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    private int getMaxHeight(Context context) {

        return getDisplayMetrics(context).heightPixels;
    }

    //end______________drag移动______________


    public int getStatusBarHeight() {
        return mStatusBarHeight == 0 ? mStatusBarDefaultHeight : mStatusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.mStatusBarHeight = statusBarHeight;
    }

    public int getTitleBarHeight() {
        return mTitleBarHeight == 0 ? mTitleBarDefaultHeight : mTitleBarHeight;
    }

    public void setTitleBarHeight(int titleBarHeight) {
        this.mTitleBarHeight = titleBarHeight;
    }

    public void setHasTitleBar(boolean continueTitleBar) {
        this.mHasTitleBar = continueTitleBar;
    }

    public boolean getHasTitleBar() {
        return mHasTitleBar;
    }

    public int dpToPx(int dp){
        DisplayMetrics metrics = getDisplayMetrics(getContext());
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,metrics);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
