package tk.approach.dengine.android.render;

import android.opengl.Matrix;

import tk.approach.dengine.android.Constans;
import tk.approach.dengine.android.gui.GuiElement;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class RenderOverlay extends RenderGuiElement {

    private GuiElement button;
    private RenderGuiElement[] renders;
    private float[] tempModelMatrix = new float[16];

    public RenderOverlay(GuiElement button, float[] color) {
        this.guiElement = this.button = button;
        this.color = color;
        this.renders = new RenderGuiElement[9];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                this.renders[y * 3 + x] = new RenderGuiElement(button, 45 + 8 * y + x, color, false);
            }
        }

    }

    @Override
    public void draw(float[] mtrxProjectionAndView) {
        if (this.button.isVisible()) {
            float scale = 90;
            float width = this.button.getWidth() / scale;
            float height = this.button.getHeight() / scale;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float tempRenderX = this.button.getPosX() * Constans.RENDER_SCALE;
                    float tempRenderY = RenderString.displayHeight - (this.button.getPosY() + this.button.getHeight()) * Constans.RENDER_SCALE;
                    tempRenderX += x * scale * Constans.RENDER_SCALE;
                    tempRenderY += (Math.ceil(height - 1) - y) * scale * Constans.RENDER_SCALE;
                    int render = getRenderByCoords(x, y, (int) Math.ceil(width), (int) Math.ceil(height));
                    Matrix.setLookAtM(this.renders[render].getmModelMatrix(), 0, -tempRenderX, -tempRenderY, 1f, -tempRenderX, -tempRenderY, 0f, 0f, 1.0f, 0.0f);
                    Matrix.scaleM(this.renders[render].getmModelMatrix(), 0, scale * Constans.RENDER_SCALE, scale * Constans.RENDER_SCALE, Constans.RENDER_SCALE);
                    Matrix.multiplyMM(tempModelMatrix, 0, mtrxProjectionAndView, 0, this.renders[render].getmModelMatrix(), 0);
                    this.renders[render].justDraw(tempModelMatrix);
                }
            }
        }
    }

    @Override
    public float[] getmModelMatrix() {
        return new float[0];
    }

    private int getRenderByCoords(int x, int y, int width, int height) {
        if (x == 0 && y == 0) return 0;
        else if (x != width - 1 && y == 0) return 1;
        else if (y == 0) return 2;
        else if (x == 0 && y != height - 1) return 3;
        else if (x == width - 1 && y != height - 1) return 5;
        else if (x == 0 && y == height - 1) return 6;
        else if (x != width - 1 && y == height - 1) return 7;
        else if (y == height - 1) return 8;
        else return 4;
    }
}
