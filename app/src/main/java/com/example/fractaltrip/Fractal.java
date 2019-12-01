package com.example.fractaltrip;

import android.os.Bundle;

/**
 * Fractal - is a simple class for storing data necessary for drawing both Julia and Mandelbrot sets
 * Also contains methods for bundling and unbundling this data
 */
public class Fractal{
    private static final String KEY_X_OFFSET = "x_offset";
    private static final String KEY_Y_OFFSET = "y_offset";
    private static final String KEY_ZOOM = "zoom";
    private static final String KEY_C0 = "c_re";
    private static final String KEY_C1 = "c_im";
    private static final String KEY_MAX_ITERATIONS = "max_iterations";

    public float xOffset = 0.0f;
    public float yOffset = 0.0f;
    public float zoom = 1.0f;
    /**C - complex parameter: C = C[0] + i*C[1], where i = sqrt(-1)*/
    public float[] C = { -1.192809364548495f, -0.30551839464882935f };
    public int maxIterations = 256;

    public static Bundle getBundleFromFractal(Fractal f){
        Bundle b = new Bundle();
        b.putFloat(KEY_X_OFFSET, f.xOffset);
        b.putFloat(KEY_Y_OFFSET, f.yOffset);
        b.putFloat(KEY_ZOOM, f.zoom);
        b.putFloat(KEY_C0, f.C[0]);
        b.putFloat(KEY_C1, f.C[1]);
        b.putInt(KEY_MAX_ITERATIONS, f.maxIterations);
        return b;
    }

    public static Fractal getFractalFromBundle(Bundle b){
        Fractal f = new Fractal();
        f.xOffset = b.getFloat(KEY_X_OFFSET);
        f.yOffset = b.getFloat(KEY_Y_OFFSET);
        f.zoom = b.getFloat(KEY_ZOOM);
        f.C[0] = b.getFloat(KEY_C0);
        f.C[1] = b.getFloat(KEY_C1);
        f.maxIterations = b.getInt(KEY_MAX_ITERATIONS);
        return f;
    }
}
