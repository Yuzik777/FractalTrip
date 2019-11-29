package OpenGLHelper;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.FloatBuffer;

public class Shader {

    private static final String TAG = "SHADER";
    private int mID;

    /*generates shader program from shader codes
          geometry shader code - optional and actually can not be used in OpenGL ES version less than 3.2
          so it is waiting here for better times to be uncommented
         */
    public Shader(String vertexCode, String fragmentCode, String... geometryCodeArg){
        String geometryCode = null;
        if(geometryCodeArg.length>0)
            geometryCode = geometryCodeArg[0];

        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexCode);
        checkCompileErrors(vertexShader, "VERTEX");

        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentCode);
        checkCompileErrors(fragmentShader, "FRAGMENT");

        int geometryShader = 0;
        if(geometryCode!=null){
            //geometryShader = loadShader(GLES30.GL_GEOMETRY_SHADER, geometryCode);
            //checkCompileErrors(geometryShader, "GEOMETRY");
        }

        mID = GLES30.glCreateProgram();
        GLES30.glAttachShader(mID, vertexShader);
        GLES30.glAttachShader(mID, fragmentShader);

        if(geometryCode!=null) {
            GLES30.glAttachShader(mID, geometryShader);
        }

        GLES30.glLinkProgram(mID);
        checkCompileErrors(mID, "PROGRAM");

        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);

        if(geometryCode!=null) {
            GLES30.glDeleteShader(geometryShader);
        }
    }

    public int getID() {
        return mID;
    }

    public void use(){
        GLES30.glUseProgram(mID);
    }

    public void setInt(String name, int value){
        GLES30.glUniform1i(GLES30.glGetUniformLocation(mID, name), value);
    }

    public void setFloat(String name, float value){
        GLES30.glUniform1f(GLES30.glGetUniformLocation(mID, name), value);
    }

    public void setVec2(String name, float[] value){
        GLES30.glUniform2fv(GLES30.glGetUniformLocation(mID, name), 1, value, 0);
    }

    public void setVec2(String name, float x, float y){
        GLES30.glUniform2f(GLES30.glGetUniformLocation(mID, name), x, y);
    }

    public void setVec3(String name, float[] value){
        GLES30.glUniform3fv(GLES30.glGetUniformLocation(mID, name), 1, value, 0);
    }

    public void setVec3(String name, float x, float y, float z){
        GLES30.glUniform3f(GLES30.glGetUniformLocation(mID, name), x, y, z);
    }

    public void setVec4(String name, float[] value){
        GLES30.glUniform4fv(GLES30.glGetUniformLocation(mID, name), 1, value, 0);
    }

    public void setVec4(String name, float x, float y, float z, float w){
        GLES30.glUniform4f(GLES30.glGetUniformLocation(mID, name), x, y, z, w);
    }

    public void setMat2(String name, float[] matrix){
        GLES30.glUniformMatrix2fv(GLES30.glGetUniformLocation(mID, name), 1, false, matrix, 0);
    }

    public void setMat2(String name, FloatBuffer matrix){
        GLES30.glUniformMatrix2fv(GLES30.glGetUniformLocation(mID, name), 1, false, matrix);
    }

    public void setMat3(String name, float[] matrix){
        GLES30.glUniformMatrix3fv(GLES30.glGetUniformLocation(mID, name), 1, false, matrix, 0);
    }

    public void setMat3(String name, FloatBuffer matrix){
        GLES30.glUniformMatrix3fv(GLES30.glGetUniformLocation(mID, name), 1, false, matrix);
    }

    public void setMat4(String name, float[] matrix){
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(mID, name), 1, false, matrix, 0);
    }

    public void setMat4(String name, FloatBuffer matrix){
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(mID, name), 1, false, matrix);
    }

    private int loadShader(int type, String shaderCode){

        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }

    private void checkCompileErrors(int shader, String type){

        int[] success = new int[1];
        if(!type.equals("PROGRAM"))
        {
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, success, 0);
            if(success[0]==0)
            {
                String info = GLES30.glGetShaderInfoLog(shader);
                Log.e(TAG, "ERROR::" + type + " SHADER COMPILATION ERROR: " + info);
            }
        }
        else
        {
            GLES30.glGetProgramiv(shader, GLES30.GL_LINK_STATUS, success, 0);
            if(success[0]==0)
            {
                String info = GLES30.glGetShaderInfoLog(shader);
                Log.e(TAG, "ERROR::PROGRAM LINKING ERROR:" + info);
            }
        }
    }
}
