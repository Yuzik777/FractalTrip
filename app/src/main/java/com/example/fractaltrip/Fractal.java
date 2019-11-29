package com.example.fractaltrip;

import android.os.Bundle;

public class Fractal{
    public static final String KEY_X_OFFSET = "x_offset";
    public static final String KEY_Y_OFFSET = "y_offset";
    public static final String KEY_ZOOM = "zoom";
    public static final String KEY_C0 = "c_re";
    public static final String KEY_C1 = "c_im";
    public static final String KEY_MAX_ITERATIONS = "max_iterations";

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

    public float xOffset = 0.0f;
    public float  yOffset = 0.0f;
    public float  zoom = 1.0f;
    public float[]  C = { -1.192809364548495f, -0.30551839464882935f };
    public int maxIterations = 256;

}
