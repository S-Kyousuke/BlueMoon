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

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.I18NManager;
import th.skyousuke.libgdx.bluemoon.game.WorldTime;

/**
 * Game time window class
 * Created by S.Kyousuke <surasek@gmail.com> on 12/7/2559.
 */
public class TimeHud extends Window {

    private Image timeIcon;
    private Label dayLabel;
    private Label timeLabel;

    private int hours;
    private int days;
    private int minutes;

    public TimeHud(Skin skin) {
        super(skin, "noTitle");

        setBackground(Assets.instance.customSkin.getDrawable("uiBackgroundDraw"));
        timeIcon = ImagePool.obtainImage(Assets.instance.ui.findRegion("time_icon"));
        timeLabel = LabelPool.obtainLabel();
        dayLabel = LabelPool.obtainLabel();

        Table table = new Table();
        table.row();
        table.add(dayLabel);
        table.row();
        table.add(timeLabel);

        padLeft(12);
        padRight(8);
        row();
        add(timeIcon).padRight(5);
        add(table).padTop(5).padBottom(5).expandX().fillX();
    }

    public void init() {
        updateDayLabel();
        updateTimeLabel();
        pack();
        setWidth(110);
        alignToStage(getStage(), Align.topRight);
    }

    public void setTime(WorldTime time) {
        setTime(time.getDays(), time.getHours(), time.getMinutes());
    }

    public void setTime(int days, int hours, int minutes) {
        if (days != this.days) {
            this.days = days;
            updateDayLabel();
        }
        if (hours != this.hours || minutes != this.minutes) {
            this.hours = hours;
            this.minutes = minutes;
            updateTimeLabel();
        }
    }

    private void updateDayLabel() {
        dayLabel.setText(I18NManager.instance.getFormattedText("day", days));
    }

    private void updateTimeLabel() {
        timeLabel.setText("" + ((hours < 10) ? 0 : "") + hours + ':' + ((minutes < 10) ? 0 : "") + minutes);
    }

}
