package tk.approach.dengine.android.activatable;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public interface Activatable {

    void init();

    void render(float[] mtrxProjection, float[] mtrxView, float[] mtrxProjectionAndView);

    void reload();

    void pause();

    void resume();
}
