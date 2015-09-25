package tk.approach.dengine.android.render;

import android.opengl.Matrix;

import tk.approach.dengine.android.Constans;
import tk.approach.dengine.android.gui.GuiButton;
import tk.approach.dengine.android.gui.GuiElement;

/**
 * Created by Darek Stojaczyk on 17/01/2015.
 */
public class RenderGuiButton extends RenderGuiElement {

    private GuiButton button;
    private RenderGuiElement[] renders;

    public RenderGuiButton(GuiButton button, float[] color) {
        this.guiElement = this.button = button;
        this.color = color;
        this.renders = new RenderGuiElement[3];
        for (int i = 0; i < this.renders.length; i++)
            this.renders[i] = new RenderGuiElement(button, 5 + i, color);

    }

    private float[] tempModelMatrix = new float[16];
    private float tempRenderX, tempRenderY;

    @Override
    public void draw(float[] mtrxProjectionAndView) {
        if (this.button.isVisible()) {
            tempRenderX = this.button.getPosX() * Constans.RENDER_SCALE;
            tempRenderY = RenderString.displayHeight - (this.button.getPosY() + this.button.getHeight()) * Constans.RENDER_SCALE;
            Matrix.setLookAtM(this.renders[0].getmModelMatrix(), 0, -this.tempRenderX, -this.tempRenderY, 1f, -this.tempRenderX, -this.tempRenderY, 0f, 0f, 1.0f, 0.0f);
            Matrix.scaleM(this.renders[0].getmModelMatrix(), 0, (this.button.getWidth() % this.button.getHeight() + this.button.getHeight()) * Constans.RENDER_SCALE, this.button.getHeight() * Constans.RENDER_SCALE, Constans.RENDER_SCALE);
            Matrix.multiplyMM(tempModelMatrix, 0, mtrxProjectionAndView, 0, this.renders[0].getmModelMatrix(), 0);
            this.renders[0].justDraw(tempModelMatrix);
            tempRenderX += (this.button.getWidth() % this.button.getHeight() + this.button.getHeight()) * Constans.RENDER_SCALE;
            Matrix.setLookAtM(this.renders[1].getmModelMatrix(), 0, -this.tempRenderX, -this.tempRenderY, 1f, -this.tempRenderX, -this.tempRenderY, 0f, 0f, 1.0f, 0.0f);
            Matrix.scaleM(this.renders[1].getmModelMatrix(), 0, (this.button.getWidth() - 2 * (this.button.getWidth() % this.button.getHeight() + this.button.getHeight())) * Constans.RENDER_SCALE, this.button.getHeight() * Constans.RENDER_SCALE, Constans.RENDER_SCALE);
            Matrix.multiplyMM(tempModelMatrix, 0, mtrxProjectionAndView, 0, this.renders[1].getmModelMatrix(), 0);
            this.renders[1].justDraw(tempModelMatrix);
            tempRenderX = (this.button.getPosX() + this.button.getWidth() - (this.button.getWidth() % this.button.getHeight() + this.button.getHeight())) * Constans.RENDER_SCALE;
            Matrix.setLookAtM(this.renders[2].getmModelMatrix(), 0, -this.tempRenderX, -this.tempRenderY, 1f, -this.tempRenderX, -this.tempRenderY, 0f, 0f, 1.0f, 0.0f);
            Matrix.scaleM(this.renders[2].getmModelMatrix(), 0, (this.button.getWidth() % this.button.getHeight() + this.button.getHeight()) * Constans.RENDER_SCALE, this.button.getHeight() * Constans.RENDER_SCALE, Constans.RENDER_SCALE);
            Matrix.multiplyMM(tempModelMatrix, 0, mtrxProjectionAndView, 0, this.renders[2].getmModelMatrix(), 0);
            this.renders[2].justDraw(tempModelMatrix);
            RenderString.renderString(this.button.getText(), this.button.getPosX() + (this.button.getWidth() - RenderText.getWidth(this.button.getText()) * 0.9f) / 2, this.button.getPosY() + (this.button.getHeight() - 32 * 0.9f) / 2, 0.9f, 1, mtrxProjectionAndView);
        }
    }

    @Override
    public void scale(Object e) {
        Matrix.scaleM(this.getmModelMatrix(), 0, ((GuiElement) e).getHeight() * Constans.RENDER_SCALE, ((GuiElement) e).getHeight() * Constans.RENDER_SCALE, Constans.RENDER_SCALE);
    }

    @Override
    public float[] getmModelMatrix() {
        return new float[0];
    }
}
