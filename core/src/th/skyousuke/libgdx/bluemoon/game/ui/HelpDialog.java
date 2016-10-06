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

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.I18NManager;

/**
 * Help window.
 * Created by S. Kyousuke <surasek@gmail.com> on 31/7/2559.
 */
public class HelpDialog extends Window {

    public HelpDialog(Skin skin) {
        super(skin, "noTitle");
        setMovable(false);
        pad(20);
    }

    public void init() {
        clear();
        final TextureAtlas uiAtlas = Assets.instance.ui;
        final I18NManager i18n = I18NManager.instance;
        Table table = new Table();
        table.add(new Image(uiAtlas.findRegion("button_up"))).colspan(3);
        table.row().padTop(5);
        table.add(new Image(uiAtlas.findRegion("button_left")));
        table.add(new Image(uiAtlas.findRegion("button_down"))).padLeft(5);
        table.add(new Image(uiAtlas.findRegion("button_right"))).padLeft(5);
        add(table).padRight(16);
        add(new Label(i18n.getText("moveCharacter"), Assets.instance.customSkin)).align(Align.left);
        row().padTop(10);
        add(new Image(uiAtlas.findRegion("button_z"))).padRight(16);
        add(new Label(i18n.getText("run"), Assets.instance.customSkin)).align(Align.left);
        row().padTop(10);
        add(new Image(uiAtlas.findRegion("button_x"))).padRight(16);
        add(new Label(i18n.getText("interact"), Assets.instance.customSkin)).align(Align.left);
        row().padTop(10);
        add(new Image(uiAtlas.findRegion("button_c"))).padRight(16);
        add(new Label(i18n.getText("attack"), Assets.instance.customSkin)).align(Align.left);
        row().padTop(10);
        add(new Image(uiAtlas.findRegion("button_space"))).padRight(16);
        add(new Label(i18n.getText("swapCharacter"), Assets.instance.customSkin)).align(Align.left);
        pack();
        alignToStage(getStage(), Align.center);
    }

}
