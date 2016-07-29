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

package th.skyousuke.libgdx.bluemoon.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import th.skyousuke.libgdx.bluemoon.framework.GamePreferences;
import th.skyousuke.libgdx.bluemoon.framework.GamePreferencesListener;
import th.skyousuke.libgdx.bluemoon.game.WorldController;
import th.skyousuke.libgdx.bluemoon.game.WorldRenderer;
import th.skyousuke.libgdx.bluemoon.game.ui.WorldGui;

public class WorldScreen extends AbstractGameScreen implements GamePreferencesListener {

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private WorldGui worldGui;

    private boolean pause;
    private boolean ready;

    public WorldScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        pause = false;

        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
        worldGui = new WorldGui(worldController);

        GamePreferences.instance.addListener(this);
        applyControlSetting(GamePreferences.instance);
        applyMusicSetting(GamePreferences.instance);
    }

    @Override
    public void render(float deltaTime) {
        if (!pause) {
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            worldController.update(deltaTime);
            worldRenderer.render();

            worldGui.update(deltaTime);
            worldGui.render();
        } else if (ready) {
            pause = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
        worldGui.resize(width, height);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        worldGui.dispose();
        GamePreferences.instance.removeListener(this);
    }

    @Override
    public void pause() {
        pause = true;
        ready = false;
    }

    @Override
    public void resume() {
        ready = true;
    }

    @Override
    public void onGamePreferencesChange(GamePreferences gamePreferences) {
        applyControlSetting(gamePreferences);
        applyMusicSetting(gamePreferences);
        worldGui.initTutorialText(gamePreferences);
    }

    private void applyControlSetting(GamePreferences gamePreferences) {
        if (gamePreferences.controlPlayer)
            worldController.cameraHelper.setTarget(worldController.controlledPlayer);
        else
            worldController.cameraHelper.setTarget(null);
    }

    private void applyMusicSetting(GamePreferences gamePreferences) {
        if (gamePreferences.music) {
            worldController.level.music.play();
        }
        else
            worldController.level.music.stop();
        worldController.level.music.setVolume(gamePreferences.musicVolume);
    }

}
