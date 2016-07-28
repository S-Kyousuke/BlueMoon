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

package th.skyousuke.libgdx.bluemoon.framework;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import th.skyousuke.libgdx.bluemoon.game.object.AbstractGameObject;

public class CameraHelper {

    private final Vector2 position;
    private float zoom;
    private AbstractGameObject target;

    private int levelWidth;
    private int levelHeight;

    public CameraHelper() {
        position = new Vector2();
        zoom = 1.0f;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void addPosition(float x, float y) {
        this.position.add(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        float MAX_ZOOM_OUT = 1.35f;
        float MAX_ZOOM_IN = 0.25f;
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public void applyTo(OrthographicCamera camera) {
        keepInLevel(camera);
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

    public AbstractGameObject getTarget() {
        return target;
    }

    public void setTarget(AbstractGameObject target) {
        this.target = target;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasTarget(AbstractGameObject target) {
        return this.target.equals(target);
    }

    public void update() {
        if (!hasTarget()) return;
        position.x = target.getPosition().x + target.getOrigin().x;
        position.y = target.getPosition().y + target.getOrigin().y;
    }

    private void keepInLevel(OrthographicCamera camera) {
        final float halfCameraWidth = camera.viewportWidth * 0.5f;
        final float halfCameraHeight = camera.viewportHeight * 0.5f;

        if (position.x > levelWidth - halfCameraWidth * zoom) {
            position.x = levelWidth - halfCameraWidth * zoom;
        } else if (position.x < halfCameraWidth * zoom)
            position.x = halfCameraWidth * zoom;

        if (position.y > levelHeight - halfCameraHeight * zoom)
            position.y = levelHeight - halfCameraHeight * zoom;
        else if (position.y < halfCameraHeight * zoom)
            position.y = halfCameraHeight * zoom;
    }

    public void setMapDimension(TiledMap map) {
        final MapProperties properties = map.getProperties();

        final int mapWidth = properties.get("width", Integer.class);
        final int mapHeight = properties.get("height", Integer.class);
        final int tilePixelWidth = properties.get("tilewidth", Integer.class);
        final int tilePixelHeight = properties.get("tileheight", Integer.class);

        levelWidth = mapWidth * tilePixelWidth;
        levelHeight = mapHeight * tilePixelHeight;
    }

}
