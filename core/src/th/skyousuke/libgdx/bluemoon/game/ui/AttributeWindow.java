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
import com.google.common.base.CaseFormat;

import java.util.EnumMap;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.I18NManager;
import th.skyousuke.libgdx.bluemoon.framework.NumberFormat;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterPrimaryAttribute;

/**
 * Character Attribute window class.
 * Created by S.Kyousuke <surasek@gmail.com> on 12/7/2559.
 */
public class AttributeWindow extends Window {

    private AbstractCharacter character;

    private EnumMap<CharacterPrimaryAttribute, Label> primaryAttributeNumberLabels;
    private EnumMap<CharacterDerivedAttribute, Label> derivedAttributeNumberLabels;
    private EnumMap<CharacterPrimaryAttribute, Label> primaryAttributeLabels;
    private EnumMap<CharacterDerivedAttribute, Label> derivedAttributeLabels;
    private Label derivedAttributeTitleLabel;

    public AttributeWindow(Skin skin) {
        super(skin);
        setColor(1, 1, 1, 0.8f);
        align(Align.topLeft);
        padLeft(10f);
        padRight(10);
        padBottom(10f);
        row().padTop(5f);

        primaryAttributeNumberLabels = new EnumMap<>(CharacterPrimaryAttribute.class);
        derivedAttributeNumberLabels = new EnumMap<>(CharacterDerivedAttribute.class);
        primaryAttributeLabels = new EnumMap<>(CharacterPrimaryAttribute.class);
        derivedAttributeLabels = new EnumMap<>(CharacterDerivedAttribute.class);
        derivedAttributeTitleLabel = LabelPool.obtainLabel();

        for (final CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            Label attributeLabel = LabelPool.obtainLabel();
            Label attributeNumberLabel = LabelPool.obtainLabel();

            ImageButton plusButton = new ImageButton(Assets.instance.customSkin, "plus");
            ImageButton minusButton = new ImageButton(Assets.instance.customSkin, "minus");

            primaryAttributeLabels.put(primaryAttribute, attributeLabel);
            primaryAttributeNumberLabels.put(primaryAttribute, attributeNumberLabel);

            plusButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    character.getAttribute().addBasePrimary(primaryAttribute, 1);
                }
            });
            minusButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    character.getAttribute().addBasePrimary(primaryAttribute, -1);
                }
            });

            add(attributeLabel).align(Align.left).fillX().expandX();
            add(attributeNumberLabel).align(Align.right);
            add(plusButton).width(16f).height(16f).padRight(8f).padLeft(10f);
            add(minusButton).width(16f).height(16f);
            row();
        }

        Table derivedAttributeTable = new Table();

        for (CharacterDerivedAttribute derivedAttribute : CharacterDerivedAttribute.values()) {
            Label attributeLabel = LabelPool.obtainLabel();
            Label attributeNumberLabel = LabelPool.obtainLabel();

            derivedAttributeLabels.put(derivedAttribute, attributeLabel);
            derivedAttributeNumberLabels.put(derivedAttribute, attributeNumberLabel);

            derivedAttributeTable.row();
            derivedAttributeTable.add(attributeLabel).fillX().expandX();
            derivedAttributeTable.add(attributeNumberLabel).align(Align.right).padRight(10f);
        }

        ScrollPane derivedAttributePane =
                new ScrollPane(derivedAttributeTable, Assets.instance.customSkin, "dimBackground");
        derivedAttributePane.setFadeScrollBars(false);
        derivedAttributePane.setForceScroll(false, true);
        derivedAttributePane.addListener(new FocusScrollListener(derivedAttributePane));

        add(derivedAttributeTitleLabel).align(Align.left).padTop(5f).colspan(4);
        row().colspan(4);
        add(derivedAttributePane).fill().expand();
    }

    public void init() {
        for (CharacterDerivedAttribute derivedAttribute : CharacterDerivedAttribute.values()) {
            derivedAttributeLabels
                    .get(derivedAttribute)
                    .setText(I18NManager.instance
                            .getText(CaseFormat.UPPER_UNDERSCORE
                                    .to(CaseFormat.LOWER_CAMEL, derivedAttribute.name())));
            pack();
            setWidth(340f);
            setHeight(420f);
        }

        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            String attributeName = primaryAttribute.name().toLowerCase();
            primaryAttributeLabels.get(primaryAttribute).setText(I18NManager.instance.getText(attributeName));
        }
        derivedAttributeTitleLabel.setText(I18NManager.instance.getText("derivedAttribute") + ':');
    }

    public void setCharacter(AbstractCharacter character) {
        this.character = character;
        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            updatePrimaryAttribute(primaryAttribute);
        }
        for (CharacterDerivedAttribute derivedAttribute : CharacterDerivedAttribute.values()) {
            updateDerivedAttribute(derivedAttribute);
        }
        setTitle(I18NManager.instance.getFormattedText("attributeWindowTitle", character.getName()));
    }

    public void updatePrimaryAttribute(CharacterPrimaryAttribute primaryAttribute) {
        final CharacterAttribute attribute = character.getAttribute();
        primaryAttributeNumberLabels
                .get(primaryAttribute)
                .setText(attribute.getBasePrimary(primaryAttribute) + " (+"
                        + attribute.getAdditionalPrimary(primaryAttribute) + ')');
    }

    public void updateDerivedAttribute(CharacterDerivedAttribute derivedAttribute) {
        final CharacterAttribute attribute = character.getAttribute();
        derivedAttributeNumberLabels
                .get(derivedAttribute)
                .setText(NumberFormat.formatFloat(attribute.getBaseDerived(derivedAttribute), 1) + " (+"
                        + NumberFormat.formatFloat(attribute.getAdditionalDerived(derivedAttribute), 1) + ')');
    }

}
