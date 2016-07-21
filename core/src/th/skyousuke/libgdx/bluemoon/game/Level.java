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

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.game.object.AbstractGameObject;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractMonster;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;
import th.skyousuke.libgdx.bluemoon.game.object.character.monsters.Slime;
import th.skyousuke.libgdx.bluemoon.game.object.character.players.Jane;
import th.skyousuke.libgdx.bluemoon.game.object.character.players.John;

public class Level {

    public AbstractPlayer john;
    public AbstractPlayer jane;
    public AbstractMonster slime;

    private TiledMap map;
    private Array<AbstractGameObject> allObjects;
    private ZOrderComparator zOrderComparator;

    public Level() {
        john = new John();
        jane = new Jane();
        slime = new Slime();
        john.setPosition(100, 0);
        jane.setPosition(200, 0);
        slime.setPosition(300, 0);

        slime.setMaxLinearSpeed(100);
        slime.setMaxLinearAcceleration(300);

        final Arrive<Vector2> arriveSB = new Arrive<>(slime, john)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0.001f)
                .setDecelerationRadius(30);
        slime.setSteeringBehavior(arriveSB);

        map = Assets.instance.mainMap;
        allObjects = new Array<>();
        zOrderComparator = new ZOrderComparator();

        allObjects.add(john);
        allObjects.add(jane);
        allObjects.add(slime);
    }

    public void update(float deltaTime) {
        for (AbstractGameObject o : allObjects) {
            o.update(deltaTime);
        }
        allObjects.sort(zOrderComparator);
    }

    public void render(SpriteBatch batch, OrthogonalTiledMapRenderer tiledRenderer, ShapeRenderer shapeRenderer) {
        tiledRenderer.setMap(map);
        tiledRenderer.render();

        batch.begin();
        for (AbstractGameObject o : allObjects) {
            o.render(batch);
        }
        batch.end();

        // for debugging only
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.end();
    }

    private static class ZOrderComparator implements Comparator<AbstractGameObject> {
        @Override
        public int compare(AbstractGameObject object1, AbstractGameObject object2) {
            return Float.compare(object2.getPosition().y, object1.getPosition().y);
        }
    }

}
