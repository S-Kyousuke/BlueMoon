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

package th.skyousuke.libgdx.bluemoon.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.I18NManager;

public class MenuScreen extends AbstractGameScreen {

    private Stage stage;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT));

        Label label = new Label("", Assets.instance.customSkin);
        label.setText(I18NManager.instance.getText("clickToContinue"));
        label.pack();
        label.setPosition(
                stage.getWidth() / 2 - label.getWidth() / 2,
                stage.getHeight() / 2 - label.getHeight() / 2);

        stage.addActor(label);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isTouched()) {
            game.setScreen(new WorldScreen(game));
            return;
        }

        if (!pause) {
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            stage.act(delta);
            stage.draw();
        } else if (ready) {
            pause = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

}
