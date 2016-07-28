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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.GamePreferences;
import th.skyousuke.libgdx.bluemoon.framework.GamePreferencesListener;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
import th.skyousuke.libgdx.bluemoon.screen.WorldScreen;

public class BlueMoon extends Game implements GamePreferencesListener {

    public static final int SCENE_WIDTH = 1024;
    public static final int SCENE_HEIGHT = 576;

    @Override
    public void create() {
        Assets.instance.init();

        GamePreferences.instance.load();
        GamePreferences.instance.addListener(this);
        setGameLanguage(GamePreferences.instance);
        setDisplayMode(GamePreferences.instance);

        setGlobalInput();
        setScreen(new WorldScreen(this));
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    private void setGameLanguage(GamePreferences gamePreferences) {
        LanguageManager.instance.setCurrentLanguage(gamePreferences.language);
    }

    private void setDisplayMode(GamePreferences gamePreferences) {
        if (gamePreferences.fullscreen)
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        else
            Gdx.graphics.setWindowedMode(SCENE_WIDTH, SCENE_HEIGHT);
    }

    @Override
    public void onGamePreferencesChange(GamePreferences gamePreferences) {
        setGameLanguage(gamePreferences);
        setDisplayMode(gamePreferences);
    }

    private InputAdapter getDesktopInput() {
        return new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        Gdx.app.exit();
                        break;
                }
                return true;
            }
        };
    }

    private InputAdapter getHtmlInput() {
        return new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        if (GamePreferences.instance.fullscreen) {
                            GamePreferences.instance.fullscreen = false;
                            GamePreferences.instance.save();
                        }
                        break;
                }
                return true;
            }
        };
    }

    private void setGlobalInput() {
        final InputMultiplexer inputMultiplexer = new InputMultiplexer();
        switch (Gdx.app.getType()) {
            case WebGL:
                inputMultiplexer.addProcessor(getHtmlInput());
                break;
            case Desktop:
                inputMultiplexer.addProcessor(getDesktopInput());
                break;
        }
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

}
