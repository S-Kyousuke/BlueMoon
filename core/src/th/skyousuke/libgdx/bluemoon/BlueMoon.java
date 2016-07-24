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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.Language;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
import th.skyousuke.libgdx.bluemoon.screen.WorldScreen;

public class BlueMoon extends Game {

    public static final int SCENE_WIDTH = 1024;
    public static final int SCENE_HEIGHT = 576;

    private boolean fullscreen;

    @Override
    public void create() {
        fullscreen = false;
        Assets.instance.init();
        setGlobalInput();
        setScreen(new WorldScreen(this));
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    private void setGlobalInput() {
        final InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.F1:
                        if (LanguageManager.instance.getCurrentLanguage() == Language.THAI)
                            LanguageManager.instance.setCurrentLanguage(Language.ENGLISH);
                        else LanguageManager.instance.setCurrentLanguage(Language.THAI);
                        break;
                    case Keys.F2:
                        if (fullscreen) {
                            Gdx.graphics.setWindowedMode(SCENE_WIDTH, SCENE_HEIGHT);
                            fullscreen = false;
                        } else {
                            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                            fullscreen = true;
                        }
                        break;
                    case Keys.ESCAPE:
                        if (fullscreen && Gdx.app.getType() == ApplicationType.WebGL) {
                            fullscreen = false;
                        }
                        break;
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
