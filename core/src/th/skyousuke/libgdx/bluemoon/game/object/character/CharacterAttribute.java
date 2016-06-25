/*
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.skyousuke.libgdx.bluemoon.game.object.character;

import com.badlogic.gdx.utils.Array;

public class CharacterAttribute {

    // 2 length array for base value and additional value

    private int[] basePrimaryAttribute;
    private float[] baseDerivedAttribute;

    private Array<int[]> additionalPrimaryAttribute;
    private Array<float[]> additionalDerivedAttribute;

    public CharacterAttribute() {
        basePrimaryAttribute = new int[PrimaryAttribute.values().length];
        baseDerivedAttribute = new float[DerivedAttribute.values().length];

        additionalPrimaryAttribute = new Array<>();
        additionalDerivedAttribute = new Array<>();

        // Initialize Attribute
        for (PrimaryAttribute primaryAttribute : PrimaryAttribute.values()) {
            setBasePrimary(primaryAttribute, 1);
        }
    }

    private void calculateBaseDerived() {
        int strength = getPrimary(PrimaryAttribute.STRENGTH);
        int agility = getPrimary(PrimaryAttribute.AGILITY);
        int intelligence = getPrimary(PrimaryAttribute.INTELLIGENCE);
        int vitality = getPrimary(PrimaryAttribute.VITALITY);
        int charisma = getPrimary(PrimaryAttribute.CHARISMA);
        int luck = getPrimary(PrimaryAttribute.LUCK);
        int survival = getPrimary(PrimaryAttribute.SURVIVAL);

        baseDerivedAttribute[DerivedAttribute.MOVING_SPEED.ordinal()] =
                120 + (agility * 10.0f);

        baseDerivedAttribute[DerivedAttribute.MAX_STAMINA.ordinal()] =
                100 + (vitality * 5.0f);

        baseDerivedAttribute[DerivedAttribute.MAX_HEALTH.ordinal()] =
                100 + (vitality * 10.0f);

        baseDerivedAttribute[DerivedAttribute.HEALTH_REGENERATION.ordinal()] =
                2 + (vitality * 0.5f);

        baseDerivedAttribute[DerivedAttribute.MAX_MANA.ordinal()] =
                10 + (intelligence * 1.0f);

        baseDerivedAttribute[DerivedAttribute.MANA_REGENERATION.ordinal()] =
                10 + (intelligence * 0.2f);

        baseDerivedAttribute[DerivedAttribute.PHYSICAL_DAMAGE.ordinal()] =
                1 + (strength * 1.0f);

        baseDerivedAttribute[DerivedAttribute.MAGICAL_DAMAGE.ordinal()] =
                1 + (intelligence * 1.0f);

        baseDerivedAttribute[DerivedAttribute.PHYSICAL_DEFENSE.ordinal()] =
                1 + (vitality * 1.0f);

        baseDerivedAttribute[DerivedAttribute.MAGICAL_DEFENSE.ordinal()] =
                1 + (intelligence * 1.0f);

        baseDerivedAttribute[DerivedAttribute.ATTACK_SPEED.ordinal()] =
                1 / (float) (1 - Math.sqrt(agility * 0.06f));

        baseDerivedAttribute[DerivedAttribute.FULLNESS_DRAIN.ordinal()] =
                1 / (float) (1 - Math.sqrt(survival * 0.05f));

        baseDerivedAttribute[DerivedAttribute.CRAFTING.ordinal()] =
                1.0f + survival;

        baseDerivedAttribute[DerivedAttribute.FISHING.ordinal()] =
                1.0f + survival;

        baseDerivedAttribute[DerivedAttribute.TOOLS_EFFICIENCY.ordinal()] =
                1 / (float) (1 - Math.sqrt(strength * 0.08f));

        baseDerivedAttribute[DerivedAttribute.TOOLS_SPEED.ordinal()] =
                1 / (float) (1 - Math.sqrt(agility * 0.05f));

        baseDerivedAttribute[DerivedAttribute.TOOLS_LEVEL.ordinal()] =
                1.0f + intelligence;

        baseDerivedAttribute[DerivedAttribute.ITEM_CHANCE.ordinal()] =
                1 / (float) (1 - Math.sqrt(luck * 0.07f));

        baseDerivedAttribute[DerivedAttribute.UPGRADE_CHANCE.ordinal()] =
                1 / (float) (1 - Math.sqrt(luck * 0.05f));

        baseDerivedAttribute[DerivedAttribute.EVENT_CHANCE.ordinal()] =
                1 / (float) (1 - Math.sqrt(luck * 0.06f));

        baseDerivedAttribute[DerivedAttribute.FRIENDSHIP.ordinal()] =
                1 / (float) (1 - Math.sqrt(charisma * 0.06f));

        baseDerivedAttribute[DerivedAttribute.SHOPPING.ordinal()] =
                (float) (1 - Math.sqrt(charisma * 0.06f));
    }

    public void setBasePrimary(PrimaryAttribute primaryAttribute, int value) {
        basePrimaryAttribute[primaryAttribute.ordinal()] = value;
        calculateBaseDerived();
    }

    //  Set/Change value methods

    public void changeBasePrimary(PrimaryAttribute primaryAttribute, int changeValue) {
        setBasePrimary(primaryAttribute, getBasePrimary(primaryAttribute) + changeValue);
    }

    public void addAdditionalPrimary(int[] value) {
        if (value.length != PrimaryAttribute.values().length) {
            throw new IllegalArgumentException("addAdditionalPrimary: Invalid array size!");
        }
        additionalPrimaryAttribute.add(value);
    }

    public void addAdditionalDerived(float[] value) {
        if (value.length != DerivedAttribute.values().length) {
            throw new IllegalArgumentException("addAdditionalDerived: Invalid array size!");
        }
        additionalDerivedAttribute.add(value);
    }

    public void removeAdditionalPrimary(int[] value) {
        additionalPrimaryAttribute.removeValue(value, true);
    }

    public void removeAdditionalDerived(float[] value) {
        additionalDerivedAttribute.removeValue(value, true);
    }

    public int getBasePrimary(PrimaryAttribute primaryAttribute) {
        return basePrimaryAttribute[primaryAttribute.ordinal()];
    }

    public int getTotalAdditionalPrimary(PrimaryAttribute primaryAttribute) {
        int totalAdditionalPrimary = 0;
        for (int[] additionalPrimary : additionalPrimaryAttribute) {
            totalAdditionalPrimary += additionalPrimary[primaryAttribute.ordinal()];
        }
        return totalAdditionalPrimary;
    }

    public int getPrimary(PrimaryAttribute primaryAttribute) {
        return getBasePrimary(primaryAttribute) + getTotalAdditionalPrimary(primaryAttribute);
    }

    public float getBaseDerived(DerivedAttribute derivedAttribute) {
        return baseDerivedAttribute[derivedAttribute.ordinal()];
    }

    public float getTotalAdditionalDerived(DerivedAttribute derivedAttribute) {
        float totalAdditionalDerived = 0;
        for (float[] additionalDerived : additionalDerivedAttribute) {
            totalAdditionalDerived += additionalDerived[derivedAttribute.ordinal()];
        }
        return totalAdditionalDerived;
    }

    public float getDerived(DerivedAttribute derivedAttribute) {
        return getBaseDerived(derivedAttribute) + getTotalAdditionalDerived(derivedAttribute);
    }

    public enum PrimaryAttribute {
        STRENGTH,
        AGILITY,
        VITALITY,
        INTELLIGENCE,
        CHARISMA,
        LUCK,
        SURVIVAL
    }

    public enum DerivedAttribute {
        MOVING_SPEED,
        MAX_STAMINA,
        MAX_HEALTH,
        MAX_MANA,
        HEALTH_REGENERATION,
        MANA_REGENERATION,
        PHYSICAL_DAMAGE,
        MAGICAL_DAMAGE,
        PHYSICAL_DEFENSE,
        MAGICAL_DEFENSE,
        ATTACK_SPEED,
        CRAFTING,
        FISHING,
        FULLNESS_DRAIN,
        TOOLS_EFFICIENCY,
        TOOLS_SPEED,
        TOOLS_LEVEL,
        ITEM_CHANCE,
        UPGRADE_CHANCE,
        EVENT_CHANCE,
        FRIENDSHIP,
        SHOPPING
    }

}
