package tk.approach.dengine.android.gui;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class GuiTextArea extends GuiElement {

    protected boolean isClickable;

    public GuiTextArea(float x, float y, float width, float height) {
        this(x, y, width, height, false);
    }

    public GuiTextArea(float x, float y, float width, float height, boolean clickable) {
        this(0, x, y, width, height);
    }

    public GuiTextArea(int order, float x, float y, float width, float height) {
        this(order, x, y, width, height, false);
    }

    public GuiTextArea(int order, float x, float y, float width, float height, boolean clickable) {
        this(order, null, x, y, width, height, clickable);
    }

    public GuiTextArea(int order, GuiElement parent, float x, float y, float width, float height) {
        this(order, parent, x, y, width, height, false);
    }

    public GuiTextArea(int order, GuiElement parent, float x, float y, float width, float height, boolean clickable) {
        super(order, parent, x, y, width, height);
        this.isClickable = clickable;
    }

    @Override
    public boolean onTouchEvent(float x, float y) {
        return this.isClickable && super.onTouchEvent(x, y);
    }

    public void printContent(float[] mtrxProjectionAndView) {
        //nothing
    }
}
