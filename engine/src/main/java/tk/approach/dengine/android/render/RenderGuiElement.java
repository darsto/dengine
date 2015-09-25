package tk.approach.dengine.android.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import tk.approach.dengine.android.Constans;
import tk.approach.dengine.android.gui.GuiElement;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class RenderGuiElement implements Renderable, Comparable<RenderGuiElement>{

    protected GuiElement guiElement;
    protected byte tex;
    protected float[] color;
    protected float[] mModelMatrix = new float[16];
    protected int mProgram;
    protected static FloatBuffer vertexBuffer;
    protected static FloatBuffer textureBuffer;
    protected FloatBuffer colorBuffer;
    protected static ShortBuffer drawListBuffer;
    protected static float[] vecs;
    protected static float[] uvs;
    protected static short[] indices;
    protected float[] colors;
    protected int mPositionHandle = 0;
    protected int mColorHandle = 0;
    protected int mMVPMatrixHandle = 0;
    protected int mTexCoordLoc = 0;
    protected float offsetX, offsetY;
    protected boolean hover = true;
    protected static int vertexBufferId;
    protected static int textureBufferId;
    private int colorBufferId;
    protected static int drawListBufferId;
    protected static int indicesLength;

    protected RenderGuiElement() {

    }

    public RenderGuiElement(GuiElement guiElement, int tex) {
        this(guiElement, tex, new float[] {1f, 1f, 1f});
    }

    public RenderGuiElement(GuiElement guiElement, int tex, boolean hover) {
        this(guiElement, tex, new float[] {1f, 1f, 1f}, hover);
    }

    public RenderGuiElement(GuiElement guiElement, int tex, float[] parcolor, boolean hover) {
        this(guiElement, tex, parcolor);
        this.hover = hover;
    }

    public RenderGuiElement(GuiElement guiElement, int tex, float[] parcolor) {
        this.guiElement = guiElement;
        this.tex = (byte) tex;
        this.color = parcolor.length == 4 ? new float[] {parcolor[0], parcolor[1], parcolor[2]} : parcolor;

        this.colors = new float[12];
        for (int i = 0; i < 12; i++) {
            colors[i] = color[i % 3];
        }

        ByteBuffer colorBB = ByteBuffer.allocateDirect(12 * 4);
        colorBB.order(ByteOrder.nativeOrder());
        colorBuffer = colorBB.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        // First, generate as many buffers as we need.
// This will give us the OpenGL handles for these buffers.
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        colorBufferId = buffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorBuffer.capacity() * 4, colorBuffer, GLES20.GL_STATIC_DRAW);

// IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        colors = null;
        colorBuffer.limit(0);
        colorBuffer = null;
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

    private float[] tempModelMatrix = new float[16];
    private float tempRenderX, tempRenderY;

    public void draw(float[] mtrxProjectionAndView) {
        if (this.guiElement.isVisible()) {
            tempRenderX = this.guiElement.getPosX() * Constans.RENDER_SCALE;
            tempRenderY = RenderString.displayHeight - (this.guiElement.getPosY() + this.guiElement.getHeight()) * Constans.RENDER_SCALE;
            Matrix.setLookAtM(this.getmModelMatrix(), 0, -this.tempRenderX, -this.tempRenderY, 1f, -this.tempRenderX, -this.tempRenderY, 0f, 0f, 1.0f, 0.0f);
            this.scale(this.guiElement);
            Matrix.multiplyMM(tempModelMatrix, 0, mtrxProjectionAndView, 0, this.getmModelMatrix(), 0);
            this.justDraw(tempModelMatrix);
        }
    }

    public void justDraw(float[] m) {
        // Set the correct shader for our grid object.
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, 0);

        mTexCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textureBufferId);
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, 0);


        mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBufferId);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 3,
                GLES20.GL_FLOAT, false,
                0, 0);



        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, m, 0);

        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "s_texture"), 1);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "texture_u"), ((float)(this.tex % 8))/8);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "texture_v"), ((float)(this.tex / 8) + (this.hover && this.guiElement.isPressed() ? 1 : 0))/8);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "alpha"), 1f);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, drawListBufferId);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indicesLength, GLES20.GL_UNSIGNED_SHORT, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }

    public void scale(Object e) {
        Matrix.scaleM(this.getmModelMatrix(), 0, ((GuiElement)e).getWidth() * Constans.RENDER_SCALE, ((GuiElement)e).getHeight() * Constans.RENDER_SCALE, Constans.RENDER_SCALE);
    }

    public float[] getmModelMatrix() {
        return mModelMatrix;
    }


    @Override
    public int compareTo(@NonNull RenderGuiElement another) {
        return this.guiElement.getOrder() > another.guiElement.getOrder() ? 1 : (this.guiElement.getOrder() == another.guiElement.getOrder() ? 0 : -1);
    }

    public static void init() {
        vecs = new float[12];
        uvs = new float[8];
        indices = new short[6];


        vecs[0] = 0;
        vecs[1] = 1;
        vecs[2] = 0f;
        vecs[3] = 0;
        vecs[4] = 0;
        vecs[5] = 0f;
        vecs[6] = 1;
        vecs[7] = 0;
        vecs[8] = 0f;
        vecs[9] = 1;
        vecs[10] = 1;
        vecs[11] = 0f;

        uvs[0] = 0.001f;
        uvs[1] = 0.001f;
        uvs[2] = 0.001f;
        uvs[3] = 0.125f - 0.001f;
        uvs[4] = 0.125f - 0.001f;
        uvs[5] = 0.125f - 0.001f;
        uvs[6] = 0.125f - 0.001f;
        uvs[7] = 0.001f;

        indices = new short[]{0, 1, 2, 0, 2, 3};
        indicesLength = indices.length;

        ByteBuffer vertexBB = ByteBuffer.allocateDirect(12 * 4);
        vertexBB.order(ByteOrder.nativeOrder());
        vertexBuffer = vertexBB.asFloatBuffer();
        vertexBuffer.put(vecs);
        vertexBuffer.position(0);

        ByteBuffer indicesBB = ByteBuffer.allocateDirect(6 * 2);
        indicesBB.order(ByteOrder.nativeOrder());
        drawListBuffer = indicesBB.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        ByteBuffer texBB = ByteBuffer.allocateDirect(8 * 4);
        texBB.order(ByteOrder.nativeOrder());
        textureBuffer = texBB.asFloatBuffer();
        textureBuffer.put(uvs);
        textureBuffer.position(0);

        // First, generate as many buffers as we need.
// This will give us the OpenGL handles for these buffers.
        final int buffers[] = new int[3];
        GLES20.glGenBuffers(3, buffers, 0);

// Bind to the buffer. Future commands will affect this buffer specifically.
        vertexBufferId = buffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferId);

// Transfer data from client memory to the buffer.
// We can release the client memory after this call.
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);

        textureBufferId = buffers[1];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textureBufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureBuffer.capacity() * 4, textureBuffer, GLES20.GL_STATIC_DRAW);

        drawListBufferId = buffers[2];
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, drawListBufferId);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, drawListBuffer.capacity() * 2, drawListBuffer, GLES20.GL_STATIC_DRAW);

// IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
