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
import com.badlogic.gdx.InputAdapter;

import th.skyousuke.libgdx.bluemoon.utils.CameraHelper;
import th.skyousuke.libgdx.bluemoon.utils.Direction;

public class WorldController extends InputAdapter {

    public CameraHelper cameraHelper;
    public Level level;

    public WorldController() {
        init();
    }

    public void init() {
        cameraHelper = new CameraHelper();
        level = new Level();
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
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) level.player.move(Direction.UP);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) level.player.move(Direction.DOWN);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) level.player.move(Direction.LEFT);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) level.player.move(Direction.RIGHT);
        //if (Gdx.input.isKeyPressed(Input.Keys.Z));
        //if (Gdx.input.isKeyPressed(Input.Keys.X));
    }

    private void handleInput(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (cameraHelper.hasTarget())
                cameraHelper.setTarget(null);
            else
                cameraHelper.setTarget(level.player);
        }

        if (!cameraHelper.hasTarget()) handleInputCamera(deltaTime);
        else handleInputPlayer(deltaTime);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        level.update(deltaTime);
        cameraHelper.update(deltaTime);
    }

}
