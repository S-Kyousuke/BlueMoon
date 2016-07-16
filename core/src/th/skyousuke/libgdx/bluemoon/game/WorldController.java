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

package th.skyousuke.libgdx.bluemoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;

import th.skyousuke.libgdx.bluemoon.framework.CameraHelper;
import th.skyousuke.libgdx.bluemoon.framework.Language;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;

public class WorldController extends InputAdapter {

    public Level level;
    public CameraHelper cameraHelper;
    public AbstractPlayer controlledPlayer;

    private WorldTime worldTime;
    private Array<WorldListener> worldListeners;

    public WorldController() {
        init();
    }

    public void init() {
        level = new Level();
        cameraHelper = new CameraHelper();
        controlledPlayer = level.John;
        cameraHelper.setTarget(controlledPlayer);

        worldTime = new WorldTime();
        worldListeners = new Array<>();
    }

    private void handleInputCamera(float deltaTime) {
        final float CAMERA_SPEED = 100f;
        final float CAMERA_ZOOM_SPEED = 1f;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) cameraHelper.addPostion(0, CAMERA_SPEED * deltaTime);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) cameraHelper.addPostion(0, -CAMERA_SPEED * deltaTime);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) cameraHelper.addPostion(-CAMERA_SPEED * deltaTime, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cameraHelper.addPostion(CAMERA_SPEED * deltaTime, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) cameraHelper.addZoom(CAMERA_ZOOM_SPEED * deltaTime);
        if (Gdx.input.isKeyPressed(Input.Keys.X)) cameraHelper.addZoom(-CAMERA_ZOOM_SPEED * deltaTime);
    }

    private void handleInputPlayer() {
        controlledPlayer.handleInput();
    }

    private void handleInput(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            if (cameraHelper.hasTarget())
                cameraHelper.setTarget(null);
            else
                cameraHelper.setTarget(controlledPlayer);
        }
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            if (!cameraHelper.hasTarget()) return;
            swapPlayer();
            cameraHelper.setTarget(controlledPlayer);
        }
        if (!cameraHelper.hasTarget())
            handleInputCamera(deltaTime);
        else
            handleInputPlayer();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) LanguageManager.instance.setCurrentLanguage(Language.THAI);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) LanguageManager.instance.setCurrentLanguage(Language.ENGLISH);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        level.update(deltaTime);
        cameraHelper.update(deltaTime);
        worldTime.update(deltaTime);
    }

    public void swapPlayer() {
        if (controlledPlayer == level.John) {
            controlledPlayer = level.Jane;
            for (WorldListener worldListener : worldListeners) {
                worldListener.onPlayerChange(level.John, level.Jane);
            }
        } else {
            controlledPlayer = level.John;
            for (WorldListener worldListener : worldListeners) {
                worldListener.onPlayerChange(level.Jane, level.John);
            }
        }
    }

    public void addListener(WorldListener listener) {
        worldListeners.add(listener);
        worldTime.addListener(listener);
    }

}
