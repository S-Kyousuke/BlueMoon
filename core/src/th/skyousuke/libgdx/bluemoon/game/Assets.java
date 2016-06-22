/****************************************************************
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ****************************************************************/

package th.skyousuke.libgdx.bluemoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final Assets instance = new Assets();

    public AssetManager manager;

    public TextureAtlas player1Altas;
    public TextureAtlas player2Altas;
    public TextureAtlas player3Altas;
    public TextureAtlas player4Altas;

    public TextureAtlas villager1Altas;
    public TextureAtlas villager2Altas;
    public TextureAtlas villager3Altas;
    public TextureAtlas villager4Altas;

    public TextureAtlas monster1Altas;
    public TextureAtlas monster2Altas;
    public TextureAtlas monster3Altas;
    public TextureAtlas monster4Altas;

    private Assets() {
        manager = new AssetManager();
        manager.setErrorListener(this);
        manager.setLoader(TiledMap.class, new TmxMapLoader());

        //load assets here
        player1Altas = manager.get("", TextureAtlas.class);
        player2Altas = manager.get("", TextureAtlas.class);
        player3Altas = manager.get("", TextureAtlas.class);
        player4Altas = manager.get("", TextureAtlas.class);

        villager1Altas = manager.get("", TextureAtlas.class);
        villager2Altas = manager.get("", TextureAtlas.class);
        villager3Altas = manager.get("", TextureAtlas.class);
        villager4Altas = manager.get("", TextureAtlas.class);

        monster1Altas = manager.get("", TextureAtlas.class);
        monster2Altas = manager.get("", TextureAtlas.class);
        monster3Altas = manager.get("", TextureAtlas.class);
        monster4Altas = manager.get("", TextureAtlas.class);

        manager.finishLoading();

        //get assets here
        player1Altas = manager.get("");
        player2Altas = manager.get("");
        player3Altas = manager.get("");
        player4Altas = manager.get("");

        villager1Altas = manager.get("");
        villager2Altas = manager.get("");
        villager3Altas = manager.get("");
        villager4Altas = manager.get("");

        monster1Altas = manager.get("");
        monster2Altas = manager.get("");
        monster3Altas = manager.get("");
        monster4Altas = manager.get("");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error("Assets", "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

}
