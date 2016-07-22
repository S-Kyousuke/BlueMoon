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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.Language;
import th.skyousuke.libgdx.bluemoon.framework.LanguageListener;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
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
        WorldListener, AttributeAndStatusListener, CharacterEffectListener, LanguageListener {

    private Stage stage;

    private StatusWindow statusWindow;
    private AttributeWindow attributeWindow;
    private InventoryWindow inventoryWindow;
    private TimeWindow timeWindow;

    private TextButton toggleAttributeWindowButton;
    private TextButton toggleInventoryWindowButton;
    private TextButton toggleStatusWindowButton;

    private Label tutorialText;
    private Table tutorialBox;

    private AbstractPlayer player;

    public WorldGui(WorldController worldController) {
        stage = new Stage(new FitViewport(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT));
        player = worldController.controlledPlayer;

        statusWindow = new StatusWindow(Assets.instance.customSkin);
        attributeWindow = new AttributeWindow(Assets.instance.customSkin);
        inventoryWindow = new InventoryWindow(Assets.instance.customSkin);
        timeWindow = new TimeWindow(Assets.instance.customSkin);
        Table menu = createMenu();
        tutorialBox = createTutorialBox();

        attributeWindow.setPosition(BlueMoon.SCENE_WIDTH - attributeWindow.getWidth(), 0);
        inventoryWindow.setPosition(0, 280);
        timeWindow.setPosition(
                BlueMoon.SCENE_WIDTH - timeWindow.getWidth(),
                BlueMoon.SCENE_HEIGHT - timeWindow.getHeight());

        worldController.addListener(this);
        listenToPlayer();
        listenToMenu();
        LanguageManager.instance.addListener(this);

        initGuiContent();

        stage.addActor(statusWindow);
        stage.addActor(attributeWindow);
        stage.addActor(timeWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(menu);
        stage.addActor(tutorialBox);

        Gdx.input.setInputProcessor(stage);
    }

    private void listenToMenu() {
        toggleStatusWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!statusWindow.isVisible()) statusWindow.setVisible(true);
                else statusWindow.setVisible(false);
            }
        });
        toggleAttributeWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!attributeWindow.isVisible()) attributeWindow.setVisible(true);
                else attributeWindow.setVisible(false);
            }
        });
        toggleInventoryWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!inventoryWindow.isVisible()) inventoryWindow.setVisible(true);
                else inventoryWindow.setVisible(false);
            }
        });
    }

    private Table createMenu() {
        toggleStatusWindowButton = new TextButton("", Assets.instance.customSkin);
        toggleAttributeWindowButton = new TextButton("", Assets.instance.customSkin);
        toggleInventoryWindowButton = new TextButton("", Assets.instance.customSkin);

        Table menu = new Table();
        menu.row();
        menu.add(toggleStatusWindowButton).width(60).height(30);
        menu.add(toggleAttributeWindowButton).padLeft(10f).width(80).height(30);
        menu.add(toggleInventoryWindowButton).padLeft(10f).width(80).height(30);
        menu.pack();
        menu.setPosition(0, BlueMoon.SCENE_HEIGHT - menu.getHeight());
        return menu;
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
        player.getAttribute().removeListener(this);
        player.getStatus().removeListener(this);
        player.getEffect().removeListener(this);

        player = newPlayer;

        listenToPlayer();
        initPlayerContent();
    }

    private void listenToPlayer() {
        player.getAttribute().addListener(this);
        player.getStatus().addListener(this);
        player.getEffect().addListener(this);
    }

    private void initPlayerContent() {
        statusWindow.setCharacter(player);
        attributeWindow.setCharacter(player);
        inventoryWindow.setCharacter(player);
    }

    private void initMenuLabel() {
        toggleStatusWindowButton.setText(LanguageManager.instance.getText("statusLabel"));
        toggleAttributeWindowButton.setText(LanguageManager.instance.getText("attributeLabel"));
        toggleInventoryWindowButton.setText(LanguageManager.instance.getText("inventoryLabel"));
    }

    private void initTutorialText() {
        tutorialText.setText(LanguageManager.instance.getText("tutorial"));
        tutorialBox.pack();
        tutorialBox.setPosition(
                BlueMoon.SCENE_WIDTH / 2 - tutorialBox.getWidth() / 2,
                BlueMoon.SCENE_HEIGHT - tutorialBox.getHeight());
    }

    private void initGuiContent() {
        statusWindow.initContent();
        attributeWindow.initContent();
        inventoryWindow.initContent();
        timeWindow.initContent();

        initMenuLabel();
        initPlayerContent();
        initTutorialText();
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

    @Override
    public void onLanguageChange(Language currentLanguage) {
        initGuiContent();
    }

    /* Control tutorial text box */
    private Table createTutorialBox() {
        tutorialText = LabelPool.obtainLabel();

        Table table = new Table();
        table.setBackground(Assets.instance.customSkin.getDrawable("dimGrayDraw"));
        table.add(tutorialText).padLeft(10f).padRight(10f);
        return table;
    }

}
