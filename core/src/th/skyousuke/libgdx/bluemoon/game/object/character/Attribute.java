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

public class Attribute {

    private final EnumMap<PrimaryAttribute, Integer> basePrimaryAttribute;
    private final EnumMap<DerivedAttribute, Float> baseDerivedAttribute;

    private final EnumMap<PrimaryAttribute, Integer> additionalPrimaryAttribute;
    private final EnumMap<DerivedAttribute, Float> additionalDerivedAttribute;

    private final Array<AttributeAndStatusListener> listeners;

    public Attribute() {
        basePrimaryAttribute = new EnumMap<>(PrimaryAttribute.class);
        baseDerivedAttribute = new EnumMap<>(DerivedAttribute.class);

        additionalPrimaryAttribute = new EnumMap<>(PrimaryAttribute.class);
        additionalDerivedAttribute = new EnumMap<>(DerivedAttribute.class);

        listeners = new Array<>();

        for (DerivedAttribute derivedAttribute : DerivedAttribute.values()) {
            baseDerivedAttribute.put(derivedAttribute, 0f);
            additionalDerivedAttribute.put(derivedAttribute, 0f);
        }
        for (PrimaryAttribute primaryAttribute : PrimaryAttribute.values()) {
            basePrimaryAttribute.put(primaryAttribute, 1);
            additionalPrimaryAttribute.put(primaryAttribute, 0);
        }
        calculateBaseDerived();
    }

    private void calculateBaseDerived() {
        int strength = getPrimary(PrimaryAttribute.STRENGTH);
        int agility = getPrimary(PrimaryAttribute.AGILITY);
        int intelligence = getPrimary(PrimaryAttribute.INTELLIGENCE);
        int vitality = getPrimary(PrimaryAttribute.VITALITY);
        int charisma = getPrimary(PrimaryAttribute.CHARISMA);
        int luck = getPrimary(PrimaryAttribute.LUCK);
        int survival = getPrimary(PrimaryAttribute.SURVIVAL);

        setBaseDerivedAttribute(DerivedAttribute.MOVING_SPEED,
                120 + (363.6744f * (agility - 1)) / (100f + agility - 1));

        setBaseDerivedAttribute(DerivedAttribute.MAX_STAMINA,
                58 + (vitality * 2.0f));

        setBaseDerivedAttribute(DerivedAttribute.MAX_FULLNESS,
                50f);

        setBaseDerivedAttribute(DerivedAttribute.MAX_HEALTH,
                271 + (vitality * 29.0f));

        setBaseDerivedAttribute(DerivedAttribute.HEALTH_REGENERATION,
                getBaseDerived(DerivedAttribute.MAX_HEALTH) * 0.01f);

        setBaseDerivedAttribute(DerivedAttribute.MAX_MANA,
                90 + (intelligence * 10.0f));

        setBaseDerivedAttribute(DerivedAttribute.MANA_REGENERATION,
                getBaseDerived(DerivedAttribute.MAX_MANA) * 0.01f);

        setBaseDerivedAttribute(DerivedAttribute.PHYSICAL_DAMAGE,
                (strength * 1.0f));

        setBaseDerivedAttribute(DerivedAttribute.MAGICAL_DAMAGE,
                (intelligence * 1.0f));

        setBaseDerivedAttribute(DerivedAttribute.PHYSICAL_DEFENSE,
                (vitality * 1.0f));

        setBaseDerivedAttribute(DerivedAttribute.MAGICAL_DEFENSE,
                (intelligence * 1.0f));

        setBaseDerivedAttribute(DerivedAttribute.ATTACK_SPEED,
                1 + (4.0408f * (agility - 1)) / (100f + agility - 1));

        setBaseDerivedAttribute(DerivedAttribute.FULLNESS_DRAIN,
                1 - (1.5154f * (survival - 1)) / (100f + survival - 1));

        setBaseDerivedAttribute(DerivedAttribute.CRAFTING,
                1.0f + survival);

        setBaseDerivedAttribute(DerivedAttribute.FISHING,
                1.0f + survival);

        setBaseDerivedAttribute(DerivedAttribute.TOOLS_EFFICIENCY,
                1 + (8.081f * (strength - 1)) / (100f + strength - 1));

        setBaseDerivedAttribute(DerivedAttribute.TOOLS_SPEED,
                1 + (8.081f * (agility - 1)) / (100f + agility - 1));

        setBaseDerivedAttribute(DerivedAttribute.TOOLS_LEVEL,
                1.0f + intelligence);

        setBaseDerivedAttribute(DerivedAttribute.ITEM_CHANCE,
                1 + (8.081f * (luck - 1)) / (100f + luck - 1));

        setBaseDerivedAttribute(DerivedAttribute.UPGRADE_CHANCE,
                1 + (6.061225f * (luck - 1)) / (100f + luck - 1));

        setBaseDerivedAttribute(DerivedAttribute.EVENT_CHANCE,
                1 + (4.0408f * (luck - 1)) / (100f + luck - 1));

        setBaseDerivedAttribute(DerivedAttribute.FRIENDSHIP,
                1 + (8.081f * (charisma - 1)) / (100f + charisma - 1));

        setBaseDerivedAttribute(DerivedAttribute.SHOPPING_PRICE,
                1 - (2.02449f * (charisma - 1)) / (150f + charisma - 1));
    }

