package test.starshine.com.demowaterripples.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * water ripples
 *
 * @author huyongsheng
 */

public class WaterRipplesView extends View {

    /**
     * the paint
     */
    private Paint mPaint = null;

    /**
     * the width of the paint
     */
    private int mPaintWidth = 3;

    /**
     * the location X of the water ripples
     */
    private int x = -1;

    /**
     * the location Y of the water ripples
     */
    private int y = -1;

    /**
     * the multiply speed of the water ripples
     * when multiply > 1.0f,the water ripples spread from fast to slow
     * when multiply < 1.0f,the water ripples spread from slow to fast
     */
    private float multiply = 2.0f;

    /**
     * the max radius of the water ripples
     */
    private int mMaxRadius = 100;

    /**
     * the min radius of the water ripples
     */
    private int mMinRadius = 40;

    /**
     * the time during a ripple appear to disappear
     */
    private int mDuringTime = 3000;

    /**
     * the time between two ripples appear
     */
    private int mNewTime = 1000;

    /**
     * the time to refresh screen
     */
    private int mRefreshTime = 50;

    /**
     * when radius equal this value, the alpha of paint is 100
     * the bigger Math.abs(radius - this) the smaller paint's alpha
     */
    private int mTransitionRadius = 80;

    /**
     * the paint's alpha when a new ripple appear
     */
    private int mBeginTransparent = 128;

    /**
     * the paint's alpha when a new ripple's radius equal the mTransitionRadius
     */
    private int mTransitionTransparent = 255;

    /**
     * the paint's alpha when a new ripple disappear
     */
    private int mEndTransparent = 0;

    /**
     * if true, when you touch the view,the x and y will reset to the point you touch
     */
    private boolean canTouch = true;

    /**
     * do not change following variables,they will be calculate by above variables
     */
    private float speed;
    private float accelerated;
    private List<Integer> mRadiusList;
    private int mTimes;
    private int mCycleTimes;
    private int a1;
    private int b1;
    private int a2;
    private int b2;
    private Handler mHandler;
    private Runnable mRunnable;

    public WaterRipplesView(Context context) {
        super(context);
    }

