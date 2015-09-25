package tk.approach.dengine.android;

/**
 * A part of DEngine project.
 * Created by Darek Stojaczyk.
 */
public interface UserSettings {

    long getLong(String setting, long baseValue);

    float getFloat(String setting, float baseValue);

    int getInt(String setting, int baseValue);

    boolean getBoolean(String setting, boolean baseValue);

    void setLong(String setting, long value);

    void setFloat(String setting, float value);

    void setInt(String setting, int value);

    void setBoolean(String setting, boolean value);
}
