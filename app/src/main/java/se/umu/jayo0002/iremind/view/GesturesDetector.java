package se.umu.jayo0002.iremind.view;

import android.view.MotionEvent;

public class GesturesDetector extends android.view.GestureDetector.SimpleOnGestureListener{


    @Override
    public boolean onDown(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

}
