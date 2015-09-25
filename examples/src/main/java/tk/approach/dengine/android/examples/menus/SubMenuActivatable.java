package tk.approach.dengine.android.examples.menus;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.Arrays;

import tk.approach.dengine.android.Constans;
import tk.approach.dengine.android.DCore;
import tk.approach.dengine.android.TouchPoint;
import tk.approach.dengine.android.activatable.Activatable;
import tk.approach.dengine.android.gui.GuiButton;
import tk.approach.dengine.android.gui.GuiElement;
import tk.approach.dengine.android.render.RenderGuiButton;
import tk.approach.dengine.android.render.RenderGuiElement;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class SubMenuActivatable implements Activatable {

    private DCore dcore;
    private float[] mtrxGuiProjectionAndView;
    private GuiElement[] guiElements;
    private RenderGuiElement[] guiRenders;
    private TouchPoint tempPoint;
    private int popupOpened = 0;
    private GuiElement overlayElement;

    public SubMenuActivatable(DCore dcore) {
        this.dcore = dcore;
    }

    @Override
    public void init() {
        this.setupGui();
    }

    private void setupGui() {
        this.guiElements = new GuiElement[1];
        this.guiRenders = new RenderGuiElement[this.guiElements.length];
        this.guiElements[0] = new GuiButton("Return", this.dcore.getDisplayWidth() / Constans.RENDER_SCALE / 2 - 135, this.dcore.getDisplayHeight() / Constans.RENDER_SCALE / 2 - 45, 270, 90) {
            @Override
            public void setPressState(boolean isPressed, TouchPoint o) {
                if (isPressed != isPressed()) {
                    if (isPressed) {
                        dcore.playSound(0);
                    } else {
                        dcore.playSound(1);
                        if (o == null || o.isToBeDeleted()) {
                            dcore.setCurrentWindow(MainActivity.MENU_ACTIVATABLE_ID);
                            dcore.playMusic(0);
                        }
                    }
                }
                setPressed(isPressed);
            }
        };

        this.guiRenders[0] = new RenderGuiButton((GuiButton) this.guiElements[0], new float[]{1f, 1f, 1f, 1.0f});
        Arrays.sort(this.guiElements);
        Arrays.sort(this.guiRenders);
    }

    @Override
    public void reload() {
        this.mtrxGuiProjectionAndView = null;
        this.setupGui();
    }

    @Override
    public void pause() {
        //nothing
    }

    @Override
    public void resume() {
        //nothing
    }

    protected void handleTouchEvents() {
        int size = this.dcore.getGLRenderer().getTouchManager().getActivePointers().size();
        for (int i = 0; i < size; i++) {
            tempPoint = this.dcore.getGLRenderer().getTouchManager().getActivePointers().get(i);
            if (tempPoint != null)
                tempPoint.setUsed(false);
        }

        for (GuiElement e : this.guiElements) {
            boolean pressed = false;
            size = this.dcore.getGLRenderer().getTouchManager().getActivePointers().size();
            for (int i = 0; i < size; i++) {
                tempPoint = this.dcore.getGLRenderer().getTouchManager().getActivePointers().get(i);
                if (tempPoint != null && !tempPoint.isUsed() && e.onTouchEvent(tempPoint.getX(), tempPoint.getY())) {
                    pressed = true;
                    e.setPressState(!tempPoint.isToBeDeleted(), tempPoint);
                    tempPoint.setUsed(true);
                }
            }
            if (!pressed) {
                e.setPressState(false, tempPoint);
            }
        }
        size = this.dcore.getGLRenderer().getTouchManager().getActivePointers().size();
        for (int i = 0; i < size; i++) {
            tempPoint = this.dcore.getGLRenderer().getTouchManager().getActivePointers().get(i);
            if (tempPoint != null) {
                if (tempPoint.isToBeDeleted())
                    this.dcore.getGLRenderer().getTouchManager().getActivePointers().remove(i);
            }
        }
    }

    @Override
    public void render(float[] mtrxProjection, float[] mtrxView, float[] mtrxProjectionAndView) {
        if (this.mtrxGuiProjectionAndView == null) {
            initGuiMatrix(mtrxProjection, mtrxView, mtrxProjectionAndView);
        }
        handleTouchEvents();
        preRender(mtrxProjection, mtrxView, mtrxProjectionAndView);
        renderContents(mtrxProjection, mtrxView, mtrxProjectionAndView);
        postRender(mtrxProjection, mtrxView, mtrxProjectionAndView);
    }

    protected void initGuiMatrix(float[] mtrxProjection, float[] mtrxView, float[] mtrxProjectionAndView) {
        mtrxGuiProjectionAndView = new float[16];
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mtrxGuiProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

    }

    protected void preRender(float[] mtrxProjection, float[] mtrxView, float[] mtrxProjectionAndView) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    protected void renderContents(float[] mtrxProjection, float[] mtrxView, float[] mtrxProjectionAndView) {
        drawGuiRenders();
    }

    protected void drawGuiRenders() {
        if (this.guiRenders != null) {
            for (RenderGuiElement e : this.guiRenders) {
                e.draw(mtrxGuiProjectionAndView);
            }
        }
    }

    protected void postRender(float[] mtrxProjection, float[] mtrxView, float[] mtrxProjectionAndView) {
        //nothing
    }
}

