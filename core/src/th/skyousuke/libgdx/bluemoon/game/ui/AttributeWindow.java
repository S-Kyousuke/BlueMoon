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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.EnumMap;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterPrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.ui.LabelPool.PooledLabel;

/**
 * Character Attribute window class.
 * Created by Skyousuke <surasek@gmail.com> on 12/7/2559.
 */
public class AttributeWindow extends Window {

    private CharacterAttribute characterAttribute;

    private EnumMap<CharacterPrimaryAttribute, PooledLabel> primaryAttributeNumberLabels;
    private EnumMap<CharacterDerivedAttribute, PooledLabel> derivedAttributeNumberLabels;

    public AttributeWindow(Skin skin) {
        super(skin);
        init();
    }

    public void init() {
        primaryAttributeNumberLabels = new EnumMap<>(CharacterPrimaryAttribute.class);
        EnumMap<CharacterPrimaryAttribute, PooledLabel> primaryAttributeLabels
                = new EnumMap<>(CharacterPrimaryAttribute.class);
        EnumMap<CharacterPrimaryAttribute, TextButton> addPrimaryAttributeButtons
                = new EnumMap<>(CharacterPrimaryAttribute.class);
        EnumMap<CharacterPrimaryAttribute, TextButton> subtractPrimaryAttributeButtons
                = new EnumMap<>(CharacterPrimaryAttribute.class);

        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            String attributeName = primaryAttribute.name().substring(0, 1)
                    + primaryAttribute.name().toLowerCase().substring(1);
            primaryAttributeLabels.put(primaryAttribute, LabelPool.obtainLabel(attributeName));
            primaryAttributeNumberLabels.put(primaryAttribute, LabelPool.obtainLabel());
            addPrimaryAttributeButtons.put(primaryAttribute, new TextButton("+", Assets.instance.skin));
            addPrimaryAttributeButtons.get(primaryAttribute).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    characterAttribute.addBasePrimary(primaryAttribute, 1);
                }
            });
            subtractPrimaryAttributeButtons.put(primaryAttribute, new TextButton("-", Assets.instance.skin));
            subtractPrimaryAttributeButtons.get(primaryAttribute).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    characterAttribute.addBasePrimary(primaryAttribute, -1);
                }
            });
        }

        PooledLabel derivedAttributeTitleLabel = LabelPool.obtainLabel("Derived Attribute:");
        EnumMap<CharacterDerivedAttribute, PooledLabel> derivedAttributeLabels
                = new EnumMap<>(CharacterDerivedAttribute.class);
        derivedAttributeNumberLabels = new EnumMap<>(CharacterDerivedAttribute.class);
        Table derivedAttributeTable = new Table();

        for (CharacterDerivedAttribute derivedAttribute : CharacterDerivedAttribute.values()) {
            String[] name = derivedAttribute.name().split("_");
            String formattedName = "";

            for (String s : name) {
                formattedName += s.substring(0, 1) + s.substring(1).toLowerCase() + ' ';
            }

            derivedAttributeLabels.put(derivedAttribute, LabelPool.obtainLabel(formattedName));
            derivedAttributeNumberLabels.put(derivedAttribute, LabelPool.obtainLabel());

            derivedAttributeTable.row();
            derivedAttributeTable.add(derivedAttributeLabels.get(derivedAttribute)).fillX().expandX();
            derivedAttributeTable.add(derivedAttributeNumberLabels.get(derivedAttribute))
                    .align(Align.right).padRight(10f);
        }

        ScrollPane derivedAttributePane = new ScrollPane(derivedAttributeTable, Assets.instance.skin);
        derivedAttributePane.setFadeScrollBars(false);
        derivedAttributePane.setForceScroll(false, true);
        derivedAttributePane.addListener(new FocusScrollListener(derivedAttributePane));

        align(Align.topLeft);
        padLeft(10f);
        padRight(10);
        padBottom(10f);
        row().padTop(10f);

        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            add(primaryAttributeLabels.get(primaryAttribute))
                    .align(Align.left).fillX().expandX();
            add(primaryAttributeNumberLabels.get(primaryAttribute))
                    .align(Align.right);
            add(addPrimaryAttributeButtons.get(primaryAttribute))
                    .width(20f).height(20f).padRight(5f).padLeft(10f);
            add(subtractPrimaryAttributeButtons.get(primaryAttribute))
                    .width(20f).height(20f);
            row();
        }

        add(derivedAttributeTitleLabel).align(Align.left).padTop(5f).colspan(4);
        row().colspan(4);
        add(derivedAttributePane).fill().expand();
        pack();
        setWidth(370f);
        setHeight(500f);
    }

    public void setCharacter(AbstractCharacter character) {
        characterAttribute = character.getAttribute();

        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            updatePrimaryAttribute(primaryAttribute);
        }
        for (CharacterDerivedAttribute derivedAttribute : CharacterDerivedAttribute.values()) {
            updateDerivedAttribute(derivedAttribute);
        }
    }

    public void updatePrimaryAttribute(CharacterPrimaryAttribute primaryAttribute) {
        primaryAttributeNumberLabels.get(primaryAttribute).setText(String.format("%d (%+d)",
                characterAttribute.getBasePrimary(primaryAttribute),
                characterAttribute.getAdditionalPrimary(primaryAttribute)));
    }

    public void updateDerivedAttribute(CharacterDerivedAttribute derivedAttribute) {
        derivedAttributeNumberLabels.get(derivedAttribute).setText(String.format("%.2f (%+.2f)",
                characterAttribute.getBaseDerived(derivedAttribute),
                characterAttribute.getAdditionalDerived(derivedAttribute)));
    }
}
