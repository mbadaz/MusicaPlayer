package com.example.mbada.musicaplayer;

import android.view.animation.Interpolator;

/**
 * Creates a custom made animation interpolator for a bouncing effect
 */
public class myBounceInterpolator implements Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    myBounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
