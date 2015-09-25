package tk.approach.dengine.android;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import tk.approach.dengine.R;
import tk.approach.dengine.android.activatable.ActivatableHandler;
import tk.approach.dengine.android.render.GLSurface;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public abstract class DActivity extends Activity {

    protected ActivityManager activityManager;
    protected DCore dcore;
    protected GLSurface glSurface;
    protected static final int DOUBLE_CLICK_TIME_INTERVAL = 2000;
    private long mBackPressed;
    private ResourceHandler resourceHandler;
    private ActivatableHandler activatableHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        Init window
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        FrameLayout layout = (FrameLayout) findViewById(R.id.layout_main);
        /*
        Custom, extended initialization
         */
        this.init();
        /*
        Setup activity manager
         */
        this.activityManager = (ActivityManager) this.getApplicationContext();
        /*
        Music init
         */
        int[] bgm = this.getResourceHandler().getMusics();
        AssetFileDescriptor[] afd = new AssetFileDescriptor[bgm.length];
        for (int i = 0; i < bgm.length; i++) {
            afd[i] = getResources().openRawResourceFd(bgm[i]);
        }
        /*
        Sound init
         */
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        SoundManager soundManager = new SoundManager();
        soundManager.initSounds(this, this.getResourceHandler().getSounds());
        /*
        Game init
         */
        dcore = new DCore(this.activityManager, Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID), new PreferencesSettings(getPreferences(MODE_PRIVATE)), this.getActivatableHandler(), MediaPlayer.create(getApplicationContext(), bgm[0]), afd, soundManager);
        /*
        GLSurface init
         */
        glSurface = new GLSurface(this, dcore, this.getResources(), this.getResourceHandler().getTextures());
        /*
        Add GLSurface to screen
         */
        layout.addView(glSurface);
    }

    protected abstract void init();

    @Override
    protected void onResume() {
        super.onResume();
        activityManager.setCurrentActivity(this);
        glSurface.onResume();
        if (dcore.getSettings().getBoolean("musicOn", true)) {
            dcore.setMusicState(true);
        }
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
        glSurface.onPause();
        dcore.setMusicState(false);
    }

    @Override
    public void onBackPressed() {
        this.doubleClickCheck();
    }

    protected void doubleClickCheck() {
        if (mBackPressed + DOUBLE_CLICK_TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    private void clearReferences() {
        Activity currActivity = activityManager.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            activityManager.setCurrentActivity(null);
        }
    }

    @Override
    public void onDestroy() {
        clearReferences();
        super.onDestroy();

        this.dcore.stopRunning();
        this.glSurface = null;
        this.dcore = null;
    }

    protected ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    protected void setResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    public ActivatableHandler getActivatableHandler() {
        return activatableHandler;
    }

    public void setActivatableHandler(ActivatableHandler activatableHandler) {
        this.activatableHandler = activatableHandler;
    }
}