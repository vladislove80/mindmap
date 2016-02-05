package app.com.myapp.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;



public class ZoomLayout extends FrameLayout implements
        ScaleGestureDetector.OnScaleGestureListener{
    public interface OnScaleChangedListener {
        public void onScaleChanged(float scale);
    }

    private enum Mode {
        NONE, DRAG, ZOOM
    }

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private Mode mode = Mode.NONE;
    public float scale = 1.0f;
    private float prevScale = 1.0f;

    public static float realX = 0f;
    public static float realY = 0f;
    MotionEvent realEvent = null;

    private float lastScaleFactor = 0f;

    // Where the finger first touches the screen
    //private float startX = 0f;
    //private float startY = 0f;

    //private float prevDx = 0f;
    //private float prevDy = 0f;

    private int screenWidth;
    private int screenHeight;
    private float topHeight;

    private float startXX = 0;
    private float startYY = 0;

    public MyRelativeLayout rootLayout;

    private ScaleGestureDetector scaleDetector;
    private GestureDetector mGestureDetector;
    private OnLongClickListener mOnLongClickListener;
    private OnClickListener mOnClickListener;
    private OnScaleChangedListener mOnScaleChangedListener;

    private boolean isTouchBlocked = false;

    public ZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        mOnLongClickListener = l;
    }

    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    public void setOnScaleChangedListener(OnScaleChangedListener l) {
        mOnScaleChangedListener = l;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        this.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        this.setVerticalScrollBarEnabled(false);

        scaleDetector = new ScaleGestureDetector(
                context, this);
        mGestureDetector = new GestureDetector(
                getContext(), new GestureListener(), null, true);

        rootLayout = new MyRelativeLayout(context);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.addView(rootLayout, lp);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //topHeight = MetricsModule.getPixelFromDip(48, this.getContext());

        //rootLayout.setOnTouchListener(this);
    }

    public void destroy() {
        scaleDetector = null;
        mGestureDetector = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        screenWidth = (int)(getMeasuredWidth() * getScaleY());
        screenHeight = (int)(getMeasuredHeight() * getScaleY());
    }

    @Override
    public void addView(View child) {
        child().addView(child);
    }

    @Override
    public void addView(View child, int index) {
        throw new RuntimeException("Use addView(View child)");
    }

    @Override
    public void removeView(View view) {
        child().removeView(view);
    }

    public void setIsTouchBlocked(boolean value) {
        isTouchBlocked = value;
    }

	/*@Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
		return super.onInterceptTouchEvent(motionEvent);
    }*/

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float diffX = 0;
        float diffY = 0;

        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(motionEvent);
        }

        if (isTouchBlocked) return true;

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "DOWN");

                if (mode == Mode.ZOOM) {
                    startXX = getXPosition();
                    startYY = getYPosition();
                } else {
                    mode = Mode.DRAG;
                    startXX = motionEvent.getX();
                    startYY = motionEvent.getY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == Mode.ZOOM) {
                    diffX = getXPosition() - startXX;
                    diffY = getYPosition() - startYY;

                } else if (mode == Mode.DRAG) {
                    diffX = motionEvent.getX() - startXX;
                    diffY = motionEvent.getY() - startYY;
                } else {

                }
                break;

		/*case MotionEvent.ACTION_POINTER_DOWN:
			mode = Mode.ZOOM;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			mode = Mode.DRAG;
			break;*/

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "UP");
                mode = Mode.NONE;
                diffX = 0;
                diffY = 0;
                break;
        }

        if (motionEvent.getPointerCount() >= 2 && scaleDetector != null) {
            scaleDetector.onTouchEvent(motionEvent);
        }

        if (mode == Mode.DRAG
                || mode == Mode.ZOOM) {
            applyScaleAndTranslation(diffX, diffY);

            if (mode == Mode.ZOOM) {
                startXX = getXPosition();
                startYY = getYPosition();
            } else {
                startXX = motionEvent.getX();
                startYY = motionEvent.getY();
            }
        }

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleBegin");
        mode = Mode.ZOOM;
        prevScale = scale;

        float newX = (-getXPosition() + scaleDetector.getFocusX() * getScaleX()) / child().getScaleX() / getScaleX();
        float newY = (-getYPosition() + scaleDetector.getFocusY() * getScaleY()) / child().getScaleY() / getScaleY();

        child().setTranslationX(child().getTranslationX() + (child().getPivotX() - newX) * (1 - child().getScaleX()));
        child().setTranslationY(child().getTranslationY() + (child().getPivotY() - newY) * (1 - child().getScaleY()));

        child().setPivotX(newX);
        child().setPivotY(newY);

        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        if (lastScaleFactor > 1 && scaleFactor < 1) scaleFactor = 1f;
        if (lastScaleFactor < 1 && scaleFactor > 1) scaleFactor = 1f;

        Log.i(TAG, "onScale " + scaleFactor);
        if (lastScaleFactor == 0
                || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(scale, MIN_ZOOM);
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleEnd");
        mode = Mode.NONE;
        scale = Math.min(scale, MAX_ZOOM);
        applyScaleAndTranslation(0, 0);

        if (mOnScaleChangedListener != null && prevScale != scale) {
            mOnScaleChangedListener.onScaleChanged(scale);
        }
    }

    public float getRealWidth() {
        return child().getWidth() * child().getScaleX();
    }

    public float getRealHeight() {
        return child().getHeight() * child().getScaleY();
    }

    public int getXPosition() {
        int originalPos[] = new int[2];
        child().getLocationOnScreen(originalPos);
        return originalPos[0];
    }

    public int getYPosition() {
        int originalPos[] = new int[2];
        child().getLocationOnScreen(originalPos);
        return originalPos[1];
    }

    public void applyScaleAndTranslation(float diffX, float diffY) {

        child().setScaleX(scale);
        child().setScaleY(scale);

        int cX = getXPosition();
        int cY = getYPosition();

        if (cX + diffX > 0) {
            diffX = -cX;
            Log.d(TAG, "X overscroll 1 - new diffX = " + diffX);
            getParent().requestDisallowInterceptTouchEvent(false);
        } else {
            int dx = screenWidth - (int)getRealWidth();
            if (dx - diffX > cX) {
                diffX = -cX + dx;
                Log.d(TAG, "X overscroll 2 - new diffX = " + diffX + " dx = " + dx);
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        int dy = screenHeight - (int)getRealHeight();
        if (dy > 0 || cY + diffY - topHeight > 0) {
            diffY = -cY + topHeight;
            Log.d(TAG, "Y overscroll 1 - new diffY = " + diffY);
        } else {
            if (dy - diffY > cY) {
                diffY = -cY + dy;
                Log.d(TAG, "Y overscroll 2 - new diffY = " + diffY + " dy = " + dy);
            }
        }

        child().setTranslationX(diffX + child().getTranslationX());
        child().setTranslationY(diffY + child().getTranslationY());
    }

    public void applyScaleAndTranslation() {
        applyScaleAndTranslation(0, 0);
    }

    public MyRelativeLayout child() {
        return rootLayout;
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(ZoomLayout.this);
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i(TAG, "Double Tap detected!");

            if (isTouchBlocked) return false;

            float newX = (-getXPosition() + e.getX() * getScaleX()) / child().getScaleX() / getScaleX();
            float newY = (-getYPosition() + e.getY() * getScaleY()) / child().getScaleY() / getScaleY();

            child().setTranslationX(child().getTranslationX() + (child().getPivotX() - newX) * (1 - child().getScaleX()));
            child().setTranslationY(child().getTranslationY() + (child().getPivotY() - newY) * (1 - child().getScaleY()));

            child().setPivotX(newX);
            child().setPivotY(newY);

            if (scale == MAX_ZOOM) {
                scale = MIN_ZOOM;
            } else {
                scale = Math.min(scale + 1.5f, MAX_ZOOM);
            }

            applyScaleAndTranslation(0, 0);

            if (mOnScaleChangedListener != null) {
                mOnScaleChangedListener.onScaleChanged(scale);
            }

            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isTouchBlocked) return;

            if (mOnLongClickListener != null) {
                mOnLongClickListener.onLongClick(ZoomLayout.this);
            }
            super.onLongPress(e);
        }

    }

    public class MyRelativeLayout extends RelativeLayout {

        //RelativeLayout subLayout;

        public MyRelativeLayout(Context context) {
            super(context);
		/*
			subLayout = new RelativeLayout(context);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			addView(subLayout, lp);*/
        }

		/*@Override
		public void addView(View child) {
			subLayout.addView(child);
		}

		@Override
		public void removeView(View view) {
			subLayout.removeView(view);
		}*/

        public void onTouch(MotionEvent event) {
            ZoomLayout.this.onTouchEvent(event);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            realX = event.getX();
            realY = event.getY();

            realEvent = event;

            return super.dispatchTouchEvent(event);
        }

    }

}


