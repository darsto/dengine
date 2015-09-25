package tk.approach.dengine.android.render;

public interface Renderable {

    void draw(float[] m);

    void scale(Object e);

    float[] getmModelMatrix();
}
