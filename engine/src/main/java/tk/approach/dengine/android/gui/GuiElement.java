package tk.approach.dengine.android.gui;

import tk.approach.dengine.android.Constans;
import tk.approach.dengine.android.TouchPoint;
import android.support.annotation.NonNull;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class GuiElement implements Comparable<GuiElement> {

    protected byte order;
    protected final GuiElement parent;
    protected float x, y;
    protected float width, height;
    protected boolean isPressed;
    protected boolean isVisible;

    public GuiElement(float x, float y, float width, float height) {
        this(0, x, y, width, height);
    }

    public GuiElement(int order, float x, float y, float width, float height) {
        this(order, null, x, y, width, height);
    }

    public GuiElement(GuiElement parent, float x, float y, float width, float height) {
        this(0, parent, x, y, width, height);
    }

    public GuiElement(int order, GuiElement parent, float x, float y, float width, float height) {
        this.order = (byte) order;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isVisible = true;
        this.postInit();
    }

    protected void postInit() {
        //nothing
    }

    public float getPosX() {
        return x + (this.parent != null ? this.parent.getPosX() : 0);
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getPosY() {
        return y + (this.parent != null ? this.parent.getPosY() : 0);
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isPressed() {
        return this.isPressed;
    }

    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isEnabled) {
        this.isVisible = isEnabled;
    }

    public GuiElement getParent() {
        return this.parent;
    }

    public boolean onTouchEvent(float x, float y) {
        return this.isVisible() && (x > (this.getPosX()) * Constans.RENDER_SCALE && y > (this.getPosY()) * Constans.RENDER_SCALE && x < (this.getPosX() + this.getWidth()) * Constans.RENDER_SCALE && y < (this.getPosY() + this.getHeight()) * Constans.RENDER_SCALE);
    }

    public void setPressState(boolean isPressed, TouchPoint o) {
        this.isPressed = isPressed;
    }

    public byte getOrder() {
        return order;
    }

    public void setOrder(byte order) {
        this.order = order;
    }

    @Override
    public int compareTo(@NonNull GuiElement another) {
        return this.order > another.order ? -1 : (this.order == another.order ? 0 : 1);
    }
}
