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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.game.Assets;
import th.skyousuke.libgdx.bluemoon.game.WorldController;
import th.skyousuke.libgdx.bluemoon.game.WorldRenderer;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;

public class WorldScreen extends AbstractGameScreen {

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    private Stage stage;

    private Window statusWindow;
    private Label healthLabel;
    private Label manaLabel;
    private Label staminaLabel;
    private Label fullnessLabel;
    private Label effectLabel;

    private List<String> effectsList;
    private ScrollPane effectsPane;

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

        healthLabel.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                healthLabel.setText(String.format("Health: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.HEALTH),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_HEALTH)));
                return false;
            }
        });

        manaLabel.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                manaLabel.setText(String.format("Mana: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.MANA),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_MANA)));
                return false;
            }
        });

        staminaLabel.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                staminaLabel.setText(String.format("Stamina: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.STAMINA),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_STAMINA)));
                return false;
            }
        });

        fullnessLabel.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                fullnessLabel.setText(String.format("Fullness: %.1f/%.1f",
                        worldController.controlledPlayer.getStatus(CharacterStatusType.FULLNESS),
                        worldController.controlledPlayer.getAttribute()
                                .getDerived(CharacterDerivedAttribute.MAX_FULLNESS)));
                return false;
            }
        });

        effectsList = new List<>(Assets.instance.skin);
        Array<String> items = new Array<>();
        effectsList.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                items.clear();
                for (AbstractEffect effect : worldController.controlledPlayer.getEffects())
                    items.add(effect.getName());
                effectsList.setItems(items);
                return false;
            }
        });
        effectsPane = new ScrollPane(effectsList, Assets.instance.skin);
        effectsPane.setForceScroll(false, true);
        effectsPane.setFadeScrollBars(false);
        effectLabel = new Label("Effects:", Assets.instance.skin);

        statusWindow = new Window("", Assets.instance.skin);
        statusWindow.padLeft(10f);
        statusWindow.padRight(15f);
        statusWindow.padBottom(10f);
        statusWindow.align(Align.left);
        statusWindow.row();
        statusWindow.add(healthLabel).align(Align.left);
        statusWindow.row();
        statusWindow.add(manaLabel).align(Align.left);
        statusWindow.row();
        statusWindow.add(staminaLabel).align(Align.left);
        statusWindow.row();
        statusWindow.add(fullnessLabel).align(Align.left);
        statusWindow.row();
        statusWindow.add(effectLabel).align(Align.left);
        statusWindow.row();
        statusWindow.add(effectsPane).fill().expand();
        statusWindow.pack();
        statusWindow.setWidth(180f);
        statusWindow.setHeight(250f);
        statusWindow.setPosition(100, 256f);

        statusWindow.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                statusWindow.getTitleLabel().setText(String.format("%s", worldController.controlledPlayer.getName()));
                return false;
            }
        });

        stage.addActor(statusWindow);
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
