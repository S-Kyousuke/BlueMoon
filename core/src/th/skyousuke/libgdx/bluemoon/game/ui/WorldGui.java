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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.EnumMap;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.game.WorldController;
import th.skyousuke.libgdx.bluemoon.game.WorldListener;
import th.skyousuke.libgdx.bluemoon.game.WorldTime;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;
import th.skyousuke.libgdx.bluemoon.game.object.character.AttributeAndStatusListener;
import th.skyousuke.libgdx.bluemoon.game.object.character.DerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.EffectListener;
import th.skyousuke.libgdx.bluemoon.game.object.character.PrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.StatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Game World GUI Class
 * Created by Skyousuke <surasek@gmail.com> on 30/6/2559.
 */
public class WorldGui extends InputAdapter implements Disposable,
        WorldListener, AttributeAndStatusListener, EffectListener {

    private WorldController worldController;
    private Stage stage;

    private Window statusWindow;
    private EnumMap<StatusType, Label> statusLabels;
    private Array<AbstractCharacterEffect> effectArray;
    private Table effectTable;
    private ScrollPane effectPane;

    private Window attributeWindow;
    private EnumMap<PrimaryAttribute, Label> primaryAttributeNumberLabels;
    private EnumMap<DerivedAttribute, Label> derivedAttributeNumberLabels;
    private ScrollPane derivedAttributePane;

    private Window inventoryWindow;

    private Label timeLabel;

    public WorldGui(WorldController worldController) {
        this.worldController = worldController;
        stage = new Stage(new FitViewport(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT));

        initStatusWindow();
        initAttributeWindow();
        initTimeWindow();
        initInventoryWindow();
        initMenu();
        initGuiContent(worldController.controlledPlayer);

        Gdx.input.setInputProcessor(stage);
    }

    private void initInventoryWindow() {
        inventoryWindow = new Window("", Assets.instance.skin);
        inventoryWindow.setPosition(0, 300);
        stage.addActor(inventoryWindow);
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

    private void initTimeWindow() {
        timeLabel = new Label("Day 1\n%24:%00d", Assets.instance.skin);
        timeLabel.setAlignment(Align.center);
        Window timeWindow = new Window("Time", Assets.instance.skin);
        timeWindow.row().align(Align.left);
        timeWindow.add(timeLabel).expand().fill();
        timeWindow.pack();
        timeWindow.setWidth(80f);
        timeWindow.setMovable(false);
        timeWindow.setPosition(
                BlueMoon.SCENE_WIDTH - timeWindow.getWidth(),
                BlueMoon.SCENE_HEIGHT - timeWindow.getHeight());
        stage.addActor(timeWindow);
    }

    private void initAttributeWindow() {
        primaryAttributeNumberLabels = new EnumMap<>(PrimaryAttribute.class);
        EnumMap<PrimaryAttribute, Label> primaryAttributeLabels
                = new EnumMap<>(PrimaryAttribute.class);
        EnumMap<PrimaryAttribute, TextButton> addPrimaryAttributeButtons
                = new EnumMap<>(PrimaryAttribute.class);
        EnumMap<PrimaryAttribute, TextButton> subtractPrimaryAttributeButtons
                = new EnumMap<>(PrimaryAttribute.class);
        for (PrimaryAttribute primaryAttribute : PrimaryAttribute.values()) {
            String attributeName = primaryAttribute.name().substring(0, 1)
                    + primaryAttribute.name().toLowerCase().substring(1);
            primaryAttributeLabels.put(primaryAttribute, new Label(attributeName, Assets.instance.skin));
            primaryAttributeNumberLabels.put(primaryAttribute, new Label("", Assets.instance.skin));
            addPrimaryAttributeButtons.put(primaryAttribute, new TextButton("+", Assets.instance.skin));
            addPrimaryAttributeButtons.get(primaryAttribute).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    worldController.controlledPlayer.getAttribute().addBasePrimary(primaryAttribute, 1);
                }
            });
            subtractPrimaryAttributeButtons.put(primaryAttribute, new TextButton("-", Assets.instance.skin));
            subtractPrimaryAttributeButtons.get(primaryAttribute).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    worldController.controlledPlayer.getAttribute().addBasePrimary(primaryAttribute, -1);
                }
            });
        }
        Label derivedAttributeTitleLabel = new Label("Derived Attribute:", Assets.instance.skin);
        EnumMap<DerivedAttribute, Label> derivedAttributeLabels
                = new EnumMap<>(DerivedAttribute.class);
        derivedAttributeNumberLabels = new EnumMap<>(DerivedAttribute.class);
        Table derivedAttributeTable = new Table();

        for (DerivedAttribute derivedAttribute : DerivedAttribute.values()) {
            String[] name = derivedAttribute.name().split("_");
            String formattedName = "";
            for (String s : name) {
                formattedName += s.substring(0, 1) + s.substring(1).toLowerCase() + ' ';
            }
            derivedAttributeLabels.put(derivedAttribute, new Label(formattedName, Assets.instance.skin));
            derivedAttributeNumberLabels.put(derivedAttribute, new Label("", Assets.instance.skin));

            derivedAttributeTable.row();
            derivedAttributeTable.add(derivedAttributeLabels.get(derivedAttribute)).fillX().expandX();
            derivedAttributeTable.add(derivedAttributeNumberLabels.get(derivedAttribute))
                    .align(Align.right).padRight(10f);
        }
        derivedAttributePane = new ScrollPane(derivedAttributeTable, Assets.instance.skin);
        derivedAttributePane.setFadeScrollBars(false);
        derivedAttributePane.setForceScroll(false, true);
        derivedAttributePane.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                stage.setScrollFocus(derivedAttributePane);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                stage.setScrollFocus(null);
            }
        });
        attributeWindow = new Window("", Assets.instance.skin);
        attributeWindow.align(Align.topLeft);
        attributeWindow.padLeft(10f);
        attributeWindow.padRight(10);
        attributeWindow.padBottom(10f);
        attributeWindow.row().padTop(10f);
        for (PrimaryAttribute primaryAttribute : PrimaryAttribute.values()) {
            attributeWindow.add(primaryAttributeLabels.get(primaryAttribute))
                    .align(Align.left).fillX().expandX();
            attributeWindow.add(primaryAttributeNumberLabels.get(primaryAttribute))
                    .align(Align.right);
            attributeWindow.add(addPrimaryAttributeButtons.get(primaryAttribute))
                    .width(20f).height(20f).padRight(5f).padLeft(10f);
            attributeWindow.add(subtractPrimaryAttributeButtons.get(primaryAttribute))
                    .width(20f).height(20f);
            attributeWindow.row();
        }
        attributeWindow.add(derivedAttributeTitleLabel).align(Align.left).padTop(5f).colspan(4);
        attributeWindow.row().colspan(4);
        attributeWindow.add(derivedAttributePane).fill().expand();
        attributeWindow.pack();
        attributeWindow.setWidth(370f);
        attributeWindow.setHeight(500f);
        attributeWindow.setPosition(BlueMoon.SCENE_WIDTH - 370f, 0);
        stage.addActor(attributeWindow);
    }

    private void initStatusWindow() {
        statusLabels = new EnumMap<>(StatusType.class);
        EnumMap<StatusType, TextButton> addStatusButtons = new EnumMap<>(StatusType.class);
        EnumMap<StatusType, TextButton> subtractStatusButtons = new EnumMap<>(StatusType.class);

        for (StatusType statusType : StatusType.values()) {
            statusLabels.put(statusType, new Label("", Assets.instance.skin));
            addStatusButtons.put(statusType, new TextButton("+", Assets.instance.skin));
            addStatusButtons.get(statusType).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    worldController.controlledPlayer.getStatus().addValue(statusType, 5);
                }
            });
            subtractStatusButtons.put(statusType, new TextButton("-", Assets.instance.skin));
            subtractStatusButtons.get(statusType).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    worldController.controlledPlayer.getStatus().addValue(statusType, -5);
                }
            });
        }
        Label effectLabel = new Label("Effects:", Assets.instance.skin);
        effectTable = new Table();
        effectTable.align(Align.top);
        effectArray = new Array<>();
        effectPane = new ScrollPane(effectTable, Assets.instance.skin);
        effectPane.setFadeScrollBars(false);
        effectPane.setForceScroll(false, true);
        effectPane.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                stage.setScrollFocus(effectPane);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                stage.setScrollFocus(null);
            }
        });
        statusWindow = new Window("", Assets.instance.skin);
        statusWindow.padLeft(10f);
        statusWindow.padRight(10f);
        statusWindow.padBottom(10f);
        statusWindow.align(Align.topLeft);
        statusWindow.row().padTop(10f);
        for (StatusType statusType : StatusType.values()) {
            statusWindow.add(statusLabels.get(statusType)).align(Align.left).fillX().expandX();
            statusWindow.add(addStatusButtons.get(statusType)).width(20f).height(20f).padRight(5f);
            statusWindow.add(subtractStatusButtons.get(statusType)).width(20f).height(20f);
            statusWindow.row();
        }
        statusWindow.add(effectLabel).align(Align.left).padTop(5f).colspan(3);
        statusWindow.row();
        statusWindow.add(effectPane).fill().expand().colspan(3);
        statusWindow.row();
        statusWindow.pack();
        statusWindow.setWidth(240f);
        statusWindow.setHeight(280f);
        statusWindow.setPosition(0, 0);
        stage.addActor(statusWindow);
    }

    private void updateWindowTitle(String playerName) {
        statusWindow.getTitleLabel().setText(String.format("%s Status", playerName));
        attributeWindow.getTitleLabel().setText(String.format("%s Attribute", playerName));
        inventoryWindow.getTitleLabel().setText(String.format("%s Inventory", playerName));
    }

    private void updateStatusLabel() {
        for (StatusType statusType : StatusType.values()) {
            updateStatusLabel(statusType);
        }
    }

    private void updateStatusLabel(StatusType statusType) {
        final AbstractCharacter character = worldController.controlledPlayer;
        final String statusName = statusType.name().substring(0, 1) + statusType.name().substring(1).toLowerCase();
        float maxStatusValue = 0f;
        switch (statusType) {
            case HEALTH:
                maxStatusValue = character.getAttribute().getDerived(DerivedAttribute.MAX_HEALTH);
                break;
            case MANA:
                maxStatusValue = character.getAttribute().getDerived(DerivedAttribute.MAX_MANA);
                break;
            case STAMINA:
                maxStatusValue = character.getAttribute().getDerived(DerivedAttribute.MAX_STAMINA);
                break;
            case FULLNESS:
                maxStatusValue = character.getAttribute().getDerived(DerivedAttribute.MAX_FULLNESS);
                break;
        }
        statusLabels.get(statusType).setText(String.format("%s: %.1f/%.1f",
                statusName, character.getStatus().getValue(statusType), maxStatusValue));
    }

    private void updatePrimaryAttributeNumberLabel() {
        for (PrimaryAttribute primaryAttribute : PrimaryAttribute.values()) {
            updatePrimaryAttributeNumberLabel(primaryAttribute);
        }
    }

    private void updatePrimaryAttributeNumberLabel(PrimaryAttribute primaryAttribute) {
        primaryAttributeNumberLabels.get(primaryAttribute).setText(String.format("%d (%+d)",
                worldController.controlledPlayer.getAttribute().getBasePrimary(primaryAttribute),
                worldController.controlledPlayer.getAttribute().getAdditionalPrimary(primaryAttribute)));
    }

    private void updateDerivedAttributeNumberLabel() {
        for (DerivedAttribute derivedAttribute : DerivedAttribute.values()) {
            updateDerivedAttributeNumberLabel(derivedAttribute);
        }
    }

    private void updateDerivedAttributeNumberLabel(DerivedAttribute derivedAttribute) {
        derivedAttributeNumberLabels.get(derivedAttribute).setText(String.format("%.2f (%+.2f)",
                worldController.controlledPlayer.getAttribute().getBaseDerived(derivedAttribute),
                worldController.controlledPlayer.getAttribute().getAdditionalDerived(derivedAttribute)));
    }

    private void updateEffectList() {
        effectTable.clear();
        effectArray.clear();
        effectArray.addAll(worldController.controlledPlayer.getEffect().getAll());
        for (AbstractCharacterEffect effect : effectArray) {
            effectTable.row().expandX().align(Align.left);
            effectTable.add(new Label(effect.getName(), Assets.instance.skin));
        }
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
        updateEffectList();
    }

    @Override
    public void onEffectRemove(AbstractCharacterEffect effect) {
        updateEffectList();
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

        updateWindowTitle(player.getName());
        updateStatusLabel();
        updateEffectList();
        updatePrimaryAttributeNumberLabel();
        updateDerivedAttributeNumberLabel();
    }

    @Override
    public void onTimeChange(WorldTime time) {
        timeLabel.setText(String.format("Day %d\n%02d:%02d", time.getDay(), time.getHours(), time.getMintues()));
    }

    @Override
    public void onPrimaryAttributeChange(PrimaryAttribute primaryAttribute, int oldValue, int newValue) {
        updatePrimaryAttributeNumberLabel(primaryAttribute);
    }

    @Override
    public void onDerivedAttributeChange(DerivedAttribute derivedAttribute, float oldValue, float newValue) {
        updateDerivedAttributeNumberLabel(derivedAttribute);
    }

    @Override
    public void onStatusChange(StatusType statusType, float oldValue, float newValue) {
        updateStatusLabel(statusType);
    }

    @Override
    public void onMaxStatusChange(StatusType statusType, float oldValue, float newValue) {
        updateStatusLabel(statusType);
    }
}
