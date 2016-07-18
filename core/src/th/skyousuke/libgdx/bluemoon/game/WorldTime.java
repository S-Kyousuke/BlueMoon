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
 * Game world time class.
 * Time can be negative. (It's just reference.)
 * Created by Skyousuke <surasek@gmail.com> on 4/7/2559.
 */
public class WorldTime {

    public static final float WORLD_SECOND_TO_REAL_SECOND = 1 / 60f;

    public static final float WORLD_MINUTE_TO_REAL_SECOND = 60 * WORLD_SECOND_TO_REAL_SECOND;
    public static final float WORLD_HOUR_TO_REAL_SECOND = 60 * WORLD_MINUTE_TO_REAL_SECOND;
    public static final float WORLD_DAY_TO_REAL_SECOND = 24 * WORLD_HOUR_TO_REAL_SECOND;

    private Array<WorldListener> worldListeners;

    /*for caching only*/
    private int worldDays;
    private int worldHours;
    private int worldMinutes;
    private float worldSeconds;

    public WorldTime() {
        this(1, 6, 0, 0);
    }

    public WorldTime(int day, int hours, int minutes, float seconds) {
        setTime(day, hours, minutes, seconds);
    }

    public WorldTime setTime(int days, int hours, int minutes, float seconds) {
        if (hours < 0 || hours > 24)
            throw new IllegalArgumentException("Invalid hour setting: hours = " + hours);
        if (minutes < 0 || minutes > 60)
            throw new IllegalArgumentException("Invalid minute setting: minutes = " + minutes);
        if (seconds < 0 || seconds > 60)
            throw new IllegalArgumentException("Invalid second setting: seconds = " + seconds);

        worldDays = days;
        worldHours = hours;
        worldMinutes = minutes;
        worldSeconds = seconds;

        if (worldListeners != null) {
            for (WorldListener worldListener : worldListeners) {
                worldListener.onTimeChange(this);
            }
        }
        return this;
    }

    public WorldTime addDays(float days) {
        return addHours(days * 24);
    }

    public WorldTime addHours(float hours) {
        return addMinutes(hours * 60);
    }

    public WorldTime addMinutes(float minutes) {
        return addSeconds(minutes * 60);
    }

    public WorldTime addSeconds(float seconds) {
        float totalSeconds = worldSeconds + seconds;
        while (totalSeconds < 0) {
            totalSeconds += 86400;
            --worldDays;
        }
        final int minutesWithCarry = worldMinutes + (int) (totalSeconds / 60);
        final int hoursWithCarry = worldHours + minutesWithCarry / 60;

        return setTime(worldDays + (hoursWithCarry / 24),
                hoursWithCarry % 24,
                minutesWithCarry % 60,
                totalSeconds % 60);
    }

    public final int getDays() {
        return worldDays;
    }

    public WorldTime setDays(int days) {
        return setTime(days, worldHours, worldMinutes, worldSeconds);
    }

    public final int getHours() {
        return worldHours;
    }

    public WorldTime setHours(int hours) {
        return setTime(worldDays, hours, worldMinutes, worldSeconds);
    }

    public final int getMinutes() {
        return worldMinutes;
    }

    public WorldTime setMinutes(int minutes) {
        return setTime(worldDays, worldHours, minutes, worldSeconds);
    }

    public final float getSeconds() {
        return worldSeconds;
    }

    public WorldTime setSeconds(float seconds) {
        return setTime(worldDays, worldHours, worldMinutes, seconds);
    }

    public void addListener(WorldListener listener) {
        if (worldListeners == null) {
            worldListeners = new Array<>();
        }
        worldListeners.add(listener);
    }

    public void removeListener(WorldListener listener) {
        if (worldListeners != null)
            worldListeners.removeValue(listener, true);
    }

    public void update(float realDeltaTime) {
        addSeconds(realDeltaTime / WORLD_SECOND_TO_REAL_SECOND);
    }

}
