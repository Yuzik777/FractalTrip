package com.example.fractaltrip;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class FractalGLSurfaceView extends GLSurfaceView {

    private final FractalRenderer mRenderer;
    private ScaleGestureDetector mScaleDetector;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mLastTouchX;
    private float mLastTouchY;

    public FractalGLSurfaceView(Context context, String orientation) {
        super(context);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        setEGLContextClientVersion(3);//OpenGL ES 3.0
        mRenderer = new FractalRenderer(getContext().getApplicationContext(), orientation);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        requestRender();
    }

    public void setJuliaFractal(Fractal fractal){
        mRenderer.setJuliaFractal(fractal);
    }

    public Fractal getJuliaFractal(){
        return mRenderer.getJuliaFractal();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //ScaleGestureDetector works here
        mScaleDetector.onTouchEvent(e);

        int action = e.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                int pointerIndex = e.getActionIndex();
                float x = e.getX(pointerIndex);
                float y = e.getY(pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = e.getPointerId(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer
                int pointerIndex =
                        e.findPointerIndex(mActivePointerId);

                float x = e.getX(pointerIndex);
                float y = e.getY(pointerIndex);

                // Calculate the distance moved
                float dx = x - mLastTouchX;
                float dy = y - mLastTouchY;

                if((!mScaleDetector.isInProgress())&&(e.getPointerCount()==1))
                {//Pass move and drag events only if we are not scaling right now and have 1 pointer active
                    mRenderer.onActionMove(x, y);
                    mRenderer.onActionDrag(x, y, dx, dy);
                    requestRender();
                }

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                int pointerIndex = e.getActionIndex();
                int pointerId = e.getPointerId(pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = e.getX(newPointerIndex);
                    mLastTouchY = e.getY(newPointerIndex);
                    mActivePointerId = e.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mRenderer.onZoom(1/detector.getScaleFactor(),
                    detector.getFocusX(),detector.getFocusY());
            requestRender();
            return true;
        }
    }

}
