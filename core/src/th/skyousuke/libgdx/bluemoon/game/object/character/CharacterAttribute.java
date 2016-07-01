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

import java.util.EnumMap;

public class CharacterAttribute {

    // 2 length array for base value and additional value

    private final EnumMap<CharacterPrimaryAttribute, Integer> basePrimaryAttribute;
    private final EnumMap<CharacterDerivedAttribute, Float> baseDerivedAttribute;

    private final EnumMap<CharacterPrimaryAttribute, Integer> additionalPrimaryAttribute;
    private final EnumMap<CharacterDerivedAttribute, Float> additionalDerivedAttribute;

    private CharacterListener characterListener;

    public CharacterAttribute() {
        basePrimaryAttribute = new EnumMap<>(CharacterPrimaryAttribute.class);
        baseDerivedAttribute = new EnumMap<>(CharacterDerivedAttribute.class);

        additionalPrimaryAttribute = new EnumMap<>(CharacterPrimaryAttribute.class);
        additionalDerivedAttribute = new EnumMap<>(CharacterDerivedAttribute.class);

        characterListener = new NullCharacterListener();

        // Initialize value
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
                120 + (agility * 10.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_STAMINA,
                50 + ((vitality - 1) * 5.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_FULLNESS,
                100f);

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_HEALTH,
                80 + ((vitality - 1) * 10.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.HEALTH_REGENERATION,
                2 + (vitality * 0.5f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAX_MANA,
                20 + ((intelligence - 1) * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MANA_REGENERATION,
                10 + (intelligence * 0.2f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.PHYSICAL_DAMAGE,
                1 + (strength * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAGICAL_DAMAGE,
                1 + (intelligence * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.PHYSICAL_DEFENSE,
                1 + (vitality * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.MAGICAL_DEFENSE,
                1 + (intelligence * 1.0f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.ATTACK_SPEED,
                1 / (float) (1 - Math.sqrt(agility * 0.06f)));

        setBaseDerivedAttribute(CharacterDerivedAttribute.FULLNESS_DRAIN,
                1 / (float) ((1 - Math.sqrt(survival * 0.05f)) * 32f));

        setBaseDerivedAttribute(CharacterDerivedAttribute.CRAFTING,
                1.0f + survival);

        setBaseDerivedAttribute(CharacterDerivedAttribute.FISHING,
                1.0f + survival);

        setBaseDerivedAttribute(CharacterDerivedAttribute.TOOLS_EFFICIENCY,
                1 / (float) (1 - Math.sqrt(strength * 0.08f)));

        setBaseDerivedAttribute(CharacterDerivedAttribute.TOOLS_SPEED,
                1 / (float) (1 - Math.sqrt(agility * 0.05f)));

        setBaseDerivedAttribute(CharacterDerivedAttribute.TOOLS_LEVEL,
                1.0f + intelligence);

        setBaseDerivedAttribute(CharacterDerivedAttribute.ITEM_CHANCE,
                1 / (float) (1 - Math.sqrt(luck * 0.07f)));

        setBaseDerivedAttribute(CharacterDerivedAttribute.UPGRADE_CHANCE,
                1 / (float) (1 - Math.sqrt(luck * 0.05f)));

        setBaseDerivedAttribute(CharacterDerivedAttribute.EVENT_CHANCE,
                1 / (float) (1 - Math.sqrt(luck * 0.06f)));

        setBaseDerivedAttribute(CharacterDerivedAttribute.FRIENDSHIP,
                1 / (float) (1 - Math.sqrt(charisma * 0.06f)));

        setBaseDerivedAttribute(CharacterDerivedAttribute.SHOPPING,
                (float) (1 - Math.sqrt(charisma * 0.06f)));
    }

    public void setBaseDerivedAttribute(CharacterDerivedAttribute derivedAttribute, float value) {
        baseDerivedAttribute.put(derivedAttribute, value);
        characterListener.onDerivedAttributeChange(derivedAttribute);

        // Call onStatusChange() if it has effect on status
        switch (derivedAttribute) {
            case MAX_STAMINA:
                characterListener.onStatusChange(CharacterStatusType.STAMINA);
                break;
            case MAX_HEALTH:
                characterListener.onStatusChange(CharacterStatusType.HEALTH);
                break;
            case MAX_MANA:
                characterListener.onStatusChange(CharacterStatusType.MANA);
                break;
            case MAX_FULLNESS:
                characterListener.onStatusChange(CharacterStatusType.FULLNESS);
                break;
        }
    }

    public void setBasePrimary(CharacterPrimaryAttribute primaryAttribute, int value) {
        // set value between 1 and 99
        basePrimaryAttribute.put(primaryAttribute, MathUtils.clamp(value, 1, 99));
        calculateBaseDerived();
        characterListener.onPrimaryAttributeChange(primaryAttribute);
    }

    public void changeBasePrimary(CharacterPrimaryAttribute primaryAttribute, int changeValue) {
        setBasePrimary(primaryAttribute, getBasePrimary(primaryAttribute) + changeValue);
    }

    public void changeAdditionalPrimary(CharacterPrimaryAttribute primaryAttribute, int changeValue) {
        int currentValue = additionalPrimaryAttribute.get(primaryAttribute);
        additionalPrimaryAttribute.put(primaryAttribute, currentValue + changeValue);
        characterListener.onPrimaryAttributeChange(primaryAttribute);
    }

    public void changeAdditionalDerived(CharacterDerivedAttribute derivedAttribute, float changeValue) {
        float currentValue = additionalDerivedAttribute.get(derivedAttribute);
        additionalDerivedAttribute.put(derivedAttribute, currentValue + changeValue);
        characterListener.onDerivedAttributeChange(derivedAttribute);
    }

    public int getBasePrimary(CharacterPrimaryAttribute primaryAttribute) {
        return basePrimaryAttribute.get(primaryAttribute);
    }

    public int getPrimary(CharacterPrimaryAttribute primaryAttribute) {
        return getBasePrimary(primaryAttribute) + additionalPrimaryAttribute.get(primaryAttribute);
    }

    public float getBaseDerived(CharacterDerivedAttribute derivedAttribute) {
        return baseDerivedAttribute.get(derivedAttribute);
    }

    public float getDerived(CharacterDerivedAttribute derivedAttribute) {
        return getBaseDerived(derivedAttribute) + additionalDerivedAttribute.get(derivedAttribute);
    }

    public void setCharacterListener(CharacterListener characterListener) {
        this.characterListener = characterListener;
    }

}
