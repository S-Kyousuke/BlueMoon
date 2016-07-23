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

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

/**
 * Window class that more lightweight than LibGDX window.
 * Created by S.Kyousuke <surasek@gmail.com> on 8/7/2559.
 */
public class Window extends Table {

    private final Label titleLabel;
    private boolean keepWithinStage;
    private boolean movable;
    private boolean dragging;

    public Window(Skin skin) {
        this("Untitled", skin);
    }

    public Window(String titleName, Skin skin) {
        titleLabel = new Label(titleName, skin);
        final WindowStyle style = skin.get(WindowStyle.class);

        setBackground(style.background);

        keepWithinStage = true;
        movable = true;

        setTouchable(Touchable.enabled);
        setClip(true);

        addListener(new DragActorListener(this) {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragging && movable) super.touchDragged(event, x, y, pointer);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (y >= getHeight() - getPadTop())
                    dragging = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragging = false;
            }
        });
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
        titleLabel.pack();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (keepWithinStage) keepWithinStage();
        super.draw(batch, parentAlpha);

        titleLabel.setPosition(
                getX(Align.left) + getPadLeft(),
                getY(Align.top) - getPadTop());
        titleLabel.draw(batch, parentAlpha);
    }

    private void keepWithinStage() {
        Stage stage = getStage();
        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        float parentWidth = stage.getWidth();
        float parentHeight = stage.getHeight();

        if (getX(Align.right) - camera.position.x > parentWidth / 2 / camera.zoom)
            setPosition(camera.position.x + parentWidth / 2 / camera.zoom, getY(Align.right), Align.right);
        if (getX(Align.left) - camera.position.x < -parentWidth / 2 / camera.zoom)
            setPosition(camera.position.x - parentWidth / 2 / camera.zoom, getY(Align.left), Align.left);
        if (getY(Align.top) - camera.position.y > parentHeight / 2 / camera.zoom)
            setPosition(getX(Align.top), camera.position.y + parentHeight / 2 / camera.zoom, Align.top);
        if (getY(Align.bottom) - camera.position.y < -parentHeight / 2 / camera.zoom)
            setPosition(getX(Align.bottom), camera.position.y - parentHeight / 2 / camera.zoom, Align.bottom);
    }

    public void setKeepWithinStage(boolean keepWithinStage) {
        this.keepWithinStage = keepWithinStage;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public static class WindowStyle {
        public Drawable background;
    }
}
