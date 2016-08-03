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

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntMap;

/**
 * Manage game music.
 * Created by S. Kyousuke <surasek@gmail.com> on 3/8/2559.
 */
public class MusicManager implements GamePreferencesListener {

    public static final MusicManager instance = new MusicManager();

    public static class Musics {
        public static final int MUSIC_1 = 0;
    }

    private IntMap<Music> musics;
    private Music currentMusic;
    private Music nextMusic;

    private MusicManager() {
        musics = new IntMap<>();
        musics.put(Musics.MUSIC_1, Assets.instance.music);

        GamePreferences.instance.addListener(this);
    }

    public void play(int id, boolean looping) {
        currentMusic = musics.get(id);
        currentMusic.setLooping(looping);
        play(currentMusic);
    }

    private void play(Music music) {
        if (!GamePreferences.instance.music) return;
        music.play();
        music.setOnCompletionListener(completeMusic -> {
            if (nextMusic != null) MusicManager.this.play(nextMusic);
        });
        updateVolume(MathUtils.clamp(GamePreferences.instance.musicVolume, 0, 1));
    }

    public void stop() {
        if (currentMusic != null) {
            currentMusic.setOnCompletionListener(null);
            currentMusic.stop();
        }
    }

    private void updateVolume(float volume) {
        currentMusic.setVolume(MathUtils.clamp(volume, 0, 1));
    }

    public void setNextMusic(int id) {
        nextMusic = musics.get(id);
    }

    @Override
    public void onGamePreferencesChange() {
        if (currentMusic == null) return;
        updateVolume(GamePreferences.instance.musicVolume);
        if (GamePreferences.instance.music) {
            play(currentMusic);
        }
        else stop();
    }
}
