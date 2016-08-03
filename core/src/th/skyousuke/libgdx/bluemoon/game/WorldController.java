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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.utils.Array;

import th.skyousuke.libgdx.bluemoon.framework.CameraHelper;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;
import th.skyousuke.libgdx.bluemoon.game.object.character.MessageType;

public class WorldController extends InputAdapter {

    public Level level;
    public CameraHelper cameraHelper;
    public AbstractPlayer controlledPlayer;
    public MessageManager messageManager;

    private WorldTime worldTime;
    private Array<WorldListener> worldListeners;

    public WorldController() {
        init();
    }

    public void init() {
        level = new Level();
        worldTime = new WorldTime();
        worldListeners = new Array<>();
        cameraHelper = new CameraHelper();
        cameraHelper.setMapDimension(level.map);

        messageManager = MessageManager.getInstance();
        messageManager.setDebugEnabled(true);
        messageManager.addListener(level.slime, MessageType.SAY_MSG);

        controlledPlayer = level.john;
        cameraHelper.setTarget(controlledPlayer);
    }

    private void handleInputCamera(float deltaTime) {
        final float CAMERA_SPEED = 100f;
        final float CAMERA_ZOOM_SPEED = 1f;

        if (Gdx.input.isKeyPressed(Keys.UP)) cameraHelper.addPosition(0, CAMERA_SPEED * deltaTime);
        if (Gdx.input.isKeyPressed(Keys.DOWN)) cameraHelper.addPosition(0, -CAMERA_SPEED * deltaTime);
        if (Gdx.input.isKeyPressed(Keys.LEFT)) cameraHelper.addPosition(-CAMERA_SPEED * deltaTime, 0);
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) cameraHelper.addPosition(CAMERA_SPEED * deltaTime, 0);
        if (Gdx.input.isKeyPressed(Keys.Z)) cameraHelper.addZoom(CAMERA_ZOOM_SPEED * deltaTime);
        if (Gdx.input.isKeyPressed(Keys.X)) cameraHelper.addZoom(-CAMERA_ZOOM_SPEED * deltaTime);
    }

    private void handleInputPlayer() {
        controlledPlayer.handleInput();
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            swapPlayer();
            cameraHelper.setTarget(controlledPlayer);
        }
    }

    private void handleInput(float deltaTime) {
        if (!cameraHelper.hasTarget())
            handleInputCamera(deltaTime);
        else
            handleInputPlayer();

        if (Gdx.input.isKeyJustPressed(Keys.F2)) {
            if (!cameraHelper.hasTarget()) {
                cameraHelper.setTarget(controlledPlayer);
            }
            else cameraHelper.setTarget(null);
        }
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        GdxAI.getTimepiece().update(deltaTime);
        messageManager.update();
        level.update(deltaTime);
        cameraHelper.update();
        worldTime.update(deltaTime);
    }

    private void swapPlayer() {
        if (controlledPlayer == level.john) {
            controlledPlayer = level.jane;
            for (WorldListener worldListener : worldListeners) {
                worldListener.onPlayerChange(level.john, level.jane);
            }
        } else {
            controlledPlayer = level.john;
            for (WorldListener worldListener : worldListeners) {
                worldListener.onPlayerChange(level.jane, level.john);
            }
        }
    }

    public void addListener(WorldListener listener) {
        worldListeners.add(listener);
        worldTime.addListener(listener);
    }

    public void removeListener(WorldListener listener) {
        worldListeners.removeValue(listener, true);
        worldTime.removeListener(listener);
    }

}
