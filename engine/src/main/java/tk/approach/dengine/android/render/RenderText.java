package tk.approach.dengine.android.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class RenderText extends RenderGuiElement {

    public static final float RI_TEXT_WIDTH = 32.0f;
    public static final float RI_TEXT_SPACESIZE = 13f;
    private float[] mModelMatrix = new float[16];
    private static int colorBufferId;

    private int mProgram;

    public static int[] l_size = {
            39, 103, 166, 231, 295, 359, 423, 487,
            9, 96, 167, 231, 310, 359, 423, 488,
            39, 103, 167, 231, 295, 359, 438, 487,
            39, 103, 167, 209, 295, 359, 423, 487,
            39, 103, 167, 231, 267, 339, 413, 465,
            26, 14, 14, 14, 25, 28, 31, 42,
            25, 89, 155, 12, 36, 34, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0};

    public RenderText(float[] color) {
        this.color = color;

        colors = new float[3 * 10];

        int vertexShader = ShaderHelper.loadShader(
                GLES20.GL_VERTEX_SHADER,
                ShaderHelper.vs_Gui);
        int fragmentShader = ShaderHelper.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                ShaderHelper.fs_Gui);

        Matrix.setIdentityM(mModelMatrix, 0);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mtrxProjectionAndView) {
    }

    public void justDraw(float[] m, int index, float alpha) {
        // Set the correct shader for our grid object.
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, 0);

        int mTexCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textureBufferId);
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, 0);


        int mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBufferId);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 3,
                GLES20.GL_FLOAT, false,
                0, 0);



        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, m, 0);

        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "s_texture"), 2);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "texture_u"), ((float)(index % 8))/8);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "texture_v"), ((float)(index / 8))/8);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "alpha"), alpha);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, drawListBufferId);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indicesLength, GLES20.GL_UNSIGNED_SHORT, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        GLES20.glDisableVertexAttribArray(mColorHandle);

    }

    public static int convertCharToIndex(int c_val) {
        int indx = -1;

        // Retrieve the index
        if (c_val > 64 && c_val < 91) // A-Z
        {
            indx = c_val - 65;
        } else if (c_val > 96 && c_val < 123) // a-z
        {
            indx = c_val - 97;
        } else if (c_val > 47 && c_val < 58) // 0-9
        {
            indx = c_val - 48 + 26;
        } else if (c_val == 43) // +
        {
            indx = 38;
        } else if (c_val == 45) // -
        {
            indx = 39;
        } else if (c_val == 33) // !
        {
            indx = 36;
        } else if (c_val == 63) // ?
        {
            indx = 37;
        } else if (c_val == 61) // =
        {
            indx = 40;
        } else if (c_val == 58) // :
        {
            indx = 41;
        } else if (c_val == 46) // .
        {
            indx = 42;
        } else if (c_val == 44) // ,
        {
            indx = 43;
        } else if (c_val == 42) // *
        {
            indx = 44;
        } else if (c_val == 36) // $
        {
            indx = 45;
        } else if (c_val == 64) // $
        {
            indx = 47;
        } else if (c_val == 60) {
            indx = 48;
        } else if (c_val == 62) {
            indx = 49;
        } else if (c_val == 47) {
            indx = 50;
        }
        return indx;
    }

    public static float getWidth(String text) {
        float x = 0;

        for (int j = 0; j < text.length(); j++) {
            char c = text.charAt(j);
            int c_val = (int) c;
            int indx = convertCharToIndex(c_val);

            if (indx == -1) {
                x += RI_TEXT_SPACESIZE;
                continue;
            }

            x += ((l_size[indx] % 64 / 2) + 4);
        }
        return x;
    }

    public static float getWidth(char[] text) {
        // Get attributes from textElement object
        float x = 0;

        // Create
        for (char aText : text) {
            if (aText == '\u0000') continue;
            // get ascii value
            int c_val = (int) aText;

            int indx = convertCharToIndex(c_val);

            if (indx == -1) {
                // unknown character, we will add a space for it to be save.
                x += RI_TEXT_SPACESIZE;
                continue;
            }

            // Calculate the new position
            x += ((l_size[indx] % 64 / 2) + 4);
        }
        return x;
    }

    public float[] getmModelMatrix() {
        return mModelMatrix;
    }

    public static void init() {
        float[] colors = new float[12];
        for (int i = 0; i < 12; i++) {
            colors[i] = 1f;
        }

        ByteBuffer colorBB = ByteBuffer.allocateDirect(12 * 4);
        colorBB.order(ByteOrder.nativeOrder());
        FloatBuffer colorBuffer = colorBB.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        final int buffers[] = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        colorBufferId = buffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorBuffer.capacity() * 4, colorBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        colorBuffer.limit(0);
    }
}