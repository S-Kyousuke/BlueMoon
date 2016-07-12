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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Character status window class
 * Created by Skyousuke <surasek@gmail.com> on 12/7/2559.
 */
public class StatusWindow extends Window {

    private AbstractCharacter character;

    private EnumMap<CharacterStatusType, Label> statusLabels;
    private EnumMap<CharacterStatusType, TextButton> addStatusButtons;
    private EnumMap<CharacterStatusType, TextButton> subtractStatusButtons;

    private Label effectPaneLabel;
    private Table effectTable;
    private ScrollPane effectPane;

    public StatusWindow(Skin skin) {
        super(skin);
        statusLabels = new EnumMap<>(CharacterStatusType.class);
        addStatusButtons = new EnumMap<>(CharacterStatusType.class);
        subtractStatusButtons = new EnumMap<>(CharacterStatusType.class);

        for (CharacterStatusType statusType : CharacterStatusType.values()) {
            statusLabels.put(statusType, new Label("", Assets.instance.skin));
            addStatusButtons.put(statusType, new TextButton("+", Assets.instance.skin));
            addStatusButtons.get(statusType).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    character.getStatus().addValue(statusType, 5);
                }
            });
            subtractStatusButtons.put(statusType, new TextButton("-", Assets.instance.skin));
            subtractStatusButtons.get(statusType).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    character.getStatus().addValue(statusType, -5);
                }
            });
        }

        effectTable = new Table();
        effectTable.align(Align.top);
        effectPane = new ScrollPane(effectTable, Assets.instance.skin);
        effectPaneLabel = new Label("Effects:", Assets.instance.skin);
        effectPane.setFadeScrollBars(false);
        effectPane.setForceScroll(false, true);
        effectPane.addListener(new FocusScrollListener(effectPane));

        padLeft(10f);
        padRight(10f);
        padBottom(10f);
        align(Align.topLeft);
        row().padTop(10f);
        for (CharacterStatusType statusType : CharacterStatusType.values()) {
            add(statusLabels.get(statusType)).align(Align.left).fillX().expandX();
            add(addStatusButtons.get(statusType)).width(20f).height(20f).padRight(5f);
            add(subtractStatusButtons.get(statusType)).width(20f).height(20f);
            row();
        }
        add(effectPaneLabel).align(Align.left).padTop(5f).colspan(3);
        row();
        add(effectPane).fill().expand().colspan(3);
        row();
        pack();
        setWidth(240f);
        setHeight(280f);
    }

    public void setCharacter(AbstractCharacter character) {
        this.character = character;
        for (CharacterStatusType statusType : CharacterStatusType.values()) {
            updateStatus(statusType);
        }
        updateEffect();
    }

    public void updateStatus(CharacterStatusType statusType) {
        final String statusName = statusType.name().substring(0, 1) + statusType.name().substring(1).toLowerCase();
        float maxStatusValue = 0f;
        switch (statusType) {
            case HEALTH:
                maxStatusValue = character.getAttribute().getDerived(CharacterDerivedAttribute.MAX_HEALTH);
                break;
            case MANA:
                maxStatusValue = character.getAttribute().getDerived(CharacterDerivedAttribute.MAX_MANA);
                break;
            case STAMINA:
                maxStatusValue = character.getAttribute().getDerived(CharacterDerivedAttribute.MAX_STAMINA);
                break;
            case FULLNESS:
                maxStatusValue = character.getAttribute().getDerived(CharacterDerivedAttribute.MAX_FULLNESS);
                break;
        }
        statusLabels.get(statusType).setText(String.format("%s: %.1f/%.1f",
                statusName, character.getStatus().getValue(statusType), maxStatusValue));
    }

    public void updateEffect() {
        effectTable.clearChildren();
        final Array<AbstractCharacterEffect> effects = character.getEffect().getAll();
        for (int i = 0; i < effects.size; ++i) {
            effectTable.row().expandX().align(Align.left);
            effectTable.add(LabelPool.obtainLabel(effects.get(i).getName()));
        }
    }

}
