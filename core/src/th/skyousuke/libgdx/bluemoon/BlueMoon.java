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

package th.skyousuke.libgdx.bluemoon;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.I18NBundle;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.screen.WorldScreen;

public class BlueMoon extends Game {

    public static final int SCENE_WIDTH = 1024;
    public static final int SCENE_HEIGHT = 576;

    @Override
    public void create() {
        I18NBundle.setSimpleFormatter(true);
        Assets.instance.init();
        setScreen(new WorldScreen(this));
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) && Gdx.app.getType() == ApplicationType.WebGL) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        super.render();
    }
}
