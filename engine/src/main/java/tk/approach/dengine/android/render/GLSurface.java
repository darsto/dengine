package tk.approach.dengine.android.render;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import tk.approach.dengine.android.DCore;

public class GLSurface extends GLSurfaceView {

    private DCore dcore;
    public final GLRenderer renderer;

    public GLSurface(Context context, DCore dcore, Resources resources, int[] textures) {
        super(context);
        this.dcore = dcore;
        setEGLContextClientVersion(2);
        renderer = new GLRenderer(dcore, resources, textures);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause() {
        //super.onPause();
        renderer.onPause();
    }

    @Override
    public void onResume() {
        //super.onResume();
        renderer.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        this.renderer.getTouchManager().processTouchEvent(e);
        return true;
    }

}
