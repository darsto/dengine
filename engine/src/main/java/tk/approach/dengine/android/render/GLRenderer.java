package tk.approach.dengine.android.render;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import tk.approach.dengine.android.Constans;
import tk.approach.dengine.android.DCore;
import tk.approach.dengine.android.TouchManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.io.IOException;
import java.io.InputStream;

public class GLRenderer implements Renderer {

    private final DCore dcore;
    private final int[] textures;
    private final Resources resources;
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];
    private TouchManager touchManager;
    private boolean isRunning;

    public GLRenderer(DCore dcore, Resources resources, int[] textures) {
        this.dcore = dcore;
        this.dcore.setGLRenderer(this);
        this.resources = resources;
        this.textures = textures;
        this.touchManager = new TouchManager(this.dcore);
        this.isRunning = true;
    }

    public void onPause() {
        this.isRunning = false;
        if (this.dcore.getCurrentWindow() != null) {
            this.dcore.getCurrentWindow().pause();
        }
    }

    public void onResume() {
        this.isRunning = true;
        if (this.dcore.getCurrentWindow() != null) {
            this.dcore.getCurrentWindow().resume();
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        if (this.isRunning) {
            try {
                if (!this.dcore.isRenderLockOn()) {
                    this.dcore.getCurrentWindow().render(mtrxProjection, mtrxView, mtrxProjectionAndView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        dcore.setDisplayWidth(width);
        dcore.setDisplayHeight(height);
        RenderString.displayHeight = height;
        GLES20.glViewport(0, 0, (int) dcore.getDisplayWidth(), (int) dcore.getDisplayHeight());
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }
        Matrix.orthoM(mtrxProjection, 0, 0f, dcore.getDisplayWidth(), 0.0f, dcore.getDisplayHeight(), 0, 50);
        this.setupScaling();
        if (this.dcore.getCurrentWindow() == null) {
            this.dcore.setCurrentWindow(0);
        }
        this.dcore.getCurrentWindow().reload();
        System.gc();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        this.setupTextures();
        RenderGuiElement.init();
        RenderText.init();
        RenderString.init();
        if (this.dcore.getCurrentWindow() != null) {
            this.dcore.getCurrentWindow().reload();
        }
        System.gc();
    }

    public void setupTextures() {
        int[] texturenames = new int[this.textures.length];
        GLES20.glGenTextures(this.textures.length, texturenames, 0);
        Bitmap tempBitmap;
        for (int i = 0; i < this.textures.length; i++) {
            InputStream is = this.resources.openRawResource(this.textures[i]);
            try {
                tempBitmap = BitmapFactory.decodeStream(is);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[i]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, tempBitmap, 0);
            tempBitmap.recycle();
        }
    }

    public void setupScaling() {
        Constans.RENDER_SCALE = 1.5f * (this.dcore.getDisplayWidth() > this.dcore.getDisplayHeight() ? Math.min(this.dcore.getDisplayWidth() / 1280, this.dcore.getDisplayHeight() / 720) : Math.min(this.dcore.getDisplayWidth() / 720, this.dcore.getDisplayHeight() / 1280));
    }

    public TouchManager getTouchManager() {
        return this.touchManager;
    }

}
