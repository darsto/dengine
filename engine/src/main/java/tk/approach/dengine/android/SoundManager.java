package tk.approach.dengine.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import tk.approach.dengine.R;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class SoundManager {

    private SoundPool soundPool;
    private int[] soundIDs;
    boolean loaded = false;

    /**
     * Universal SoundPool wrapper.
     * If Build.Version.SDK_INT >= 21 SoundPool.Builder() is used, (Deprecated) SoundPool() otherwise.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SoundManager() {
        if ((android.os.Build.VERSION.SDK_INT) >= 21) {
            this.soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        } else {
            this.soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
    }

    /**
     * Initializes all given sounds.
     *
     * @param context
     * @param sounds array of sound resources
     */
    public void initSounds(Context context, int[] sounds) {
        this.soundIDs = new int[sounds.length];
        for (int i = 0; i < sounds.length; i++) {
            this.soundIDs[i] = this.soundPool.load(context, sounds[i], 1);
        }
    }

    /**
     * Plays a sound with given id and default (loudest) volumes.
     * @param soundIndex index in pre-initialized sound array
     */
    public void playSound(int soundIndex) {
        this.playSound(soundIndex, 1, 1);
    }

    /**
     * Plays a sound with a given id and left/right volumes.
     *
     * @param soundIndex index in pre-initialized sound array
     * @param left left volume value (range = 0.0 to 1.0)
     * @param right right volume value (range = 0.0 to 1.0)
     */
    public void playSound(int soundIndex, float left, float right) {
        if (this.loaded && this.soundIDs[soundIndex] != 0) {
            this.soundPool.play(this.soundIDs[soundIndex], left, right, 0, 0, 1);
        }
    }
}
