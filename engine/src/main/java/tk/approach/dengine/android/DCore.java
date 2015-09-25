package tk.approach.dengine.android;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import tk.approach.dengine.android.activatable.*;
import tk.approach.dengine.android.render.GLRenderer;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class DCore {

    private boolean running = true;
    private GLRenderer glRenderer;
    private ActivityManager activityManager;
    private float displayWidth;
    private float displayHeight;
    private Activatable currentWindow;
    private ReentrantLock renderLock;
    private Timer timer;
    private ActivatableHandler activatableHandler;
    private UserSettings settings;
    private final String deviceID;
    private final MediaPlayer mediaPlayer;
    private final SoundManager soundManager;
    private Random rand;
    private AssetFileDescriptor[] bgm;

    private float leftMusicVol, rightMusicVol;


    public DCore(ActivityManager activityManager, String deviceID, UserSettings settings, ActivatableHandler activatableHandler, MediaPlayer mediaPlayer, AssetFileDescriptor[] bgm, SoundManager soundManager) {
        this.activityManager = activityManager;
        this.renderLock = new ReentrantLock();
        this.timer = new Timer();
        this.rand = new Random();
        this.activatableHandler = activatableHandler;
        this.deviceID = deviceID;
        this.settings = settings;
        this.bgm = bgm;
        this.mediaPlayer = mediaPlayer;
        this.mediaPlayer.setLooping(true);
        setMusicState(this.getSettings().getBoolean("musicOn", true));
        this.soundManager = soundManager;
    }

    public float getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(float displayWidth) {
        this.displayWidth = displayWidth;
    }

    public float getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(float displayHeight) {
        this.displayHeight = displayHeight;
    }

    public void setGLRenderer(GLRenderer glRenderer) {
        this.glRenderer = glRenderer;
    }

    public GLRenderer getGLRenderer() {
        return this.glRenderer;
    }

    public Activatable getCurrentWindow() {
        return this.currentWindow;
    }

    public void setCurrentWindow(int id) {
        Activatable currentWindow = getActivatableHandler().getActivatable(id);
        this.renderLock.lock();
        this.currentWindow = currentWindow;
        this.currentWindow.init();
        this.getGLRenderer().onSurfaceChanged(null, (int) this.getDisplayWidth(), (int) this.getDisplayHeight());
        this.renderLock.unlock();
    }

    public ActivityManager getActivityManager() {
        return this.activityManager;
    }

    public boolean isRenderLockOn() {
        return this.renderLock.isLocked();
    }

    public ReentrantLock getRenderLock() {
        return this.renderLock;
    }

    public Timer getTimer() {
        return this.timer;
    }



    public UserSettings getSettings() {
        return this.settings;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public void setMusicState(boolean playing) {
        if (playing && !this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.start();
        } else if (!playing && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
        }
    }

    public Random getRand() {
        return this.rand;
    }

    public void playSound(int soundId, float left, float right) {
        if (this.getSettings().getBoolean("soundsOn", true)) {
            this.soundManager.playSound(soundId, left, right);
        }
    }

    public void playSound(int soundId) {
        this.playSound(soundId, 1, 1);
    }

    public boolean isRunning() {
        return this.running;
    }

    public void stopRunning() {
        this.running = false;
        this.getTimer().cancel();
        this.getTimer().purge();
    }

    public void setMusicVolume(float left, float right) {
        this.leftMusicVol = left;
        this.rightMusicVol = right;
        this.mediaPlayer.setVolume(left, right);
    }

    public float getLeftMusicVol() {
        return this.leftMusicVol;
    }

    public float getRightMusicVol() {
        return this.rightMusicVol;
    }

    public void playMusic(int resid) {
        AssetFileDescriptor afd = this.bgm[resid];
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            this.mediaPlayer.setLooping(true);
            setMusicState(this.getSettings().getBoolean("musicOn", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public ActivatableHandler getActivatableHandler() {
        return activatableHandler;
    }
}
