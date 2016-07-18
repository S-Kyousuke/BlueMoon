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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.Format;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
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
    private EnumMap<CharacterStatusType, ImageButton> addStatusButtons;
    private EnumMap<CharacterStatusType, ImageButton> subtractStatusButtons;

    private Label effectPaneLabel;
    private Table effectTable;
    private ScrollPane effectPane;

    public StatusWindow(Skin skin) {
        super(skin);
        statusLabels = new EnumMap<>(CharacterStatusType.class);
        addStatusButtons = new EnumMap<>(CharacterStatusType.class);
        subtractStatusButtons = new EnumMap<>(CharacterStatusType.class);

        for (CharacterStatusType statusType : CharacterStatusType.values()) {
            statusLabels.put(statusType, LabelPool.obtainLabel());
            addStatusButtons.put(statusType, new ImageButton(Assets.instance.customSkin, "plus"));
            addStatusButtons.get(statusType).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    character.getStatus().addValue(statusType, 5);
                }
            });
            subtractStatusButtons.put(statusType, new ImageButton(Assets.instance.customSkin, "minus"));
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
        effectPaneLabel = LabelPool.obtainLabel();
        effectPane.setFadeScrollBars(false);
        effectPane.setForceScroll(false, true);
        effectPane.addListener(new FocusScrollListener(effectPane));

        padLeft(10f);
        padRight(10f);
        padBottom(10f);
        align(Align.topLeft);
        row().padTop(5f);
        for (CharacterStatusType statusType : CharacterStatusType.values()) {
            add(statusLabels.get(statusType)).align(Align.left).fillX().expandX();
            add(addStatusButtons.get(statusType)).width(16f).height(16f).padRight(8f);
            add(subtractStatusButtons.get(statusType)).width(16f).height(16f);
            row();
        }
        add(effectPaneLabel).align(Align.left).colspan(3);
        row();
        add(effectPane).fill().expand().colspan(3);
        row();
        pack();
        setWidth(240f);
        setHeight(260f);
    }

    public void initContent() {
        effectPaneLabel.setText(LanguageManager.instance.getText("effect") + ':');
    }

    public void setCharacter(AbstractCharacter character) {
        this.character = character;
        for (CharacterStatusType statusType : CharacterStatusType.values()) {
            updateStatus(statusType);
        }
        updateEffect();
        setTitle(LanguageManager.instance.getFormattedText("statusWindowTitle", character.getName()));
    }

    public void updateStatus(CharacterStatusType statusType) {
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
        Format.decimalFormat.applyPattern("0.0");
        statusLabels.get(statusType)
                .setText(LanguageManager.instance.getText(statusType.name().toLowerCase()) + ": "
                        + Format.decimalFormat.format(character.getStatus().getValue(statusType)) + '/'
                        + Format.decimalFormat.format(maxStatusValue));
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
