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

import java.io.IOException;
import java.util.Properties;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.GamePreferences;
import th.skyousuke.libgdx.bluemoon.framework.I18NManager;
import th.skyousuke.libgdx.bluemoon.framework.Language;
import th.skyousuke.libgdx.bluemoon.framework.Resolution;
import th.skyousuke.libgdx.bluemoon.framework.ResolutionManager;
import th.skyousuke.libgdx.bluemoon.screen.MenuScreen;

public class BlueMoon extends Game {

    public static final int SCENE_WIDTH = 1024;
    public static final int SCENE_HEIGHT = 576;

    @Override
    public void create() {
        setTitle();

        Assets.instance.init();

        GamePreferences.instance.load();
        ResolutionManager.instance.setResolution(Resolution.getValue(GamePreferences.instance.resolution));
        I18NManager.instance.setCurrentLanguage(Language.getValue(GamePreferences.instance.language));

        setGlobalInput();
        setScreen(new MenuScreen(this));
    }

    private void setTitle() {
        Properties gameProperties = new Properties();
        try {
            gameProperties.load(Gdx.files.internal("version.properties").read());
        } catch (IOException e) {
            return;
        }
        Gdx.graphics.setTitle("Blue Moon" + ' ' + gameProperties.getProperty("gameVersion"));
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
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

    private void setGlobalInput() {
        final InputMultiplexer inputMultiplexer = new InputMultiplexer();
        switch (Gdx.app.getType()) {
            case Desktop:
                inputMultiplexer.addProcessor(getDesktopInput());
                break;
        }
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

}
