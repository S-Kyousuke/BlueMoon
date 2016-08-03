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

import th.skyousuke.libgdx.bluemoon.BlueMoon;

/**
 * Game screen resolution in pixel.
 * Created by S. Kyousuke <surasek@gmail.com> on 6/8/2559.
 */
public class ResolutionManager implements GamePreferencesListener {

    public static final ResolutionManager instance = new ResolutionManager();

    private ResolutionManager() {
        GamePreferences.instance.addListener(this);
    }

    public void setResolution(Resolution resolution) {
        switch (resolution) {
            case FULLSCREEN:
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                break;
            case GAME_WORD:
                Gdx.graphics.setWindowedMode(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT);
                break;
            case HD:
                Gdx.graphics.setWindowedMode(1280, 720);
                break;
            case FULL_HD:
                Gdx.graphics.setWindowedMode(1920, 1080);
                break;
            default:
        }
    }

    @Override
    public void onGamePreferencesChange() {
        setResolution(Resolution.getValue(GamePreferences.instance.resolution));
    }
}
