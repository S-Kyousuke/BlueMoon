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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.framework.Assets;
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
 * Created by S.Kyousuke <surasek@gmail.com> on 30/6/2559.
 */
public class WorldGui extends InputAdapter implements Disposable,
        WorldListener, AttributeAndStatusListener, CharacterEffectListener, LanguageListener {

    private Stage stage;
    private InputMultiplexer multiplexer;
    private WorldController worldController;

    private StatusWindow statusWindow;
    private AttributeWindow attributeWindow;
    private InventoryWindow inventoryWindow;
    private SettingWindow settingWindow;
    private HelpWindow helpWindow;

    private TimeHud timeHud;
    private MenuHud menuHud;
    private CharacterHud characterHud;

    private AbstractPlayer player;

    public WorldGui(WorldController worldController) {
        stage = new Stage(new FitViewport(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT));
        player = worldController.controlledPlayer;
        this.worldController = worldController;

        statusWindow = new StatusWindow(Assets.instance.customSkin);
        attributeWindow = new AttributeWindow(Assets.instance.customSkin);
        inventoryWindow = new InventoryWindow(Assets.instance.customSkin);
        timeHud = new TimeHud(Assets.instance.customSkin);
        settingWindow = new SettingWindow(Assets.instance.customSkin);
        helpWindow = new HelpWindow(Assets.instance.customSkin);

        FpsLabel fpsLabel = new FpsLabel(LabelPool.obtainLabel());

        characterHud = new CharacterHud();
        characterHud.setPosition(0, stage.getHeight() - characterHud.getHeight());

        menuHud = new MenuHud();
        menuHud.setPosition(stage.getWidth() - menuHud.getWidth(), 0);
        menuHud.setCharacterWindow(attributeWindow);
        menuHud.setInventoryWindow(inventoryWindow);
        menuHud.setSettingWindow(settingWindow);
        menuHud.setHelpWindow(helpWindow);

        initGuiContent();
        hideWindow();

        statusWindow.setY(40);
        attributeWindow.setPosition(BlueMoon.SCENE_WIDTH - attributeWindow.getWidth(), 0);
        inventoryWindow.setPosition(BlueMoon.SCENE_WIDTH / 2 - inventoryWindow.getWidth() / 2, 0);
        timeHud.setPosition(
                BlueMoon.SCENE_WIDTH - timeHud.getWidth(),
                BlueMoon.SCENE_HEIGHT - timeHud.getHeight());
        settingWindow.setPosition(0, 330);
        fpsLabel.setX(5);

        listenToPlayer();
        worldController.addListener(this);
        LanguageManager.instance.addListener(this);

        stage.addActor(statusWindow);
        stage.addActor(attributeWindow);
        stage.addActor(timeHud);
        stage.addActor(inventoryWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(settingWindow);
        stage.addActor(fpsLabel);
        stage.addActor(helpWindow);
        stage.addActor(menuHud);
        stage.addActor(characterHud);
        //stage.setDebugAll(true);

        multiplexer = (InputMultiplexer) (Gdx.input.getInputProcessor());
        multiplexer.addProcessor(stage);
    }

    private void hideWindow() {
        statusWindow.setVisible(false);
        attributeWindow.setVisible(false);
        inventoryWindow.setVisible(false);
        settingWindow.setVisible(false);
        helpWindow.setVisible(false);
    }

    public void update(float deltaTime) {
        stage.act(deltaTime);

        // TODO: For debugging only.
        if (Gdx.input.isKeyJustPressed(Keys.F7)) {
            statusWindow.setVisible(!statusWindow.isVisible());
        }
        if (Gdx.input.isKeyJustPressed(Keys.F8)) {
            attributeWindow.setVisible(!attributeWindow.isVisible());
        }

    }

    public void render() {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        multiplexer.removeProcessor(stage);
        worldController.removeListener(this);
        LanguageManager.instance.removeListener(this);
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
        characterHud.setCharacter(player);
        attributeWindow.setCharacter(player);
        inventoryWindow.setCharacter(player);
    }

    private void initGuiContent() {
        statusWindow.initContent();
        attributeWindow.initContent();
        inventoryWindow.initContent();
        timeHud.initContent();
        settingWindow.initContent();
        initHelpWindow();
        initPlayerContent();
    }

    public void initHelpWindow() {
        helpWindow.initContent();
    }

    @Override
    public void onTimeChange(WorldTime time) {
        timeHud.setTime(time);
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
        characterHud.updateStatus(statusType);
    }

    @Override
    public void onMaxStatusChange(CharacterStatusType statusType, float oldValue, float newValue) {
        statusWindow.updateStatus(statusType);
        characterHud.updateStatus(statusType);
    }

    @Override
    public void onLanguageChange(int currentLanguage) {
        initGuiContent();
    }

}
