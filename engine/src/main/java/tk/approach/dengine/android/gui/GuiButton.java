package tk.approach.dengine.android.gui;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class GuiButton extends GuiElement {

    private String text;

    public GuiButton(String text, float x, float y, float width, float height) {
        this(0, text, x, y, width, height);
    }

    public GuiButton(int order, String text, float x, float y, float width, float height) {
        this(order, text, null, x, y, width, height);
    }

    public GuiButton(String text, GuiElement parent, float x, float y, float width, float height) {
        this(0, text, parent, x, y, width, height);
    }

    public GuiButton(int order, String text, GuiElement parent, float x, float y, float width, float height) {
        super(order, parent, x, y, width, height);
        if (this.text == null)
            this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
