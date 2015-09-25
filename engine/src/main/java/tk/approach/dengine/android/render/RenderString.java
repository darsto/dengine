package tk.approach.dengine.android.render;

import android.opengl.Matrix;

import tk.approach.dengine.android.Constans;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class RenderString {
    private static RenderText letterRender;
    public static int displayHeight;
    private static float[] tempModelMatrix = new float[16];

    /*private static String[] letters = {
            "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "!", "?", "+", "-",
            "=", ":", ".", ",", "*", "$", "€", "@",
            "<", ">", "/"
    };*/

    public static void init() {
        letterRender = new RenderText(new float[]{1f, 1f, 1f});
    }

    //Two methods below are almost identical, but we cannot make one call the other, as that would increase memory usage (converting string to char array)

    /**
     * Renders String object at given position, with given scale and transparency.
     *
     * @param string
     * @param x
     * @param y
     * @param scale
     * @param alpha
     * @param mtrxProjectionAndView
     */
    public static void renderString(String string, float x, float y, float scale, float alpha, float[] mtrxProjectionAndView) {
        for (int i = 0; i < string.length(); i++) {
            int j = RenderText.convertCharToIndex((int) string.charAt(i));
            if (j == -1) {
                x += RenderText.RI_TEXT_SPACESIZE * scale;
                continue;
            }
            float tempRenderX = x * Constans.RENDER_SCALE;
            float tempRenderY = displayHeight - (y + RenderText.RI_TEXT_WIDTH * scale) * Constans.RENDER_SCALE;
            Matrix.setLookAtM(letterRender.getmModelMatrix(), 0, -tempRenderX, -tempRenderY, 1f, -tempRenderX, -tempRenderY, 0f, 0f, 1.0f, 0.0f);
            Matrix.scaleM(letterRender.getmModelMatrix(), 0, 32 * scale * Constans.RENDER_SCALE, 32 * scale * Constans.RENDER_SCALE, 0);
            Matrix.multiplyMM(tempModelMatrix, 0, mtrxProjectionAndView, 0, letterRender.getmModelMatrix(), 0);
            letterRender.justDraw(tempModelMatrix, j, alpha);
            x += ((RenderText.l_size[j] % 64 / 2) + 4) * scale;
        }
    }

    /**
     * Renders char array at given position, with given scale and transparency.
     *
     * @param string
     * @param x
     * @param y
     * @param scale
     * @param alpha
     * @param mtrxProjectionAndView
     */
    public static void renderString(char[] string, float x, float y, float scale, float alpha, float[] mtrxProjectionAndView) {
        for (char c : string) {
            if (c == '\u0000') continue;
            int j = RenderText.convertCharToIndex((int) c);
            if (j == -1) {
                x += RenderText.RI_TEXT_SPACESIZE * scale;
                continue;
            }
            float tempRenderX = x * Constans.RENDER_SCALE;
            float tempRenderY = displayHeight - (y + RenderText.RI_TEXT_WIDTH * scale) * Constans.RENDER_SCALE;
            Matrix.setLookAtM(letterRender.getmModelMatrix(), 0, -tempRenderX, -tempRenderY, 1f, -tempRenderX, -tempRenderY, 0f, 0f, 1.0f, 0.0f);
            Matrix.scaleM(letterRender.getmModelMatrix(), 0, 32 * scale * Constans.RENDER_SCALE, 32 * scale * Constans.RENDER_SCALE, 0);
            Matrix.multiplyMM(tempModelMatrix, 0, mtrxProjectionAndView, 0, letterRender.getmModelMatrix(), 0);
            letterRender.justDraw(tempModelMatrix, j, alpha);
            x += ((RenderText.l_size[j] % 64 / 2) + 4) * scale;
        }
    }
}
