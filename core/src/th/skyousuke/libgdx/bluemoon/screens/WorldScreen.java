/*
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.skyousuke.libgdx.bluemoon.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.game.Assets;
import th.skyousuke.libgdx.bluemoon.game.WorldController;
import th.skyousuke.libgdx.bluemoon.game.WorldRenderer;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;

public class WorldScreen extends AbstractGameScreen {

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    private Stage stage;
    //private Table table;
    private Label healthLabel;
    private Label manaLabel;
    private Label staminaLabel;
    private Label fullnessLabel;
    private Window window;

    public WorldScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);

        // GUI
        stage = new Stage(new FitViewport(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT));
        healthLabel = new Label("", Assets.instance.skin);
        manaLabel = new Label("", Assets.instance.skin);
        staminaLabel = new Label("", Assets.instance.skin);
        fullnessLabel = new Label("", Assets.instance.skin);

        healthLabel.addAction(Actions.forever(new Action(){
            @Override
            public boolean act(float delta) {
                healthLabel.setText(String.format("Health: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.HEALTH),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_HEALTH)));
                return true;
            }
        }));

        manaLabel.addAction(Actions.forever(new Action(){
            @Override
            public boolean act(float delta) {
                manaLabel.setText(String.format("Mana: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.MANA),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_MANA)));
                return true;
            }
        }));

        staminaLabel.addAction(Actions.forever(new Action(){
            @Override
            public boolean act(float delta) {
                staminaLabel.setText(String.format("Stamina: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.STAMINA),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_STAMINA)));
                return true;
            }
        }));

        fullnessLabel.addAction(Actions.forever(new Action(){
            @Override
            public boolean act(float delta) {
                fullnessLabel.setText(String.format("Fullness: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.FULLNESS),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_FULLNESS)));
                return true;
            }
        }));

        window = new Window("Character Status", Assets.instance.skin);
        window.padLeft(10f);
        window.padRight(15f);
        window.align(Align.left);
        window.row();
        window.add(healthLabel).align(Align.left);
        window.row();
        window.add(manaLabel).align(Align.left);
        window.row();
        window.add(staminaLabel).align(Align.left);
        window.row();
        window.add(fullnessLabel).align(Align.left);
        window.pack();
        window.setWidth(180f);
        window.setPosition(256f, 512f);


        stage.addActor(window);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldController.update(deltaTime);
        worldRenderer.render();

        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void hide() {
        stage.dispose();
        worldRenderer.dispose();
    }

}
