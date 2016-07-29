package th.skyousuke.libgdx.bluemoon.game.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private CheckBox soundCheckBox;
    private CheckBox musicCheckBox;
    private Slider soundSlider;
    private Slider musicSlider;
    private TextButton okButton;
    private TextButton cancelButton;

    private int languageSetting;
    private boolean controlPlayerSetting;
    private boolean fullscreenSetting;

    public SettingWindow(Skin skin) {
        super("", skin);
        setColor(1, 1, 1, 0.8f);

        languageLabel = LabelPool.obtainLabel();
        controlPlayerLabel = LabelPool.obtainLabel();
        fullscreenLabel = LabelPool.obtainLabel();

        musicCheckBox = new CheckBox("", skin);
        soundCheckBox = new CheckBox("", skin);
        musicSlider = new Slider(0, 1, 0.1f, false, skin);
        soundSlider = new Slider(0, 1, 0.1f, false, skin);
        okButton = new TextButton("", skin);
        cancelButton = new TextButton("", skin);

        addListenerToLabel();
        addListenerToButton();

        row().align(Align.left).padTop(5f);
        add(languageLabel);
        row().align(Align.left);
        add(controlPlayerLabel).align(Align.left).row();
        row().align(Align.left);
        add(fullscreenLabel);
        row().align(Align.left);
        add(createSoundSetting()).expandX().fillX();
        row().align(Align.left);
        add(createMusicSetting()).expandX().fillX();
        row().padTop(10f);
        add(createButtons());

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
        //setWidth(300f);
    }

    public void readSettings() {
        final GamePreferences preferences = GamePreferences.instance;
        languageSetting = preferences.language;
        controlPlayerSetting = preferences.controlPlayer;
        fullscreenSetting = preferences.fullscreen;
        soundCheckBox.setChecked(preferences.sound);
        musicCheckBox.setChecked(preferences.music);
        soundSlider.setValue(preferences.soundVolume);
        musicSlider.setValue(preferences.musicVolume);
        updateLabel();
    }

    private void saveSettings() {
        GamePreferences preferences = GamePreferences.instance;
        preferences.language = languageSetting;
        preferences.controlPlayer = controlPlayerSetting;
        preferences.fullscreen = fullscreenSetting;
        preferences.sound = soundCheckBox.isChecked();
        preferences.music = musicCheckBox.isChecked();
        preferences.soundVolume = soundSlider.getValue();
        preferences.musicVolume = musicSlider.getValue();
        preferences.save();
    }

    private void addListenerToLabel() {
        addListenerToLanguageLabel();
        addListenerToControlPlayerLabel();
        addListenerToFullscreenLabel();
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
        updateSoundCheckBox();
        updateMusicCheckBox();
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

    private void updateSoundCheckBox() {
        soundCheckBox.setText(' ' + LanguageManager.instance.getText("soundSetting"));
    }

    private void updateMusicCheckBox() {
        musicCheckBox.setText(' ' + LanguageManager.instance.getText("musicSetting"));
    }

    private Table createSoundSetting() {
        Table soundSetting = new Table();
        soundSetting.add(soundCheckBox).padRight(10f);
        soundSetting.add(soundSlider).width(100).expandX().align(Align.right);
        return soundSetting;
    }

    private Table createMusicSetting() {
        Table musicSetting = new Table();
        musicSetting.add(musicCheckBox).padRight(10f);
        musicSetting.add(musicSlider).width(100).expandX().align(Align.right);
        return musicSetting;
    }

    private Table createButtons() {
        Table buttons = new Table();
        buttons.add(okButton).width(60f).padRight(10f);
        buttons.add(cancelButton).width(60f);
        return buttons;
    }

}
