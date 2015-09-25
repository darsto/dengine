package tk.approach.dengine.android.render;

import tk.approach.dengine.android.gui.GuiTextArea;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class RenderGuiTextArea extends RenderGuiElement {

    protected final GuiTextArea textArea;

    public RenderGuiTextArea(GuiTextArea textArea) {
        this.guiElement = this.textArea = textArea;
    }

    @Override
    public void draw(float[] mtrxProjectionAndView) {
        if (this.textArea.isVisible())
            this.textArea.printContent(mtrxProjectionAndView);

    }
}
