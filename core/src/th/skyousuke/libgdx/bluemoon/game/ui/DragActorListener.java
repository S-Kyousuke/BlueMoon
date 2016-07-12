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
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Causes an actor to move along with the mouse cursor (or touch) when an actor is being dragged.
 * Created by Skyousuke <surasek@gmail.com> on 6/7/2559.
 */
public class DragActorListener extends InputListener {

    private final Actor actor;
    private float startX;
    private float startY;

    public DragActorListener(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        actor.moveBy(x - startX, y - startY);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        startX = x;
        startY = y;
        actor.toFront();
        return true;
    }
}
