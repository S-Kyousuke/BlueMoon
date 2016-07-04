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

import com.badlogic.gdx.utils.Array;

/**
 * Game world time class
 * Created by Skyousuke <surasek@gmail.com> on 4/7/2559.
 */
public class WorldTime {

    public static final int SECONDS_PER_WORLD_DAY = 1440;

    public static final float SECONDS_PER_WORLD_HOUR = SECONDS_PER_WORLD_DAY / 24.0f;
    public static final float SECONDS_PER_WORLD_MINUTE = SECONDS_PER_WORLD_HOUR / 60.0f;
    public static float SECONDS_PER_WORLD_SECOND = SECONDS_PER_WORLD_MINUTE / 60.0f;
    private final Array<WorldListener> worldListeners;
    private int worldDay;
    private float dayTimeInSecond;

    public WorldTime() {
        worldDay = 1;
        worldListeners = new Array<>();
        setTime(6, 0, 0);
    }

    private void addDayTimeInSecond(float value) {
        dayTimeInSecond += value;
        if (dayTimeInSecond > SECONDS_PER_WORLD_DAY) {
            ++worldDay;
            dayTimeInSecond -= SECONDS_PER_WORLD_DAY;
        }
        for (WorldListener worldListener : worldListeners) {
            worldListener.onTimeChange(this);
        }
    }

    public void update(float deltaTime) {
        addDayTimeInSecond(deltaTime);
    }

    public void setTime(int hours, int minutes, int seconds) {
        dayTimeInSecond = 0;
        addHours(hours);
        addMinutes(minutes);
        addSeconds(seconds);
    }

    public void addDays(float value) {
        int integerValue = (int) value;
        float fractionalValue = value - integerValue;
        worldDay += integerValue;
        addDayTimeInSecond(fractionalValue * SECONDS_PER_WORLD_DAY);
    }

    public void addHours(float value) {
        addDayTimeInSecond(value * SECONDS_PER_WORLD_HOUR);
    }

    public void addMinutes(float value) {
        addDayTimeInSecond(value * SECONDS_PER_WORLD_MINUTE);
    }

    public void addSeconds(float value) {
        addDayTimeInSecond(value * SECONDS_PER_WORLD_SECOND);
    }

    public int getDay() {
        return worldDay;
    }

    public void setDay(int day) {
        worldDay = day;
    }

    public int getHours() {
        return (int) (dayTimeInSecond / SECONDS_PER_WORLD_HOUR);
    }

    public int getMintues() {
        return (int) (dayTimeInSecond / SECONDS_PER_WORLD_MINUTE) % 60;
    }

    public int getSeconds() {
        return (int) (dayTimeInSecond / SECONDS_PER_WORLD_SECOND) % 60;
    }

    public void addWorldListener(WorldListener worldListener) {
        worldListeners.add(worldListener);
    }

}
