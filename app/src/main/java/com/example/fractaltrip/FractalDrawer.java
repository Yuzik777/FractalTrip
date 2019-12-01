package com.example.fractaltrip;

import android.opengl.GLES30;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

import OpenGLHelper.Shader;

/**
 * Class for drawing fractals
 */
public class FractalDrawer {

    private Fractal mFractal;
    private Shader mShader;
    /**
     * Position of the fractal (in world coordinates)
     */
    private Vector3f mPosition = new Vector3f(0f, 0.0f, 0.0f);
    /**
     * Scale vector of the fractal
     */
    private Vector3f mScale = new Vector3f(1.0f, 1.0f, 1.0f);
    /**
     * Inverse projection matrix
     */
    private Matrix4f mInvProjection;
    /**
     * Vertex Array Object
     */
    private int mVAO;


    public void draw(){
        //Calculate model matrix for fractal(scaling and translation)
        Matrix4f model = new Matrix4f()
                .scale(mScale)
                .translate(mPosition);

        FloatBuffer fb = FloatBuffer.allocate(16);

        mShader.use();
        //Passing all uniforms to shader program
        mShader.setMat4("model", model.get(fb));
        mShader.setVec3("scale_vector", mScale.x, mScale.y, mScale.z);
        mShader.setMat4("inv_projection", mInvProjection.get(fb));
        mShader.setFloat("zoom", mFractal.zoom);
        mShader.setVec2("offset", mFractal.xOffset, mFractal.yOffset);
        mShader.setVec2("C", mFractal.C);
        mShader.setInt("max_iterations", mFractal.maxIterations);

        //Bind the shape and draw the fractal
        GLES30.glBindVertexArray(mVAO);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6);
        GLES30.glBindVertexArray(0);
    }

    public Fractal getFractal() {
        return mFractal;
    }

    public void setFractal(Fractal fractal) {
        mFractal = fractal;
    }

    public Vector3f getPosition() {
        return mPosition;
    }

    public void setPosition(Vector3f position) {
        mPosition = position;
    }

    public Vector3f getScale() {
        return mScale;
    }

    public void setScale(Vector3f scale) {
        mScale = scale;
    }

    public Matrix4f getInvProjection() {
        return mInvProjection;
    }

    public void setInvProjection(Matrix4f invProjection) {
        mInvProjection = invProjection;
    }

    public void setShader(Shader shader) {
        mShader = shader;
    }

    public void setVAO(int VAO) {
        mVAO = VAO;
    }


}
