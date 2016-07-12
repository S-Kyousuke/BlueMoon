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

package th.skyousuke.libgdx.bluemoon.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.game.WorldTime;

/**
 * Game time window class
 * Created by Skyousuke <surasek@gmail.com> on 12/7/2559.
 */
public class TimeWindow extends Window {

    private Label timeLabel;

    public TimeWindow(Skin skin) {
        super(skin);
        init();
    }

    public void init() {
        timeLabel = new Label("Day 1\n%24:%00d", Assets.instance.skin);
        timeLabel.setAlignment(Align.center);
        row().align(Align.left);
        add(timeLabel).expand().fill();
        pack();
        setWidth(80f);
        setMovable(false);

        setTitle("Time");
        padLeft(10f);
        padRight(10f);
    }

    public void setTime(WorldTime time) {
        setTime(time.getDay(), time.getHours(), time.getMintues());
    }

    public void setTime(int day, int hours, int minutes) {
        timeLabel.setText(String.format("Day %d\n%02d:%02d", day, hours, minutes));
    }

}
