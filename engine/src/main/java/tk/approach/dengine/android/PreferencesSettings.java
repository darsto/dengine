package tk.approach.dengine.android;

import android.content.SharedPreferences;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public class PreferencesSettings implements UserSettings {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

    public PreferencesSettings(SharedPreferences preferences) {
        this.preferences = preferences;
        this.preferencesEditor = preferences.edit();
    }

    @Override
    public long getLong(String setting, long baseValue) {
        return this.preferences.getLong(setting, baseValue);
    }

    @Override
    public float getFloat(String setting, float baseValue) {
        return this.preferences.getFloat(setting, baseValue);
    }

    @Override
    public int getInt(String setting, int baseValue) {
        return this.preferences.getInt(setting, baseValue);
    }

    @Override
    public boolean getBoolean(String setting, boolean baseValue) {
        return this.preferences.getBoolean(setting, baseValue);
    }

    @Override
    public void setLong(String setting, long value) {
        this.preferencesEditor.putLong(setting, value);
        this.saveChanges();
    }

    @Override
    public void setFloat(String setting, float value) {
        this.preferencesEditor.putFloat(setting, value);
        this.saveChanges();
    }

    @Override
    public void setInt(String setting, int value) {
        this.preferencesEditor.putInt(setting, value);
        this.saveChanges();
    }

    @Override
    public void setBoolean(String setting, boolean value) {
        this.preferencesEditor.putBoolean(setting, value);
        this.saveChanges();
    }

    private void saveChanges() {
        this.preferencesEditor.commit();
    }
}