    public WaterRipplesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start(){
        if (mPaint == null){
            // create the paint
            mPaint = new Paint();
            mPaint.setStrokeWidth(mPaintWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            // AntiAlias
            mPaint.setAntiAlias(true);
            mPaint.setFilterBitmap(true);
        }
        if (x < 0 || x > getHeight()){
            x = getHeight() / 2;
        }
        if (y < 0 || y > getWidth()){
            y = getWidth() / 2;
        }
        // calculate the times
        mCycleTimes = mNewTime / mRefreshTime;
        // set the radius
        if (mRadiusList == null) {
            mRadiusList = new ArrayList<Integer>();
        }
        // calculate the speed of ripple's spread
        speed = (float) (mMaxRadius - mMinRadius) / (float) mDuringTime;
        // multiply the speed
        speed = speed * multiply;
        // calculate the accelerate by formula : s = s0 + vt + 1/2*at*t
        accelerated = (float) ((mMaxRadius - mMinRadius - speed * mDuringTime) * 2 / Math.pow(mDuringTime, 2));
        // set transition radius
        if (mTransitionRadius > mMaxRadius || mTransitionRadius < mMinRadius){
            mTransitionRadius = (int) (0.8 * (mMaxRadius + mMinRadius) / 2);
        }
        // calculate the constant in transparent variety formula
        a1 = (mTransitionTransparent - mBeginTransparent) / mTransitionRadius;
        b1 = mTransitionTransparent - a1 * mTransitionRadius;
        a2 = (mEndTransparent - mTransitionTransparent) / (mMaxRadius - mTransitionRadius);
        b2 = mTransitionTransparent - a2 * mTransitionRadius;
        // init handler
        if (mHandler == null) {
            mHandler = new Handler();
        }
        // init runnable
        if (mRunnable == null) {
            mRunnable = new WaterRipplesRunnable();
        }
        // start thread to animate the water ripples
        mHandler.post(mRunnable);
    }

    public void stop(){
        mRadiusList.clear();
        mHandler.removeCallbacks(mRunnable);
    }

    public WaterRipplesView paint(Paint paint){
        mPaint = paint;
        return this;
    }

    public WaterRipplesView paintWidth(int paintWidth) {
        mPaintWidth = paintWidth;
        return  this;
    }

    public WaterRipplesView x(int x) {
        this.x = x;
        return  this;
    }

    public WaterRipplesView y(int y) {
        this.y = y;
        return  this;
    }

    public WaterRipplesView multiply(float multiply) {
        this.multiply = multiply;
        return  this;
    }

    public WaterRipplesView maxRadius(int mMaxRadius) {
        this.mMaxRadius = mMaxRadius;
        return  this;
    }

    public WaterRipplesView minRadius(int mMinRadius) {
        this.mMinRadius = mMinRadius;
        return  this;
    }

    public WaterRipplesView duringTime(int mDuringTime) {
        this.mDuringTime = mDuringTime;
        return  this;
    }

    public WaterRipplesView newTime(int mNewTime) {
        this.mNewTime = mNewTime;
        return  this;
    }

    public WaterRipplesView refreshTime(int mRefreshTime) {
        this.mRefreshTime = mRefreshTime;
        return  this;
    }

    public WaterRipplesView transparentTransitionRadius(int mTransparentTransitionRadius) {
        this.mTransitionRadius = mTransparentTransitionRadius;
        return  this;
    }

    public WaterRipplesView beginTransparent(int mBeginTransparent) {
        this.mBeginTransparent = mBeginTransparent;
        return  this;
    }

    public WaterRipplesView transparentTransition(int transparentTransition) {
        this.mTransitionTransparent = transparentTransition;
        return  this;
    }

    public WaterRipplesView endTransparent(int endTransparent) {
        this.mEndTransparent = endTransparent;
        return  this;
    }

    public WaterRipplesView backgroundResource(int resId){
        setBackgroundResource(resId);
        return  this;
    }

    public WaterRipplesView backgroundColor(int color){
        setBackgroundColor(color);
        return  this;
    }

    public WaterRipplesView canTouch(boolean canTouch){
        this.canTouch = canTouch;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRadiusList != null) {
            for (int radius : mRadiusList) {
                if (radius > 0) {
                    // calculate the transparent by radius
                    int alpha;
                    if (radius <= mTransitionRadius) {
                        alpha = a1 * radius + b1;
                    } else {
                        alpha = a2 * radius + b2;
                    }
                    // set color and transparent of paint
                    mPaint.setARGB(alpha, 255, 255, 255);
                    // draw the ripple
                    canvas.drawCircle(x, y, radius, mPaint);
                }
            }
        }
    }

    /**
     * touch listener
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (canTouch) {
            x = (int) event.getX();
            y = (int) event.getY();
            // draw
            postInvalidate();
        }
        // end this touch
        return true;
    }

    private class WaterRipplesRunnable implements Runnable {
        @Override
        public void run() {
            try {
                int currentTime = mRefreshTime * mTimes;
                if (currentTime >= mDuringTime) {
                    mRadiusList.remove(0);
                    mTimes -= mCycleTimes;
                    currentTime -= mNewTime;
                }
                if (currentTime == 0 || currentTime / mNewTime >= mRadiusList.size()){
                    mRadiusList.add(mMinRadius);
                }
                for (int i = 0; i < mRadiusList.size() ; i++){
                    mRadiusList.set(i, (int) (speed * (currentTime - i * mNewTime) + 0.5 * accelerated * Math.pow((currentTime - i * mNewTime), 2)) + mMinRadius);
                }
                Thread.sleep(mRefreshTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mTimes++;
            postInvalidate();
            mHandler.post(this);
        }
    }
}
