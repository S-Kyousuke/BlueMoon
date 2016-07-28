package th.skyousuke.libgdx.bluemoon.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

/**
 * Game preferences class.
 * Created by S. Kyousuke <surasek@gmail.com> on 28/7/2559.
 */
public class GamePreferences {

    public static final GamePreferences instance = new GamePreferences();

    public int language;
    public boolean controlPlayer;
    public boolean fullscreen;
    public boolean sound;
    public boolean music;
    public float soundVolume;
    public float musicVolume;

    private Preferences preferences;
    private Array<GamePreferencesListener> listeners;

    private GamePreferences() {
        preferences = Gdx.app.getPreferences("gamePreferences");
        listeners = new Array<>();
    }

    public void addListener(GamePreferencesListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GamePreferencesListener listener) {
        listeners.removeValue(listener, true);
    }

    public void load() {
        language = preferences.getInteger("language", Languages.THAI);
        controlPlayer = preferences.getBoolean("controlPlayer", true);
        fullscreen = preferences.getBoolean("fullscreen", false);
        sound = preferences.getBoolean("sound", true);
        music = preferences.getBoolean("music", true);
        soundVolume = preferences.getFloat("soundVolume", 1.0f);
        musicVolume = preferences.getFloat("musicVolume", 1.0f);
    }

    public void save() {
        preferences.putInteger("language", language);
        preferences.putBoolean("controlPlayer", controlPlayer);
        preferences.putBoolean("fullscreen", fullscreen);
        preferences.putBoolean("sound", sound);
        preferences.putBoolean("music", music);
        preferences.putFloat("soundVolume", soundVolume);
        preferences.putFloat("musicVolume", musicVolume);
        preferences.flush();

        for (GamePreferencesListener listener : listeners) {
            listener.onGamePreferencesChange(instance);
        }
    }

}
