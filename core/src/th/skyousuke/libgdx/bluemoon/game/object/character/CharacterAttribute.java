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

package th.skyousuke.libgdx.bluemoon.game.object.character;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

public class CharacterAttribute {

    private final EnumMap<CharacterPrimaryAttribute, Integer> basePrimaryAttribute;
    private final EnumMap<CharacterDerivedAttribute, Float> baseDerivedAttribute;

    private final EnumMap<CharacterPrimaryAttribute, Integer> additionalPrimaryAttribute;
    private final EnumMap<CharacterDerivedAttribute, Float> additionalDerivedAttribute;

    private final Array<AttributeAndStatusListener> listeners;

    public CharacterAttribute() {
        basePrimaryAttribute = new EnumMap<>(CharacterPrimaryAttribute.class);
        baseDerivedAttribute = new EnumMap<>(CharacterDerivedAttribute.class);

        additionalPrimaryAttribute = new EnumMap<>(CharacterPrimaryAttribute.class);
        additionalDerivedAttribute = new EnumMap<>(CharacterDerivedAttribute.class);

        listeners = new Array<>();

        for (CharacterDerivedAttribute derivedAttribute : CharacterDerivedAttribute.values()) {
            baseDerivedAttribute.put(derivedAttribute, 0f);
            additionalDerivedAttribute.put(derivedAttribute, 0f);
        }
        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            basePrimaryAttribute.put(primaryAttribute, 1);
            additionalPrimaryAttribute.put(primaryAttribute, 0);
        }
        calculateBaseDerived();
    }

    private void calculateBaseDerived() {
        int strength = getPrimary(CharacterPrimaryAttribute.STRENGTH);
        int agility = getPrimary(CharacterPrimaryAttribute.AGILITY);
        int intelligence = getPrimary(CharacterPrimaryAttribute.INTELLIGENCE);
        int vitality = getPrimary(CharacterPrimaryAttribute.VITALITY);
        int charisma = getPrimary(CharacterPrimaryAttribute.CHARISMA);
        int luck = getPrimary(CharacterPrimaryAttribute.LUCK);
        int survival = getPrimary(CharacterPrimaryAttribute.SURVIVAL);

        setBaseDerivedAttribute(CharacterDerivedAttribute.MOVING_SPEED,
                120 + (363.6744f * (agility - 1)) / (100f + agility - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_STAMINA,
                58 + (vitality * 2.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_FULLNESS,
                50f);

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_HEALTH,
                271 + (vitality * 29.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.HEALTH_REGENERATION,
                getBaseDerived(CharacterDerivedAttribute.MAX_HEALTH) * 0.01f);

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_MANA,
                90 + (intelligence * 10.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MANA_REGENERATION,
                getBaseDerived(CharacterDerivedAttribute.MAX_MANA) * 0.01f);

        setBaseDerivedAttribute(CharacterDerivedAttribute.PHYSICAL_DAMAGE,
                (strength * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAGICAL_DAMAGE,
                (intelligence * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.PHYSICAL_DEFENSE,
                (vitality * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAGICAL_DEFENSE,
                (intelligence * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.ATTACK_SPEED,
                1 + (4.0408f * (agility - 1)) / (100f + agility - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.FULLNESS_DRAIN,
                1 - (1.5154f * (survival - 1)) / (100f + survival - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.CRAFTING,
                1.0f + survival);

        setBaseDerivedAttribute(CharacterDerivedAttribute.FISHING,
                1.0f + survival);

        setBaseDerivedAttribute(CharacterDerivedAttribute.TOOLS_EFFICIENCY,
                1 + (8.081f * (strength - 1)) / (100f + strength - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.TOOLS_SPEED,
                1 + (8.081f * (agility - 1)) / (100f + agility - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.TOOLS_LEVEL,
                1.0f + intelligence);

        setBaseDerivedAttribute(CharacterDerivedAttribute.ITEM_CHANCE,
                1 + (8.081f * (luck - 1)) / (100f + luck - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.UPGRADE_CHANCE,
                1 + (6.061225f * (luck - 1)) / (100f + luck - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.EVENT_CHANCE,
                1 + (4.0408f * (luck - 1)) / (100f + luck - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.FRIENDSHIP,
                1 + (8.081f * (charisma - 1)) / (100f + charisma - 1));

        setBaseDerivedAttribute(CharacterDerivedAttribute.SHOPPING_PRICE,
                1 - (2.02449f * (charisma - 1)) / (150f + charisma - 1));
    }

    public void setBaseDerivedAttribute(CharacterDerivedAttribute derivedAttribute, float value) {
        float oldValue = getDerived(derivedAttribute);
        baseDerivedAttribute.put(derivedAttribute, value);
        informDerivedChangeToListener(derivedAttribute, oldValue);
    }

    public void setBasePrimary(CharacterPrimaryAttribute primaryAttribute, int value) {
        int oldValue = getPrimary(primaryAttribute);
        basePrimaryAttribute.put(primaryAttribute, MathUtils.clamp(value, 1, 99));
        calculateBaseDerived();
        informPrimaryChangeToListener(primaryAttribute, oldValue);
    }

    public void addBasePrimary(CharacterPrimaryAttribute primaryAttribute, int changeValue) {
        setBasePrimary(primaryAttribute, getBasePrimary(primaryAttribute) + changeValue);
    }

    public void addAdditionalPrimary(CharacterPrimaryAttribute primaryAttribute, int changeValue) {
        int oldValue = additionalPrimaryAttribute.get(primaryAttribute);
        additionalPrimaryAttribute.put(primaryAttribute, oldValue + changeValue);
        calculateBaseDerived();
        informPrimaryChangeToListener(primaryAttribute, oldValue);
    }

    public void addAdditionalDerived(CharacterDerivedAttribute derivedAttribute, float changeValue) {
        float oldValue = additionalDerivedAttribute.get(derivedAttribute);
        additionalDerivedAttribute.put(derivedAttribute, oldValue + changeValue);
        informDerivedChangeToListener(derivedAttribute, oldValue);
    }

    public int getBasePrimary(CharacterPrimaryAttribute primaryAttribute) {
        return basePrimaryAttribute.get(primaryAttribute);
    }

    public int getAdditionalPrimary(CharacterPrimaryAttribute primaryAttribute) {
        return additionalPrimaryAttribute.get(primaryAttribute);
    }

    public int getPrimary(CharacterPrimaryAttribute primaryAttribute) {
        return getBasePrimary(primaryAttribute) + getAdditionalPrimary(primaryAttribute);
    }

    public float getBaseDerived(CharacterDerivedAttribute derivedAttribute) {
        return baseDerivedAttribute.get(derivedAttribute);
    }

    public float getAdditionalDerived(CharacterDerivedAttribute derivedAttribute) {
        return additionalDerivedAttribute.get(derivedAttribute);
    }

    public float getDerived(CharacterDerivedAttribute derivedAttribute) {
        return getBaseDerived(derivedAttribute) + getAdditionalDerived(derivedAttribute);
    }

    public void addListener(AttributeAndStatusListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AttributeAndStatusListener listener) {
        listeners.removeValue(listener, true);
    }

    private void informPrimaryChangeToListener(CharacterPrimaryAttribute primaryAttribute, int oldValue) {
        for (AttributeAndStatusListener listener : listeners) {
            listener.onPrimaryAttributeChange(primaryAttribute, oldValue, getPrimary(primaryAttribute));
        }
    }

    private void informDerivedChangeToListener(CharacterDerivedAttribute derivedAttribute, float oldValue) {
        for (AttributeAndStatusListener listener : listeners) {
            listener.onDerivedAttributeChange(derivedAttribute, oldValue, getDerived(derivedAttribute));
            switch (derivedAttribute) {
                case MAX_STAMINA:
                    listener.onMaxStatusChange(CharacterStatusType.STAMINA, oldValue, getDerived(derivedAttribute));
                    break;
                case MAX_HEALTH:
                    listener.onMaxStatusChange(CharacterStatusType.HEALTH, oldValue, getDerived(derivedAttribute));
                    break;
                case MAX_MANA:
                    listener.onMaxStatusChange(CharacterStatusType.MANA, oldValue, getDerived(derivedAttribute));
                    break;
                case MAX_FULLNESS:
                    listener.onMaxStatusChange(CharacterStatusType.FULLNESS, oldValue, getDerived(derivedAttribute));
                    break;
            }
        }
    }

}
