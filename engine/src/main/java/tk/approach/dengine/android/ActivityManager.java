package tk.approach.dengine.android;

import android.app.Activity;
import android.app.Application;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class ActivityManager extends Application {

    private Activity mCurrentActivity = null;

    public void onCreate() {
        super.onCreate();
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
}