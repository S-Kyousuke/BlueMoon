/*
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.skyousuke.libgdx.bluemoon.game.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import th.skyousuke.libgdx.bluemoon.framework.GamePreferences;
import th.skyousuke.libgdx.bluemoon.framework.I18NManager;
import th.skyousuke.libgdx.bluemoon.framework.Resolution;
import th.skyousuke.libgdx.bluemoon.framework.ResolutionManager;

/**
 * @author S.Kyousuke <surasek@gmail.com>
 */
public class ResolutionConfirmDialog extends Window {

    private static final int DURATION = 10;

    private final Action revertResolution = new Action() {
        @Override
        public boolean act(float delta) {
            GamePreferences.instance.resolution = oldResolution.ordinal();
            GamePreferences.instance.save();
            ResolutionManager.instance.setResolution(oldResolution);
            settingWindow.setVisible(false);
            remove();
            return true;
        }
    };

    private final Action updateCountLabel = new Action() {
        @Override
        public boolean act(float delta) {
            --timeRemaining;
            updateCountDown();
            return true;
        }
    };

    private SettingWindow settingWindow;
    private Resolution oldResolution;
    private int timeRemaining;

    private Label confirmQuestion;
    private Label countDown;
    private TextButton yesButton;
    private TextButton noButton;

    public ResolutionConfirmDialog(Skin skin, SettingWindow settingWindow, Resolution oldResolution) {
        super(skin, "noTitle");
        this.settingWindow = settingWindow;
        this.oldResolution = oldResolution;
        timeRemaining = DURATION;

        final I18NManager i18n = I18NManager.instance;
        confirmQuestion = LabelPool.obtainLabel(I18NManager.instance.getText("resolutionConfirm"));
        countDown = LabelPool.obtainLabel();
        yesButton = new TextButton(i18n.getText("yes"), skin);
        noButton = new TextButton(i18n.getText("no"), skin);

        updateCountDown();

        pad(16);
        add(confirmQuestion).padBottom(5).row();
        add(countDown).padBottom(10).row();
        add(createButtons());
        pack();

        addAction(Actions.sequence(Actions.repeat(DURATION, Actions.delay(1, updateCountLabel)), revertResolution));
        addListenerToButton();
    }

    private Table createButtons() {
        Table buttons = new Table();
        buttons.add(yesButton).width(60f).padRight(12f);
        buttons.add(noButton).width(60f);
        return buttons;
    }

    private void addListenerToButton() {
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                remove();
            }
        });
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clearActions();
                addAction(revertResolution);
            }
        });
    }

    private void updateCountDown() {
        countDown.setText(I18NManager.instance.getFormattedText("revertingResolutionCountdown",
                Integer.toString(timeRemaining)));
    }
}