    public void setBaseDerivedAttribute(DerivedAttribute derivedAttribute, float value) {
        float oldValue = getDerived(derivedAttribute);
        baseDerivedAttribute.put(derivedAttribute, value);
        informDerivedChangeToListener(derivedAttribute, oldValue);
    }

    public void setBasePrimary(PrimaryAttribute primaryAttribute, int value) {
        int oldValue = getPrimary(primaryAttribute);
        basePrimaryAttribute.put(primaryAttribute, MathUtils.clamp(value, 1, 99));
        calculateBaseDerived();
        informPrimaryChangeToListener(primaryAttribute, oldValue);
    }

    public void addBasePrimary(PrimaryAttribute primaryAttribute, int changeValue) {
        setBasePrimary(primaryAttribute, getBasePrimary(primaryAttribute) + changeValue);
    }

    public void addAdditionalPrimary(PrimaryAttribute primaryAttribute, int changeValue) {
        int oldValue = additionalPrimaryAttribute.get(primaryAttribute);
        additionalPrimaryAttribute.put(primaryAttribute, oldValue + changeValue);
        calculateBaseDerived();
        informPrimaryChangeToListener(primaryAttribute, oldValue);
    }

    public void addAdditionalDerived(DerivedAttribute derivedAttribute, float changeValue) {
        float oldValue = additionalDerivedAttribute.get(derivedAttribute);
        additionalDerivedAttribute.put(derivedAttribute, oldValue + changeValue);
        informDerivedChangeToListener(derivedAttribute, oldValue);
    }

    public int getBasePrimary(PrimaryAttribute primaryAttribute) {
        return basePrimaryAttribute.get(primaryAttribute);
    }

    public int getAdditionalPrimary(PrimaryAttribute primaryAttribute) {
        return additionalPrimaryAttribute.get(primaryAttribute);
    }

    public int getPrimary(PrimaryAttribute primaryAttribute) {
        return getBasePrimary(primaryAttribute) + getAdditionalPrimary(primaryAttribute);
    }

    public float getBaseDerived(DerivedAttribute derivedAttribute) {
        return baseDerivedAttribute.get(derivedAttribute);
    }

    public float getAdditionalDerived(DerivedAttribute derivedAttribute) {
        return additionalDerivedAttribute.get(derivedAttribute);
    }

    public float getDerived(DerivedAttribute derivedAttribute) {
        return getBaseDerived(derivedAttribute) + getAdditionalDerived(derivedAttribute);
    }

    public void addListener(AttributeAndStatusListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AttributeAndStatusListener listener) {
        listeners.removeValue(listener, true);
    }

    private void informPrimaryChangeToListener(PrimaryAttribute primaryAttribute, int oldValue) {
        for (AttributeAndStatusListener listener : listeners) {
            listener.onPrimaryAttributeChange(primaryAttribute, oldValue, getPrimary(primaryAttribute));
        }
    }

    private void informDerivedChangeToListener(DerivedAttribute derivedAttribute, float oldValue) {
        for (AttributeAndStatusListener listener : listeners) {
            listener.onDerivedAttributeChange(derivedAttribute, oldValue, getDerived(derivedAttribute));
            switch (derivedAttribute) {
                case MAX_STAMINA:
                    listener.onMaxStatusChange(StatusType.STAMINA, oldValue, getDerived(derivedAttribute));
                    break;
                case MAX_HEALTH:
                    listener.onMaxStatusChange(StatusType.HEALTH, oldValue, getDerived(derivedAttribute));
                    break;
                case MAX_MANA:
                    listener.onMaxStatusChange(StatusType.MANA, oldValue, getDerived(derivedAttribute));
                    break;
                case MAX_FULLNESS:
                    listener.onMaxStatusChange(StatusType.FULLNESS, oldValue, getDerived(derivedAttribute));
                    break;
            }
        }
    }

}
