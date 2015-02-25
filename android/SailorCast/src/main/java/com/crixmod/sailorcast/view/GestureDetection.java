package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by fire3 on 2015/2/25.
 */
public class GestureDetection extends GestureDetector.SimpleOnGestureListener {

    public final static int SWIPE_UP = 1;
    public final static int SWIPE_DOWN = 2;
    public final static int SWIPE_LEFT = 3;
    public final static int SWIPE_RIGHT = 4;

    public final static int MODE_SOLID = 1;
    public final static int MODE_DYNAMIC = 2;

    private final static int ACTION_FAKE = -13; // just an unlikely number
    private int swipe_Min_Distance = 50;
    private int swipe_Max_Distance = 350;
    private int swipe_Min_Velocity = 100;

    private int mode = MODE_DYNAMIC;
    private boolean running = true;
    private boolean tapIndicator = false;

    private Activity context;

    private GestureDetector detector;
    private SimpleGestureListener listener;

    public GestureDetection(Activity context, SimpleGestureListener sgl) {

        this.context = context;
        this.detector = new GestureDetector(context, this);
        this.listener = sgl;
    }

    public void onTouchEvent(MotionEvent event) {

        if (!this.running)
            return;

        boolean result = this.detector.onTouchEvent(event);

        if (this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (this.mode == MODE_DYNAMIC) {

            if (event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if (this.tapIndicator) {
                event.setAction(MotionEvent.ACTION_DOWN);
                this.tapIndicator = false;
            }

        }
        // else just do nothing, it's Transparent
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        final float xDistance = Math.abs(e1.getX() - e2.getX());
        final float yDistance = Math.abs(e1.getY() - e2.getY());

        if (xDistance > this.swipe_Max_Distance
                || yDistance > this.swipe_Max_Distance)
            return false;

        velocityX = Math.abs(velocityX);
        velocityY = Math.abs(velocityY);
        boolean result = false;

        if (velocityX > this.swipe_Min_Velocity
                && xDistance > this.swipe_Min_Distance) {
            if (e1.getX() > e2.getX()) // right to left
                this.listener.onSwipe(SWIPE_RIGHT);
            else
                this.listener.onSwipe(SWIPE_LEFT);

            result = true;
        } else if (velocityY > this.swipe_Min_Velocity
                && yDistance > this.swipe_Min_Distance) {
            if (e1.getY() > e2.getY()) // bottom to up
                this.listener.onSwipe(SWIPE_UP);
            else
                this.listener.onSwipe(SWIPE_DOWN);

            result = true;
        }

        return result;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent arg) {

        if (this.mode == MODE_DYNAMIC) {
            arg.setAction(ACTION_FAKE);

            this.context.dispatchTouchEvent(arg);
        }

        return false;
    }

    static interface SimpleGestureListener {
        void onSwipe(int direction);

    }

}
