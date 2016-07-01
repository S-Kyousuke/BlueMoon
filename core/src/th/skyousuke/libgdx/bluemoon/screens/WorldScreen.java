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

package th.skyousuke.libgdx.bluemoon.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import th.skyousuke.libgdx.bluemoon.game.WorldController;
import th.skyousuke.libgdx.bluemoon.game.WorldGui;
import th.skyousuke.libgdx.bluemoon.game.WorldRenderer;

public class WorldScreen extends AbstractGameScreen {

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private WorldGui worldGui;


    public WorldScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
        worldGui = new WorldGui(worldController);

        worldController.setListener(worldGui);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldController.update(deltaTime);
        worldRenderer.render();

        worldGui.update(deltaTime);
        worldGui.render();
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
    }

}
