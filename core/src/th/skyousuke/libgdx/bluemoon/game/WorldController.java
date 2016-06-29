/*
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;
import th.skyousuke.libgdx.bluemoon.utils.CameraHelper;

public class WorldController extends InputAdapter {

    public CameraHelper cameraHelper;
    public Level level;
    public AbstractPlayer controlledPlayer;

    public WorldController() {
        init();
    }

    public void init() {
        cameraHelper = new CameraHelper();
        level = new Level();
        controlledPlayer = level.player;
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

    private void handleInputPlayer(float deltaTime) {
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
            if (controlledPlayer == level.player)
                controlledPlayer = level.player2;
            else
                controlledPlayer = level.player;
            cameraHelper.setTarget(controlledPlayer);
        }
        if (!cameraHelper.hasTarget())
            handleInputCamera(deltaTime);
        else
            handleInputPlayer(deltaTime);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        level.update(deltaTime);
        cameraHelper.update(deltaTime);
    }

}
