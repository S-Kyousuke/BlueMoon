package th.skyousuke.libgdx.bluemoon.game.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import th.skyousuke.libgdx.bluemoon.framework.GamePreferences;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
import th.skyousuke.libgdx.bluemoon.framework.Languages;

/**
 * Option window class.
 * Created by S. Kyousuke <surasek@gmail.com> on 24/7/2559.
 */
public class SettingWindow extends Window {

    private Label languageLabel;
    private Label controlPlayerLabel;
    private Label fullscreenLabel;
    private Label soundLabel;
    private Label musicLabel;
    private TextButton okButton;
    private TextButton cancelButton;

    private int languageSetting;
    private boolean controlPlayerSetting;
    private boolean fullscreenSetting;
    private boolean soundSetting;
    private boolean musicSetting;

    public SettingWindow(Skin skin) {
        super("", skin);
        setColor(1, 1, 1, 0.8f);

        languageLabel = LabelPool.obtainLabel();
        controlPlayerLabel = LabelPool.obtainLabel();
        fullscreenLabel = LabelPool.obtainLabel();
        soundLabel = LabelPool.obtainLabel();
        musicLabel = LabelPool.obtainLabel();
        okButton = new TextButton("", skin);
        cancelButton = new TextButton("", skin);

        addListenerToLabel();
        addListenerToButton();


        row().align(Align.left).padTop(5f);
        add(languageLabel).colspan(2);
        row().align(Align.left).colspan(2);
        add(controlPlayerLabel).align(Align.left).row();
        row().align(Align.left).colspan(2);
        add(fullscreenLabel);
        row().align(Align.left).colspan(2);
        add(soundLabel);
        row().align(Align.left).colspan(2);
        add(musicLabel);
        row().uniform().padTop(10f);
        add(okButton).expandX().fillX().width(60f);
        add(cancelButton).expandX().fillX().width(60f);

        padLeft(10f);
        padRight(10f);
        padBottom(10f);
    }

    public void initContent() {
        okButton.setText(LanguageManager.instance.getText("ok"));
        cancelButton.setText(LanguageManager.instance.getText("cancel"));
        setTitle(LanguageManager.instance.getText("settingWindowTitle"));

        updateLabel();
        pack();
        setWidth(160f);
    }

    public void readSettings() {
        final GamePreferences preferences = GamePreferences.instance;
        languageSetting = preferences.language;
        controlPlayerSetting = preferences.controlPlayer;
        fullscreenSetting = preferences.fullscreen;
        soundSetting = preferences.sound;
        musicSetting = preferences.music;
        updateLabel();
    }

    private void saveSettings() {
        GamePreferences preferences = GamePreferences.instance;
        preferences.language = languageSetting;
        preferences.controlPlayer = controlPlayerSetting;
        preferences.fullscreen = fullscreenSetting;
        preferences.sound = soundSetting;
        preferences.music = musicSetting;
        preferences.save();
    }

    private void addListenerToLabel() {
        addListenerToLanguageLabel();
        addListenerToControlPlayerLabel();
        addListenerToFullscreenLabel();
        addListenerToSoundLabel();
        addListenerToMusicLabel();
    }

    private void addListenerToButton() {
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveSettings();
                setVisible(false);
            }
        });
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });
    }

    private void addListenerToLanguageLabel() {
        addHoverEffectTo(languageLabel);
        languageLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (languageSetting == Languages.ENGLISH) {
                    languageSetting = Languages.THAI;
                } else languageSetting = Languages.ENGLISH;
                updateLanguageLabel();
                return true;
            }
        });
    }

    private void addListenerToSoundLabel() {
        addHoverEffectTo(soundLabel);
        soundLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                soundSetting = !soundSetting;
                updateSoundLabel();
                return true;
            }
        });
    }

    private void addListenerToMusicLabel() {
        addHoverEffectTo(musicLabel);
        musicLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                musicSetting = !musicSetting;
                updateMusicLabel();
                return true;
            }
        });
    }

    private void addListenerToControlPlayerLabel() {
        addHoverEffectTo(controlPlayerLabel);
        controlPlayerLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controlPlayerSetting = !controlPlayerSetting;
                updateControlPlayerLabel();
                return true;
            }
        });
    }

    private void addListenerToFullscreenLabel() {
        addHoverEffectTo(fullscreenLabel);
        fullscreenLabel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                fullscreenSetting = !fullscreenSetting;
                updateFullscreenLabel();
                return true;
            }
        });
    }

    private void addHoverEffectTo(final Label label) {
        label.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                label.setColor(Color.GOLD);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                label.setColor(Color.WHITE);
            }
        });
    }

    public void updateLabel() {
        updateLanguageLabel();
        updateControlPlayerLabel();
        updateFullscreenLabel();
        updateSoundLabel();
        updateMusicLabel();
    }

    private void updateLanguageLabel() {
        languageLabel.setText(LanguageManager.instance.getText("languageSettingLabel")
                + ": " + Languages.toString(languageSetting));
    }

    // @formatter:off
    private void updateControlPlayerLabel() {
        controlPlayerLabel.setText(LanguageManager.instance.getText("controlSettingLabel")
                + ": " + (controlPlayerSetting ? LanguageManager.instance.getText("player")
                                               : LanguageManager.instance.getText("camera")));
    }

    private void updateFullscreenLabel() {
        fullscreenLabel.setText(LanguageManager.instance.getText("fullscreenSettingLabel")
                + ": " + (fullscreenSetting ? LanguageManager.instance.getText("turnOn")
                                            : LanguageManager.instance.getText("turnOff")));
    }

    private void updateSoundLabel() {
        soundLabel.setText(LanguageManager.instance.getText("soundSettingLabel")
                + ": " + (soundSetting ? LanguageManager.instance.getText("turnOn")
                                       : LanguageManager.instance.getText("turnOff")));
    }

    private void updateMusicLabel() {
        musicLabel.setText(LanguageManager.instance.getText("musicSettingLabel")
                + ": " + (musicSetting ? LanguageManager.instance.getText("turnOn")
                                       : LanguageManager.instance.getText("turnOff")));
    }

}
