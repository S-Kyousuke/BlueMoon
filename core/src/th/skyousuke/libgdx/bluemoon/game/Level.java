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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.MusicManager.Musics;
import th.skyousuke.libgdx.bluemoon.game.object.AbstractGameObject;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractMonster;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;
import th.skyousuke.libgdx.bluemoon.game.object.character.monsters.Slime;
import th.skyousuke.libgdx.bluemoon.game.object.character.players.Jane;
import th.skyousuke.libgdx.bluemoon.game.object.character.players.John;

public class Level {

    public TiledMap map;
    public int music;

    public AbstractPlayer john;
    public AbstractPlayer jane;
    public AbstractMonster slime;

    private Array<AbstractGameObject> allObjects;
    private ZOrderComparator zOrderComparator;

    public Level() {
        map = Assets.instance.mainMap;
        music = Musics.MUSIC_1;

        allObjects = new Array<>();
        zOrderComparator = new ZOrderComparator();
        final TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get(0);

        john = new John(tileLayer);
        jane = new Jane(tileLayer);
        slime = new Slime(tileLayer);
        john.setPosition(500, 500);
        jane.setPosition(700, 500);
        slime.setPosition(900, 500);

        allObjects.add(john);
        allObjects.add(jane);
        allObjects.add(slime);
    }


    public void update(float deltaTime) {
        for (AbstractGameObject object : allObjects) {
            object.update(deltaTime);
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
        drawDebug(shapeRenderer);
    }

    private void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeType.Line);
        for (AbstractGameObject o : allObjects) {
            final Rectangle bounds = o.getBounds();
            shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        shapeRenderer.end();
    }

    private static class ZOrderComparator implements Comparator<AbstractGameObject> {
        @Override
        public int compare(AbstractGameObject object1, AbstractGameObject object2) {
            return Float.compare(object2.getPosition().y, object1.getPosition().y);
        }
    }
}
