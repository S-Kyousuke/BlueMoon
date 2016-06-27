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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import th.skyousuke.libgdx.bluemoon.game.object.AbstractGameObject;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;
import th.skyousuke.libgdx.bluemoon.game.object.character.players.Player1;

public class Level {

    public AbstractPlayer player;

    private TiledMap map;
    private Array<AbstractGameObject> allObjects;
    private ZOrderComparator zOrderComparator;

    public Level() {
        player = new Player1();

        map = Assets.instance.mainMap;
        allObjects = new Array<>();
        zOrderComparator = new ZOrderComparator();
    }

    public void update(float deltaTime) {

        // add logic here
        //...
        player.update(deltaTime);

        // add all object here
        //...
        //allObjects.add(player);
        allObjects.sort(zOrderComparator);
    }

    public void render(SpriteBatch batch, OrthogonalTiledMapRenderer tiledRenderer, ShapeRenderer shapeRenderer) {
        tiledRenderer.setMap(map);
        tiledRenderer.render();

        batch.begin();
        for (AbstractGameObject o : allObjects) {
            o.render(batch);
        }
        player.render(batch);
        batch.end();

        // for debugging only
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.end();

    }

    private static class ZOrderComparator implements Comparator<AbstractGameObject> {
        @Override
        public int compare(AbstractGameObject object1, AbstractGameObject object2) {
            return Float.compare(object1.getPosition().y, object2.getPosition().y);
        }
    }

}
