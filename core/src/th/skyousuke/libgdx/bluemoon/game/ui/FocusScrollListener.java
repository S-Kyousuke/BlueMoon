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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Automatically focus scroll when mouse cursor is entered or exited scroll pane.
 * Created by Skyousuke <surasek@gmail.com> on 12/7/2559.
 */
public class FocusScrollListener extends ClickListener {

    final ScrollPane scrollPane;

    public FocusScrollListener(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    /*
     * pointer = -1 for hover entering and exiting.
     * pointer = 0, 1, 2, ... etc. for touching events.
     */

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer == -1)
            scrollPane.getStage().setScrollFocus(scrollPane);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (pointer == -1)
            scrollPane.getStage().setScrollFocus(null);
    }
}
