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

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.IntMap;

/**
 * Manage game sound effect.
 * Created by S. Kyousuke <surasek@gmail.com> on 3/8/2559.
 */
public class SoundManager {

    public static final SoundManager instance = new SoundManager();

    public static class Sounds {
        public static final int BUTTON = 0;

    }

    private IntMap<Sound> sounds;

    public SoundManager() {
        sounds = new IntMap<>();
        sounds.put(Sounds.BUTTON, Assets.instance.buttonSound);
    }

    public void play(int id) {
        final GamePreferences preferences = GamePreferences.instance;
        if (preferences.sound) sounds.get(id).play(preferences.soundVolume);
    }

}
