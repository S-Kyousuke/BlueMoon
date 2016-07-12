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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final Assets instance = new Assets();

    public AssetManager manager;

    public TextureAtlas johnAltas;
    public TextureAtlas janeAltas;
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

    public TiledMap mainMap;

    public BitmapFont thaiFont24;

    public Skin skin;
    public Skin customSkin;

    private Assets() {
    }

    public void init() {
        manager = new AssetManager();
        manager.setErrorListener(this);
        manager.setLoader(TiledMap.class, new TmxMapLoader());

        //load assets here
        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);

        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);

        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);
        manager.load("images/characters/character.atlas", TextureAtlas.class);

        manager.load("maps/main.tmx", TiledMap.class);

        manager.load("fonts/thai24.fnt", BitmapFont.class);

        manager.load("skins/uiskin.json", Skin.class, new SkinParameter("skins/uiskin.atlas"));
        manager.load("skins/custom_skin.json", Skin.class, new SkinParameter("skins/custom_skin.atlas"));

        manager.finishLoading();

        //get assets here
        johnAltas = manager.get("images/characters/character.atlas");
        janeAltas = manager.get("images/characters/character.atlas");
        player3Altas = manager.get("images/characters/character.atlas");
        player4Altas = manager.get("images/characters/character.atlas");

        villager1Altas = manager.get("images/characters/character.atlas");
        villager2Altas = manager.get("images/characters/character.atlas");
        villager3Altas = manager.get("images/characters/character.atlas");
        villager4Altas = manager.get("images/characters/character.atlas");

        monster1Altas = manager.get("images/characters/character.atlas");
        monster2Altas = manager.get("images/characters/character.atlas");
        monster3Altas = manager.get("images/characters/character.atlas");
        monster4Altas = manager.get("images/characters/character.atlas");

        mainMap = manager.get("maps/main.tmx");

        thaiFont24 = manager.get("fonts/thai24.fnt");

        skin = manager.get("skins/uiskin.json");
        skin.getFont("default-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        customSkin = manager.get("skins/custom_skin.json");
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
