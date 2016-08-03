package th.skyousuke.libgdx.bluemoon.game.ui;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.GamePreferences;
import th.skyousuke.libgdx.bluemoon.framework.I18NManager;
import th.skyousuke.libgdx.bluemoon.framework.Language;
import th.skyousuke.libgdx.bluemoon.framework.Resolution;

/**
 * Option window class.
 * Created by S. Kyousuke <surasek@gmail.com> on 24/7/2559.
 */
public class SettingWindow extends Window {

    private Label languageTitle;
    private Label resolutionTitle;
    private SelectBox<Language> language;
    private SelectBox<Resolution> resolution;
    private CheckBox soundCheckBox;
    private CheckBox musicCheckBox;
    private Slider soundSlider;
    private Slider musicSlider;
    private TextButton okButton;
    private TextButton cancelButton;

    private Resolution oldResolution;

    public SettingWindow(Skin skin) {
        super(skin);

        languageTitle = LabelPool.obtainLabel();
        resolutionTitle = LabelPool.obtainLabel();

        language = new SelectBox<>(skin);
        final Array<Language> languages = new Array<>();
        languages.add(Language.ENGLISH);
        languages.add(Language.THAI);
        language.setItems(languages);

        resolution = new SelectBox<>(skin);
        final Array<Resolution> resolutions = new Array<>();
        resolutions.add(Resolution.FULLSCREEN);
        resolutions.add(Resolution.GAME_WORD);
        resolution.setItems(resolutions);

        musicCheckBox = new CheckBox("", skin);
        soundCheckBox = new CheckBox("", skin);
        musicSlider = new Slider(0, 1, 0.1f, false, skin);
        soundSlider = new Slider(0, 1, 0.1f, false, skin);
        okButton = new TextButton("", skin);
        cancelButton = new TextButton("", skin);

        addListenerToButton();

        padLeft(20);
        padRight(20);
        padBottom(20);
        row().expandX().padTop(20);
        add(createDisplaySetting());
        row().expandX().padTop(12);
        add(createAudioSetting());
        row().expandX().padTop(12);
        add(createButtons());
    }

    public void init() {
        updateLabel();
        pack();
        alignToStage(getStage(), Align.center);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            readSettings();
            alignToStage(getStage(), Align.center);
        }
        super.setVisible(visible);
    }

    public void readSettings() {
        final GamePreferences preferences = GamePreferences.instance;
        language.setSelected(Language.getValue(preferences.language));
        resolution.setSelected(Resolution.getValue(GamePreferences.instance.resolution));
        soundCheckBox.setChecked(preferences.sound);
        musicCheckBox.setChecked(preferences.music);
        soundSlider.setValue(preferences.soundVolume);
        musicSlider.setValue(preferences.musicVolume);
        oldResolution = resolution.getSelected();
    }

    private void saveSettings() {
        GamePreferences preferences = GamePreferences.instance;
        final Resolution currentResolution =  resolution.getSelected();
        preferences.language = language.getSelected().ordinal();
        preferences.resolution = currentResolution.ordinal();
        preferences.sound = soundCheckBox.isChecked();
        preferences.music = musicCheckBox.isChecked();
        preferences.soundVolume = soundSlider.getValue();
        preferences.musicVolume = musicSlider.getValue();
        preferences.save();
        if (currentResolution != oldResolution) {
            ResolutionConfirmDialog confirmDialog =
                    new ResolutionConfirmDialog(Assets.instance.customSkin, this, oldResolution);
            confirmDialog.alignToStage(getStage(), Align.center);
            getStage().addActor(confirmDialog);
        }
    }

    private void addListenerToButton() {
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveSettings();
                setVisible(false);
            }
        });
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setVisible(false);
            }
        });
    }

    public void updateLabel() {
        final I18NManager i18n = I18NManager.instance;
        setTitle(i18n.getText("settingWindowTitle"));
        languageTitle.setText(i18n.getText("language"));
        resolutionTitle.setText(i18n.getText("resolution"));
        soundCheckBox.setText(' ' + i18n.getText("soundSetting"));
        musicCheckBox.setText(' ' + i18n.getText("musicSetting"));
        okButton.setText(i18n.getText("ok"));
        cancelButton.setText(i18n.getText("cancel"));
    }

    private Table createDisplaySetting() {
        Table displaySetting = new Table();
        displaySetting.row().expandX().align(Align.left);
        displaySetting.add(languageTitle).padRight(10);
        displaySetting.add(language).width(100).expandX();
        displaySetting.row().expandX().align(Align.left).padTop(8);
        displaySetting.add(resolutionTitle).padRight(10);
        displaySetting.add(resolution).width(100).expandX();
        return displaySetting;
    }

    private Table createAudioSetting() {
        Table audioSetting = new Table();
        audioSetting.row().expandX().align(Align.left);
        audioSetting.add(soundCheckBox).padRight(10);
        audioSetting.add(soundSlider).width(100).expandX();
        audioSetting.row().expandX().align(Align.left);
        audioSetting.add(musicCheckBox).padRight(10);
        audioSetting.add(musicSlider).width(100).expandX();
        return audioSetting;
    }

    private Table createButtons() {
        Table buttons = new Table();
        buttons.add(okButton).width(60f).padRight(12f);
        buttons.add(cancelButton).width(60f);
        return buttons;
    }

}
