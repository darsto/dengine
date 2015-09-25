package tk.approach.dengine.android.examples.menus;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.Arrays;

import tk.approach.dengine.android.Constans;
import tk.approach.dengine.android.DCore;
import tk.approach.dengine.android.TouchPoint;
import tk.approach.dengine.android.activatable.Activatable;
import tk.approach.dengine.android.gui.*;
import tk.approach.dengine.android.render.*;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class MenuActivatable implements Activatable {

    private DCore dcore;
    private float[] mtrxGuiProjectionAndView;
    private GuiElement[] guiElements;
    private RenderGuiElement[] guiRenders;
    private TouchPoint tempPoint;
    private int popupOpened = 0;
    private GuiElement overlayElement;

    public MenuActivatable(DCore dcore) {
        this.dcore = dcore;
    }

    @Override
    public void init() {
        this.setupGui();
    }

    private void setupGui() {
        this.guiElements = new GuiElement[8];
        this.guiRenders = new RenderGuiElement[this.guiElements.length];
        this.guiElements[0] = new GuiButton("Play", this.dcore.getDisplayWidth() / Constans.RENDER_SCALE / 2 - 135, this.dcore.getDisplayHeight() / Constans.RENDER_SCALE / 2 - 145, 270, 90) {
            @Override
            public void setPressState(boolean isPressed, TouchPoint o) {
                if (isPressed != isPressed()) {
                    if (isPressed) {
                        dcore.playSound(0);
                    } else {
                        dcore.playSound(1);
                        if (o == null || o.isToBeDeleted()) {
                            dcore.setCurrentWindow(MainActivity.SUBMENU_ACTIVATABLE_ID);
                            dcore.playMusic(1);
                        }
                    }
                }
                setPressed(isPressed);
            }
        };
        this.guiElements[1] = new GuiButton("Settings", this.dcore.getDisplayWidth() / Constans.RENDER_SCALE / 2 - 135, this.dcore.getDisplayHeight() / Constans.RENDER_SCALE / 2 - 45, 270, 90) {
            @Override
            public void setPressState(boolean isPressed, TouchPoint o) {
                if (isPressed != isPressed()) {
                    if (isPressed) {
                        dcore.playSound(0);
                    } else {
                        dcore.playSound(1);
                        if (o == null || o.isToBeDeleted()) {
                            popupOpened = 1;
                        }
                    }
                }
                setPressed(isPressed);
            }
        };
        this.guiElements[2] = new GuiButton("About", this.dcore.getDisplayWidth() / Constans.RENDER_SCALE / 2 - 135, this.dcore.getDisplayHeight() / Constans.RENDER_SCALE / 2 + 60, 270, 90) {
            @Override
            public void setPressState(boolean isPressed, TouchPoint o) {
                if (isPressed != isPressed()) {
                    if (isPressed) {
                        dcore.playSound(0);
                    } else {
                        dcore.playSound(1);
                        if (o == null || o.isToBeDeleted()) {
                            popupOpened = 2;
                        }
                    }
                }
                setPressed(isPressed);
            }
        };
        this.overlayElement = this.guiElements[3] = new GuiElement(10, this.dcore.getDisplayWidth() / Constans.RENDER_SCALE / 2 - 180, this.dcore.getDisplayHeight() / Constans.RENDER_SCALE / 2 - 225, 360, 450) {
            @Override
            public boolean isVisible() {
                return popupOpened != 0;
            }
        };
        this.guiElements[4] = new GuiButton(11, "Return", this.guiElements[3], this.guiElements[3].getWidth() / 2 - 135, this.guiElements[3].getHeight() - 130, 270, 90) {
            @Override
            public boolean isVisible() {
                return popupOpened != 0;
            }

            @Override
            public void setPressState(boolean isPressed, TouchPoint o) {
                if (isPressed != isPressed()) {
                    if (isPressed) {
                        dcore.playSound(0);
                    } else {
                        dcore.playSound(1);
                        if (o == null || o.isToBeDeleted()) {
                            popupOpened = 0;
                        }
                    }
                }
                setPressed(isPressed);
            }
        };
        this.guiElements[5] = new GuiButton(11, null, this.guiElements[3], this.guiElements[3].getWidth() / 2 - 135, 45, 270, 90) {
            private String soundsString;

            @Override
            protected void postInit() {
                soundsString = dcore.getSettings().getBoolean(String.valueOf("soundsOn").intern(), true) ? String.valueOf("Sounds: ON").intern() : String.valueOf("Sounds: OFF").intern();
                this.setText(soundsString);
            }

            @Override
            public boolean isVisible() {
                return popupOpened == 1;
            }

            @Override
            public void setPressState(boolean isPressed, TouchPoint o) {
                if (isPressed != isPressed()) {
                    if (isPressed) {
                        dcore.playSound(0);
                    } else {
                        dcore.playSound(1);
                        if (o == null || o.isToBeDeleted()) {
                            dcore.getSettings().setBoolean(String.valueOf("soundsOn").intern(), !dcore.getSettings().getBoolean(String.valueOf("soundsOn").intern(), true));
                            soundsString = dcore.getSettings().getBoolean(String.valueOf("soundsOn").intern(), true) ? String.valueOf("Sounds: ON").intern() : String.valueOf("Sounds: OFF").intern();
                            this.setText(soundsString);
                        }
                    }
                }
                setPressed(isPressed);
            }
        };
        this.guiElements[6] = new GuiButton(11, null, this.guiElements[3], this.guiElements[3].getWidth() / 2 - 135, 140, 270, 90) {
            private String musicString;

            @Override
            protected void postInit() {
                musicString = dcore.getSettings().getBoolean(String.valueOf("musicOn").intern(), true) ? String.valueOf("Music: ON").intern() : String.valueOf("Music: OFF").intern();
                this.setText(musicString);
            }

            @Override
            public boolean isVisible() {
                return popupOpened == 1;
            }

            @Override
            public void setPressState(boolean isPressed, TouchPoint o) {
                if (isPressed != isPressed()) {
                    if (isPressed) {
                        dcore.playSound(0);
                    } else {
                        dcore.playSound(1);
                        if (o == null || o.isToBeDeleted()) {
                            dcore.getSettings().setBoolean(String.valueOf("musicOn").intern(), !dcore.getSettings().getBoolean(String.valueOf("musicOn").intern(), true));
                            dcore.setMusicState(dcore.getSettings().getBoolean("musicOn", true));
                            musicString = dcore.getSettings().getBoolean(String.valueOf("musicOn").intern(), true) ? String.valueOf("Music: ON").intern() : String.valueOf("Music: OFF").intern();
                            this.setText(musicString);
                        }
                    }
                }
                setPressed(isPressed);
            }
        };
        this.guiElements[7] = new GuiTextArea(11, this.guiElements[3].getWidth() / 2 - 135, 140, 270, 90) {
            @Override
            public boolean isVisible() {
                return popupOpened == 2;
            }

            @Override
            public void printContent(float[] mtrxProjectionAndView) {
                RenderString.renderString(String.valueOf("Made by:").intern(), overlayElement.getPosX() + 40, overlayElement.getPosY() + 40, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("Darek Stojaczyk").intern(), overlayElement.getPosX() + 48, overlayElement.getPosY() + 40 + 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("www.approach.tk").intern(), overlayElement.getPosX() + 48, overlayElement.getPosY() + 40 + 2 * 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("Music used:").intern(), overlayElement.getPosX() + 40, overlayElement.getPosY() + 47 + 3 * 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("Veneer").intern(), overlayElement.getPosX() + 48, overlayElement.getPosY() + 47 + 4 * 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("by Petriform").intern(), overlayElement.getPosX() + 48, overlayElement.getPosY() + 47 + 5 * 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("Thanks to").intern(), overlayElement.getPosX() + 40, overlayElement.getPosY() + 53 + 6 * 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("-").intern(), overlayElement.getPosX() + 48, overlayElement.getPosY() + 53 + 7 * 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("-").intern(), overlayElement.getPosX() + 48, overlayElement.getPosY() + 53 + 8 * 25, 0.75f, 1, mtrxProjectionAndView);
                RenderString.renderString(String.valueOf("-").intern(), overlayElement.getPosX() + 48, overlayElement.getPosY() + 53 + 9 * 25, 0.75f, 1, mtrxProjectionAndView);
            }
        };
        for (int i = 0; i < 3; i++) {
            guiRenders[i] = new RenderGuiButton((GuiButton) this.guiElements[i], new float[]{1f, 1f, 1f, 1.0f});
        }
        this.guiRenders[3] = new RenderOverlay(this.guiElements[3], new float[]{1f, 1f, 1f, 1f});
        for (int i = 4; i < 7; i++)
            this.guiRenders[i] = new RenderGuiButton((GuiButton) this.guiElements[i], new float[]{1f, 1f, 1f, 1f});
        this.guiRenders[7] = new RenderGuiTextArea((GuiTextArea) this.guiElements[7]);

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

