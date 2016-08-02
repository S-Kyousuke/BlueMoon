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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import th.skyousuke.libgdx.bluemoon.framework.GamePreferences;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;

/**
 * Help window.
 * Created by S. Kyousuke <surasek@gmail.com> on 31/7/2559.
 */
public class HelpWindow extends Window {

    private Label tutorialText;

    public HelpWindow(Skin skin) {
        super(skin);
        tutorialText = LabelPool.obtainLabel();

        setColor(1, 1, 1, 0.8f);
        padLeft(10);
        padRight(10);
        padBottom(5);
        add(tutorialText).padTop(5);
    }

    public void initContent() {
        if (GamePreferences.instance.controlPlayer)
            tutorialText.setText(LanguageManager.instance.getText("playerTutorial"));
        else
            tutorialText.setText(LanguageManager.instance.getText("cameraTutorial"));
        setTitle(LanguageManager.instance.getText("helpWindowTitle"));
        pack();
    }

}
