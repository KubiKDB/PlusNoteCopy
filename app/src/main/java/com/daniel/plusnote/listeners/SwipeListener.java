package com.daniel.plusnote.listeners;

public class SwipeListener extends HorizontalSwipeListener {

    public SwipeListener(int minDistance) {
        super(minDistance);
    }

    @Override
    void onSwipeRight() {
        System.out.println("right");
    }

    @Override
    void onSwipeLeft() {
        System.out.println("left");
    }

}