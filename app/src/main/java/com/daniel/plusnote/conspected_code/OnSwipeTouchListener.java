package com.daniel.plusnote.conspected_code;//package com.example.plusnote.listeners;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class OnSwipeTouchListener /*extends AppCompatActivity*/ implements View.OnTouchListener {
//
//    private GestureDetector gestureDetector;
//
//    public OnSwipeTouchListener(Context c) {
//        gestureDetector = new GestureDetector(c, new GestureListener());
//    }
//
////    @Override
////    protected void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        gestureDetector = new GestureDetector(this, new GestureListener());
////    }
//
//    public boolean onTouch(final View view, final MotionEvent motionEvent) {
//        return gestureDetector.onTouchEvent(motionEvent);
//    }
//
//    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        private static final int SWIPE_THRESHOLD = 300;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 3000;
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//        // Determines the fling velocity and then fires the appropriate swipe event accordingly
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            boolean result = false;
//            try {
//                float diffX = e2.getX() - e1.getX();
//                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffX > 0) {
//                        onSwipeToRight();
//                    } else {
//                        onSwipeToLeft();
//                    }
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            return result;
//        }
//    }
//    public void onSwipeToRight() {
//    }
//
//    public void onSwipeToLeft() {
//    }
//}