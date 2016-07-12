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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.game.WorldController;
import th.skyousuke.libgdx.bluemoon.game.WorldListener;
import th.skyousuke.libgdx.bluemoon.game.WorldTime;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;
import th.skyousuke.libgdx.bluemoon.game.object.character.AttributeAndStatusListener;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterEffectListener;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterPrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Game World GUI Class
 * Created by Skyousuke <surasek@gmail.com> on 30/6/2559.
 */
public class WorldGui extends InputAdapter implements Disposable,
        WorldListener, AttributeAndStatusListener, CharacterEffectListener {

    private Stage stage;

    private StatusWindow statusWindow;
    private AttributeWindow attributeWindow;
    private InventoryWindow inventoryWindow;
    private TimeWindow timeWindow;

    public WorldGui(WorldController worldController) {
        stage = new Stage(new FitViewport(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT));

        statusWindow = new StatusWindow(Assets.instance.customSkin);
        attributeWindow = new AttributeWindow(Assets.instance.customSkin);
        inventoryWindow = new InventoryWindow(Assets.instance.customSkin);
        timeWindow = new TimeWindow(Assets.instance.customSkin);

        attributeWindow.setPosition(BlueMoon.SCENE_WIDTH - attributeWindow.getWidth(), 0);
        inventoryWindow.setPosition(0, 400);
        timeWindow.setPosition(
                BlueMoon.SCENE_WIDTH - timeWindow.getWidth(),
                BlueMoon.SCENE_HEIGHT - timeWindow.getHeight());

        stage.addActor(statusWindow);
        stage.addActor(attributeWindow);
        stage.addActor(timeWindow);
        stage.addActor(inventoryWindow);

        initMenu();
        initGuiContent(worldController.controlledPlayer);

        Gdx.input.setInputProcessor(stage);
    }

    private void initMenu() {
        TextButton toggleStatusWindowButton = new TextButton("Status", Assets.instance.skin);
        toggleStatusWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!statusWindow.isVisible()) statusWindow.setVisible(true);
                else statusWindow.setVisible(false);
            }
        });
        TextButton toggleAttributeWindowButton = new TextButton("Attribute", Assets.instance.skin);
        toggleAttributeWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!attributeWindow.isVisible()) attributeWindow.setVisible(true);
                else attributeWindow.setVisible(false);
            }
        });
        TextButton toggleInventoryWindowButton = new TextButton("Inventory", Assets.instance.skin);
        toggleInventoryWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!inventoryWindow.isVisible()) inventoryWindow.setVisible(true);
                else inventoryWindow.setVisible(false);
            }
        });

        Table menu = new Table();
        menu.row();
        menu.add(toggleStatusWindowButton).padLeft(10f).width(60).height(30);
        menu.add(toggleAttributeWindowButton).padLeft(10f).width(80).height(30);
        menu.add(toggleInventoryWindowButton).padLeft(10f).width(80).height(30);
        menu.pack();
        menu.setPosition(0, 680);
        stage.addActor(menu);
    }

    public void update(float deltaTime) {
        stage.act(deltaTime);
    }

    public void render() {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void onEffectAdd(AbstractCharacterEffect effect) {
        statusWindow.updateEffect();
    }

    @Override
    public void onEffectRemove(AbstractCharacterEffect effect) {
        statusWindow.updateEffect();
    }

    @Override
    public void onPlayerChange(AbstractPlayer oldPlayer, AbstractPlayer newPlayer) {
        oldPlayer.getAttribute().removeListener(this);
        oldPlayer.getStatus().removeListener(this);
        oldPlayer.getEffect().removeListener(this);

        initGuiContent(newPlayer);
    }

    private void initGuiContent(AbstractPlayer player) {
        player.getAttribute().addListener(this);
        player.getStatus().addListener(this);
        player.getEffect().addListener(this);

        String playerName = player.getName();

        statusWindow.setTitle(String.format("%s Status", playerName));
        attributeWindow.setTitle(String.format("%s Attribute", playerName));
        inventoryWindow.setTitle(String.format("%s Inventory", playerName));

        statusWindow.setCharacter(player);
        attributeWindow.setCharacter(player);
        inventoryWindow.setCharacter(player);
    }

    @Override
    public void onTimeChange(WorldTime time) {
        timeWindow.setTime(time);
    }

    @Override
    public void onPrimaryAttributeChange(CharacterPrimaryAttribute primaryAttribute, int oldValue, int newValue) {
        attributeWindow.updatePrimaryAttribute(primaryAttribute);
    }

    @Override
    public void onDerivedAttributeChange(CharacterDerivedAttribute derivedAttribute, float oldValue, float newValue) {
        attributeWindow.updateDerivedAttribute(derivedAttribute);
    }

    @Override
    public void onStatusChange(CharacterStatusType statusType, float oldValue, float newValue) {
        statusWindow.updateStatus(statusType);
    }

    @Override
    public void onMaxStatusChange(CharacterStatusType statusType, float oldValue, float newValue) {
        statusWindow.updateStatus(statusType);
    }
}
