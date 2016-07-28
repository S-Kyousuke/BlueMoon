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
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class Assets implements Disposable, AssetErrorListener {

    public static final Assets instance = new Assets();

    public Music music;

    public TextureAtlas johnAltas;
    public TextureAtlas janeAltas;

    public TextureAtlas villager1Altas;
    public TextureAtlas slimeAltas;

    public TiledMap mainMap;

    public Skin skin;
    public Skin customSkin;

    public I18NBundle thaiLanguage;
    public I18NBundle englishLanguage;

    private AssetManager manager;

    private Assets() {
    }

    public void init() {
        manager = new AssetManager();
        manager.setErrorListener(this);
        manager.setLoader(TiledMap.class, new TmxMapLoader());

        /* load assets here */
        manager.load("music/music.mp3", Music.class);

        manager.load("characters/character.atlas", TextureAtlas.class);

        manager.load("maps/main.tmx", TiledMap.class);

        manager.load("skins/uiskin.json", Skin.class, new SkinParameter("skins/uiskin.atlas"));
        manager.load("skins/custom_skin.json", Skin.class, new SkinParameter("skins/custom_skin.atlas"));

        manager.load("i18n/strings_th_TH", I18NBundle.class, new I18NBundleParameter(new Locale("th", "TH")));
        manager.load("i18n/strings_en_US", I18NBundle.class, new I18NBundleParameter(Locale.US));

        manager.finishLoading();

        /* get assets here */
        music = manager.get("music/music.mp3");

        johnAltas = manager.get("characters/character.atlas");
        janeAltas = manager.get("characters/character.atlas");

        villager1Altas = manager.get("characters/character.atlas");

        slimeAltas = manager.get("characters/character.atlas");

        mainMap = manager.get("maps/main.tmx");

        skin = manager.get("skins/uiskin.json");
        customSkin = manager.get("skins/custom_skin.json");

        thaiLanguage = manager.get("i18n/strings_th_TH");
        englishLanguage = manager.get("i18n/strings_en_US");
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
