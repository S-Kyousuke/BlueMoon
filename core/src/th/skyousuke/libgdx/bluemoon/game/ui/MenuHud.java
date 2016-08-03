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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.SoundManager;
import th.skyousuke.libgdx.bluemoon.framework.SoundManager.Sounds;

/**
 * Menu head-up display.
 * Created by S. Kyousuke <surasek@gmail.com> on 31/7/2559.
 */
public class MenuHud extends Table {

    private Button characterIcon;
    private Button inventoryIcon;
    private Button helpIcon;
    private Button settingIcon;

    public MenuHud() {
        ButtonStyle buttonStyle;
        buttonStyle = new ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(Assets.instance.ui.findRegion("character_icon"));
        buttonStyle.over = new TextureRegionDrawable(Assets.instance.ui.findRegion("character_icon_over"));
        buttonStyle.down = buttonStyle.up;
        characterIcon = new Button(buttonStyle);

        buttonStyle = new ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(Assets.instance.ui.findRegion("inventory_icon"));
        buttonStyle.over = new TextureRegionDrawable(Assets.instance.ui.findRegion("inventory_icon_over"));
        buttonStyle.down = buttonStyle.up;
        inventoryIcon = new Button(buttonStyle);

        buttonStyle = new ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(Assets.instance.ui.findRegion("help_icon"));
        buttonStyle.over = new TextureRegionDrawable(Assets.instance.ui.findRegion("help_icon_over"));
        buttonStyle.down = buttonStyle.up;
        helpIcon = new Button(buttonStyle);

        buttonStyle = new ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(Assets.instance.ui.findRegion("setting_icon"));
        buttonStyle.over = new TextureRegionDrawable(Assets.instance.ui.findRegion("setting_icon_over"));
        buttonStyle.down = buttonStyle.up;
        settingIcon = new Button(buttonStyle);

        setBackground(Assets.instance.customSkin.getDrawable("uiBackgroundDraw"));
        row().pad(5);
        add(characterIcon).padLeft(10);
        add(inventoryIcon);
        add(helpIcon);
        add(settingIcon).padRight(10);
        pack();
    }

    public void setCharacterWindow(Window window) {
        addWindowToggleListener(characterIcon, window);
    }

    public void setInventoryWindow(Window window) {
        addWindowToggleListener(inventoryIcon, window);
    }

    public void setHelpWindow(Window window) {
        addWindowToggleListener(helpIcon, window);
    }

    public void setSettingWindow(Window window) {
        addWindowToggleListener(settingIcon, window);
    }

    private void addWindowToggleListener(Button button, final Window window) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.setVisible(!window.isVisible());
                window.toFront();
                SoundManager.instance.play(Sounds.BUTTON);
            }
        });
    }

}
