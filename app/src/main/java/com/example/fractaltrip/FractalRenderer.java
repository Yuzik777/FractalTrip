package com.example.fractaltrip;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import org.joml.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import OpenGLHelper.Shader;

public class FractalRenderer implements GLSurfaceView.Renderer {

    private int width;
    private int height;
    private String mOrientation;

    private int mVAOSquare;
    private int mVBOSquare;
    private FloatBuffer fb = FloatBuffer.allocate(16);
    private Matrix4f mProjection;
    private Matrix4f mInvProjection;

    private Vector3f mProjVector;

    private int mPalette;
    private Fractal mJuliaFractal = new Fractal();
    private Vector3f mJuliaPos = new Vector3f(0f, -1.0f, 0.0f);
    private Vector3f mJuliaScale = new Vector3f(1.0f, 0.5f, 1.0f);;
    private Shader mJuliaShader;

    private Shader mMandelbrotShader;
    private Fractal mMandelbrotFractal = new Fractal();
    private Vector3f mMandelbrotPos = new Vector3f(0f, 1.0f, 0.0f);
    private Vector3f mMandelbrotScale = new Vector3f(1.0f, 0.5f, 1.0f);;


    private Context mContext;

    private void createPalette(){
        //Palette for fragment shader(1D texture in other words)
        //Format is R1,G1,B1,R2,G2,B2...
        int[] palette =
                {
                        0x6F, 0x00, 0x00, 0xFF, 0x0E, 0x03, 0xFF, 0x1C,
                        0x07, 0xFF, 0x2A, 0x0A, 0xFF, 0x38, 0x0E, 0xFF,
                        0x46, 0x12, 0xFF, 0x54, 0x15, 0xFF, 0x62, 0x19,
                        0xFF, 0x71, 0x1D, 0xFF, 0x7F, 0x20, 0xFF, 0x88,
                        0x22, 0xFF, 0x92, 0x25, 0xFF, 0x9C, 0x27, 0xFF,
                        0xA6, 0x2A, 0xFF, 0xB0, 0x2C, 0xFF, 0xBA, 0x2F,
                        0xFF, 0xC4, 0x31, 0xFF, 0xCE, 0x34, 0xFF, 0xD8,
                        0x36, 0xFF, 0xE2, 0x39, 0xFF, 0xEC, 0x3B, 0xFF,
                        0xF6, 0x3E, 0xFF, 0xFF, 0x40, 0xF8, 0xFE, 0x40,
                        0xF1, 0xFE, 0x40, 0xEA, 0xFE, 0x41, 0xE3, 0xFD,
                        0x41, 0xDC, 0xFD, 0x41, 0xD6, 0xFD, 0x42, 0xCF,
                        0xFC, 0x42, 0xC8, 0xFC, 0x42, 0xC1, 0xFC, 0x43,
                        0xBA, 0xFB, 0x43, 0xB4, 0xFB, 0x43, 0xAD, 0xFB,
                        0x44, 0xA6, 0xFA, 0x44, 0x9F, 0xFA, 0x45, 0x98,
                        0xFA, 0x45, 0x92, 0xF9, 0x45, 0x8B, 0xF9, 0x46,
                        0x84, 0xF9, 0x46, 0x7D, 0xF8, 0x46, 0x76, 0xF8,
                        0x46, 0x6F, 0xF8, 0x47, 0x68, 0xF8, 0x47, 0x61,
                        0xF7, 0x47, 0x5A, 0xF7, 0x48, 0x53, 0xF7, 0x48,
                        0x4C, 0xF6, 0x48, 0x45, 0xF6, 0x49, 0x3E, 0xF6,
                        0x49, 0x37, 0xF6, 0x4A, 0x30, 0xF5, 0x4A, 0x29,
                        0xF5, 0x4A, 0x22, 0xF5, 0x4B, 0x1B, 0xF5, 0x4B,
                        0x14, 0xF4, 0x4B, 0x0D, 0xF4, 0x4C, 0x06, 0xF4,
                        0x4D, 0x04, 0xF1, 0x51, 0x0D, 0xE9, 0x55, 0x16,
                        0xE1, 0x59, 0x1F, 0xD9, 0x5D, 0x28, 0xD1, 0x61,
                        0x31, 0xC9, 0x65, 0x3A, 0xC1, 0x69, 0x42, 0xB9,
                        0x6D, 0x4B, 0xB1, 0x71, 0x54, 0xA9, 0x75, 0x5D,
                        0xA1, 0x79, 0x66, 0x99, 0x7D, 0x6F, 0x91, 0x81,
                        0x78, 0x89, 0x86, 0x80, 0x81, 0x8A, 0x88, 0x7A,
                        0x8E, 0x90, 0x72, 0x92, 0x98, 0x6A, 0x96, 0xA1,
                        0x62, 0x9A, 0xA9, 0x5A, 0x9E, 0xB1, 0x52, 0xA2,
                        0xBA, 0x4A, 0xA6, 0xC2, 0x42, 0xAA, 0xCA, 0x3A,
                        0xAE, 0xD3, 0x32, 0xB2, 0xDB, 0x2A, 0xB6, 0xE3,
                        0x22, 0xBA, 0xEB, 0x1A, 0xBE, 0xF4, 0x12, 0xC2,
                        0xFC, 0x0A, 0xC6, 0xF5, 0x02, 0xCA, 0xE6, 0x09,
                        0xCE, 0xD8, 0x18, 0xD1, 0xCA, 0x27, 0xD5, 0xBB,
                        0x36, 0xD8, 0xAD, 0x45, 0xDC, 0x9E, 0x54, 0xE0,
                        0x90, 0x62, 0xE3, 0x82, 0x6F, 0xE6, 0x71, 0x7C,
                        0xEA, 0x61, 0x89, 0xEE, 0x51, 0x96, 0xF2, 0x40,
                        0xA3, 0xF5, 0x30, 0xB0, 0xF9, 0x20, 0xBD, 0xFD,
                        0x0F, 0xE3, 0xFF, 0x01, 0xE9, 0xFF, 0x01, 0xEE,
                        0xFF, 0x01, 0xF4, 0xFF, 0x00, 0xFA, 0xFF, 0x00,
                        0xFF, 0xFF, 0x00, 0xFF, 0xFF, 0x0A, 0xFF, 0xFF,
                        0x15, 0xFF, 0xFF, 0x20, 0xFF, 0xFF, 0x2B, 0xFF,
                        0xFF, 0x36, 0xFF, 0xFF, 0x41, 0xFF, 0xFF, 0x4C,
                        0xFF, 0xFF, 0x57, 0xFF, 0xFF, 0x62, 0xFF, 0xFF,
                        0x6D, 0xFF, 0xFF, 0x78, 0xFF, 0xFF, 0x81, 0xFF,
                        0xFF, 0x8A, 0xFF, 0xFF, 0x92, 0xFF, 0xFF, 0x9A,
                        0xFF, 0xFF, 0xA3, 0xFF, 0xFF, 0xAB, 0xFF, 0xFF,
                        0xB4, 0xFF, 0xFF, 0xBC, 0xFF, 0xFF, 0xC4, 0xFF,
                        0xFF, 0xCD, 0xFF, 0xFF, 0xD5, 0xFF, 0xFF, 0xDD,
                        0xFF, 0xFF, 0xE6, 0xFF, 0xFF, 0xEE, 0xFF, 0xFF,
                        0xF7, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xF9,
                        0xFF, 0xFF, 0xF3, 0xFF, 0xFF, 0xED, 0xFF, 0xFF,
                        0xE7, 0xFF, 0xFF, 0xE1, 0xFF, 0xFF, 0xDB, 0xFF,
                        0xFF, 0xD5, 0xFF, 0xFF, 0xCF, 0xFF, 0xFF, 0xCA,
                        0xFF, 0xFF, 0xC4, 0xFF, 0xFF, 0xBE, 0xFF, 0xFF,
                        0xB8, 0xFF, 0xFF, 0xB2, 0xFF, 0xFF, 0xAC, 0xFF,
                        0xFF, 0xA6, 0xFF, 0xFF, 0xA0, 0xFF, 0xFF, 0x9B,
                        0xFF, 0xFF, 0x96, 0xFF, 0xFF, 0x90, 0xFF, 0xFF,
                        0x8B, 0xFF, 0xFF, 0x86, 0xFF, 0xFF, 0x81, 0xFF,
                        0xFF, 0x7B, 0xFF, 0xFF, 0x76, 0xFF, 0xFF, 0x71,
                        0xFF, 0xFF, 0x6B, 0xFF, 0xFF, 0x66, 0xFF, 0xFF,
                        0x61, 0xFF, 0xFF, 0x5C, 0xFF, 0xFF, 0x56, 0xFF,
                        0xFF, 0x51, 0xFF, 0xFF, 0x4C, 0xFF, 0xFF, 0x47,
                        0xFF, 0xFF, 0x41, 0xF9, 0xFF, 0x40, 0xF0, 0xFF,
                        0x40, 0xE8, 0xFF, 0x40, 0xDF, 0xFF, 0x40, 0xD7,
                        0xFF, 0x40, 0xCF, 0xFF, 0x40, 0xC6, 0xFF, 0x40,
                        0xBE, 0xFF, 0x40, 0xB5, 0xFF, 0x40, 0xAD, 0xFF,
                        0x40, 0xA4, 0xFF, 0x40, 0x9C, 0xFF, 0x40, 0x95,
                        0xFF, 0x40, 0x8D, 0xFF, 0x40, 0x86, 0xFF, 0x40,
                        0x7E, 0xFF, 0x40, 0x77, 0xFF, 0x40, 0x6F, 0xFF,
                        0x40, 0x68, 0xFF, 0x40, 0x60, 0xFF, 0x40, 0x59,
                        0xFF, 0x40, 0x51, 0xFF, 0x40, 0x4A, 0xFA, 0x43,
                        0x42, 0xF3, 0x48, 0x3E, 0xED, 0x4E, 0x3D, 0xE6,
                        0x53, 0x3B, 0xDF, 0x58, 0x39, 0xD8, 0x5E, 0x37,
                        0xD2, 0x63, 0x35, 0xCB, 0x68, 0x34, 0xC4, 0x6D,
                        0x32, 0xBD, 0x73, 0x30, 0xB7, 0x78, 0x2E, 0xB0,
                        0x7D, 0x2D, 0xA9, 0x83, 0x2B, 0xA2, 0x88, 0x29,
                        0x9C, 0x8D, 0x27, 0x95, 0x92, 0x25, 0x8E, 0x98,
                        0x24, 0x87, 0x9D, 0x22, 0x81, 0xA2, 0x20, 0x7A,
                        0xA6, 0x1E, 0x74, 0xAB, 0x1D, 0x6E, 0xB0, 0x1B,
                        0x68, 0xB5, 0x1A, 0x61, 0xB9, 0x18, 0x5B, 0xBE,
                        0x17, 0x55, 0xC3, 0x15, 0x4F, 0xC8, 0x13, 0x48,
                        0xCD, 0x12, 0x42, 0xD1, 0x10, 0x3C, 0xD6, 0x0F,
                        0x36, 0xDB, 0x0D, 0x2F, 0xE0, 0x0C, 0x29, 0xE4,
                        0x0A, 0x23, 0xE9, 0x08, 0x1D, 0xEE, 0x07, 0x16,
                        0xF3, 0x05, 0x10, 0xF7, 0x04, 0x0A, 0xFC, 0x02,
                        0x04, 0xFB, 0x01, 0x04, 0xEF, 0x00, 0x12, 0xE4,
                        0x00, 0x1F, 0xD9, 0x00, 0x2D, 0xCE, 0x00, 0x3B,
                        0xC2, 0x00, 0x48, 0xB7, 0x00, 0x56, 0xAC, 0x00,
                        0x64, 0xA0, 0x00, 0x72, 0x95, 0x00, 0x7F, 0x8A,
                        0x00, 0x88, 0x7F, 0x00, 0x92, 0x75, 0x00, 0x9C,
                        0x6B, 0x00, 0xA6, 0x61, 0x00, 0xB0, 0x57, 0x00,
                        0xBA, 0x4E, 0x00, 0xC4, 0x44, 0x00, 0xCE, 0x3A,
                        0x00, 0xD8, 0x30, 0x00, 0xE2, 0x27, 0x00, 0xEC,
                        0x1D, 0x00, 0xF6, 0x13, 0x00, 0xFF, 0x09, 0x00,
                };
        //Packing out palette to ByteBuffer
        ByteBuffer buf = ByteBuffer.allocate(palette.length);
        for(int color : palette){
            buf.put((byte)color);
        }
        buf.position(0);

        int[] tmp = new int[1];
        //Generating texture ID
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glGenTextures(1, tmp, 0);
        mPalette = tmp[0];

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mPalette);
        //Passing texture data(ByteBuffer of RGBs) to binded texture
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB8, palette.length, 1,
                0, GLES30.GL_RGB, GLES30.GL_UNSIGNED_BYTE, buf);
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
    }

    public FractalRenderer(Context context, String orientation){
        super();
        mContext = context;
        mInvProjection = new Matrix4f();
        mOrientation = orientation;
        if(mOrientation.equals("landscape")){
            mJuliaPos = new Vector3f(0.0f, 0.0f, 0.0f);
            mJuliaScale = new Vector3f(1.0f);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        mJuliaShader = new Shader(mContext.getString(R.string.julia_vs),
                mContext.getString(R.string.julia_fs));
        mMandelbrotShader = new Shader(mContext.getString(R.string.julia_vs),
                mContext.getString(R.string.mandelbrot_fs));
        createPalette();
        makeSquareVAO();
        GLES30.glClearColor(1.0f, 0.0f, 0.5f, 1.0f);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
        //Calculate width-height ratio and generate orthonormal projection matrix
        float fAspect;
        fAspect = 1.0f*width/height;
        float unit = 1.0f;
        if(fAspect<=1) {
            mProjection = new Matrix4f().ortho(-unit, unit,
                    -unit/fAspect, unit/fAspect , -unit, unit);
        }else {
            mProjection = new Matrix4f().ortho(-unit*fAspect, unit*fAspect,
                    -unit, unit, -unit, unit);
        }
        mInvProjection = mProjection.invert(mInvProjection);

        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {


        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        //If orientation is landscape - then only Julia set is drawn(full size)
        //If portrait - both Mandelbrot(top) and Julia(bottom) sets are drawn
        mJuliaShader.use();
        //Calculate model matrix for fractal(scaling and translation)
        Matrix4f model = new Matrix4f()
                .scale(mJuliaScale)
                .translate(mJuliaPos);

        //Passing all uniforms to shader program
        mJuliaShader.setMat4("model", model.get(fb));
        mJuliaShader.setVec3("scale_vector", mJuliaScale.x, mJuliaScale.y, mJuliaScale.z);
        mJuliaShader.setMat4("inv_projection", mInvProjection.get(fb));
        mJuliaShader.setFloat("zoom", mJuliaFractal.zoom);
        mJuliaShader.setVec2("offset", mJuliaFractal.xOffset, mJuliaFractal.yOffset);
        mJuliaShader.setVec2("C", mJuliaFractal.C);
        mJuliaShader.setInt("max_iterations", mJuliaFractal.maxIterations);

        //Bind rectangle shape and draw the fractal
        GLES30.glBindVertexArray(mVAOSquare);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);

        if(mOrientation.equals("landscape")) return;
        mMandelbrotShader.use();
        model = new Matrix4f()
                .scale(mMandelbrotScale)
                .translate(mMandelbrotPos);

        mMandelbrotShader.setMat4("model", model.get(fb));
        mMandelbrotShader.setVec3("scale_vector", mMandelbrotScale.x, mMandelbrotScale.y, mMandelbrotScale.z);
        mMandelbrotShader.setMat4("inv_projection", mInvProjection.get(fb));
        mMandelbrotShader.setFloat("zoom", mMandelbrotFractal.zoom);
        mMandelbrotShader.setVec2("offset", mMandelbrotFractal.xOffset, mMandelbrotFractal.yOffset);
        mMandelbrotShader.setVec2("C", mMandelbrotFractal.C);
        mMandelbrotShader.setInt("max_iterations", mMandelbrotFractal.maxIterations);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);

        GLES30.glBindVertexArray(0);
    }

    public void setJuliaFractal(Fractal fractal){
        mJuliaFractal = fractal;
    }

    public Fractal getJuliaFractal(){
        return mJuliaFractal;
    }

    private void makeSquareVAO(){
        float[] squarePoints = {
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(squarePoints.length * 4);
        vbb.order(ByteOrder.nativeOrder());    // use the device hardware's native byte order
        FloatBuffer fb = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        fb.put(squarePoints);    // add the coordinates to the FloatBuffer
        fb.position(0);      // set the buffer to read the first coordinate

        int[] tmp = new int[1];
        GLES30.glGenVertexArrays(1, tmp, 0);
        mVAOSquare = tmp[0];

        GLES30.glGenBuffers(1, tmp, 0);
        mVBOSquare = tmp[0];
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOSquare);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, squarePoints.length*4, fb, GLES30.GL_STATIC_DRAW);

        GLES30.glBindVertexArray(mVAOSquare);

        //position attribute
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3*4,0);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);


    }

    public void onActionMove(float xScreen, float yScreen) {

        float x = xScreen/width;
        float y = yScreen/height;


        //If Mandelbrot set is visible and the pointer hit the top part of the screen, then
        //set Julia's complex parameter C to a new value, based on pointer position
        if(y<=0.5 && mOrientation.equals("portrait")) {
            Fractal mand = mMandelbrotFractal;

            Matrix3f m = mInvProjection.get3x3(new Matrix3f());
            mJuliaFractal.C[1] = new Vector3f(1.0f, -1 * mand.zoom + mand.yOffset + mand.zoom * y * 2 * 2, 1.0f).mul(mMandelbrotScale).mul(m).y;
            mJuliaFractal.C[0] = new Vector3f(-1 * mand.zoom + mand.xOffset + mand.zoom * x * 2, 1.0f, 1.0f).mul(mMandelbrotScale).mul(m).x;
        }

    }

    public void onZoom(float scaleFactor, float focusX, float focusY) {
        //Zoom in or out Julia fractal
        if(focusY/height>0.5 || mOrientation.equals("landscape"))
            mJuliaFractal.zoom*=scaleFactor;
    }

    public void onActionDrag(float x, float y, float dx, float dy) {
        //Change Julia's offsets when drag occurs
        if(y/height<0.5 && mOrientation.equals("portrait")) return;
        mJuliaFractal.xOffset-=dx/width*mJuliaFractal.zoom;
        mJuliaFractal.yOffset+=dy/height*mJuliaFractal.zoom/mJuliaScale.y;//TODO
    }


}
