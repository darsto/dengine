package tk.approach.dengine.android;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class TouchPoint {

    private float x, y;
    private boolean toBeDeleted = false;
    private boolean isUsed;

    public TouchPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    public void setToBeDeleted(boolean toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }
}
