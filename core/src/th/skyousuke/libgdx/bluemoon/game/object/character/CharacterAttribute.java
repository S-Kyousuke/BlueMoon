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

    private final int[] basePrimaryAttribute;
    private final float[] baseDerivedAttribute;

    private final Array<int[]> additionalPrimaryAttribute;
    private final Array<float[]> additionalDerivedAttribute;

    public CharacterAttribute() {
        basePrimaryAttribute = new int[CharacterPrimaryAttribute.values().length];
        baseDerivedAttribute = new float[CharacterDerivedAttribute.values().length];

        additionalPrimaryAttribute = new Array<>();
        additionalDerivedAttribute = new Array<>();

        // Initialize Attribute
        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            setBasePrimary(primaryAttribute, 1);
        }
    }

    private void calculateBaseDerived() {
        int strength = getPrimary(CharacterPrimaryAttribute.STRENGTH);
        int agility = getPrimary(CharacterPrimaryAttribute.AGILITY);
        int intelligence = getPrimary(CharacterPrimaryAttribute.INTELLIGENCE);
        int vitality = getPrimary(CharacterPrimaryAttribute.VITALITY);
        int charisma = getPrimary(CharacterPrimaryAttribute.CHARISMA);
        int luck = getPrimary(CharacterPrimaryAttribute.LUCK);
        int survival = getPrimary(CharacterPrimaryAttribute.SURVIVAL);

        baseDerivedAttribute[CharacterDerivedAttribute.MOVING_SPEED.ordinal()] =
                120 + (agility * 10.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.MAX_STAMINA.ordinal()] =
                50 + ((vitality - 1) * 5.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.MAX_FULLNESS.ordinal()] =
                100f;

        baseDerivedAttribute[CharacterDerivedAttribute.MAX_HEALTH.ordinal()] =
                80 + ((vitality - 1) * 10.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.HEALTH_REGENERATION.ordinal()] =
                2 + (vitality * 0.5f);

        baseDerivedAttribute[CharacterDerivedAttribute.MAX_MANA.ordinal()] =
                20 + ((intelligence - 1) * 1.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.MANA_REGENERATION.ordinal()] =
                10 + (intelligence * 0.2f);

        baseDerivedAttribute[CharacterDerivedAttribute.PHYSICAL_DAMAGE.ordinal()] =
                1 + (strength * 1.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.MAGICAL_DAMAGE.ordinal()] =
                1 + (intelligence * 1.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.PHYSICAL_DEFENSE.ordinal()] =
                1 + (vitality * 1.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.MAGICAL_DEFENSE.ordinal()] =
                1 + (intelligence * 1.0f);

        baseDerivedAttribute[CharacterDerivedAttribute.ATTACK_SPEED.ordinal()] =
                1 / (float) (1 - Math.sqrt(agility * 0.06f));

        baseDerivedAttribute[CharacterDerivedAttribute.FULLNESS_DRAIN.ordinal()] =
                1 / (float) ((1 - Math.sqrt(survival * 0.05f)) * 32f);

        baseDerivedAttribute[CharacterDerivedAttribute.CRAFTING.ordinal()] =
                1.0f + survival;

        baseDerivedAttribute[CharacterDerivedAttribute.FISHING.ordinal()] =
                1.0f + survival;

        baseDerivedAttribute[CharacterDerivedAttribute.TOOLS_EFFICIENCY.ordinal()] =
                1 / (float) (1 - Math.sqrt(strength * 0.08f));

        baseDerivedAttribute[CharacterDerivedAttribute.TOOLS_SPEED.ordinal()] =
                1 / (float) (1 - Math.sqrt(agility * 0.05f));

        baseDerivedAttribute[CharacterDerivedAttribute.TOOLS_LEVEL.ordinal()] =
                1.0f + intelligence;

        baseDerivedAttribute[CharacterDerivedAttribute.ITEM_CHANCE.ordinal()] =
                1 / (float) (1 - Math.sqrt(luck * 0.07f));

        baseDerivedAttribute[CharacterDerivedAttribute.UPGRADE_CHANCE.ordinal()] =
                1 / (float) (1 - Math.sqrt(luck * 0.05f));

        baseDerivedAttribute[CharacterDerivedAttribute.EVENT_CHANCE.ordinal()] =
                1 / (float) (1 - Math.sqrt(luck * 0.06f));

        baseDerivedAttribute[CharacterDerivedAttribute.FRIENDSHIP.ordinal()] =
                1 / (float) (1 - Math.sqrt(charisma * 0.06f));

        baseDerivedAttribute[CharacterDerivedAttribute.SHOPPING.ordinal()] =
                (float) (1 - Math.sqrt(charisma * 0.06f));
    }

    public void setBasePrimary(CharacterPrimaryAttribute primaryAttribute, int value) {
        basePrimaryAttribute[primaryAttribute.ordinal()] = value;
        calculateBaseDerived();
    }

    //  Set/Change value methods

    public void changeBasePrimary(CharacterPrimaryAttribute primaryAttribute, int changeValue) {
        setBasePrimary(primaryAttribute, getBasePrimary(primaryAttribute) + changeValue);
    }

    public void addAdditionalPrimary(int[] value) {
        if (value.length != CharacterPrimaryAttribute.values().length) {
            throw new IllegalArgumentException("addAdditionalPrimary: Invalid array size!");
        }
        additionalPrimaryAttribute.add(value);
    }

    public void addAdditionalDerived(float[] value) {
        if (value.length != CharacterDerivedAttribute.values().length) {
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

    public int getBasePrimary(CharacterPrimaryAttribute primaryAttribute) {
        return basePrimaryAttribute[primaryAttribute.ordinal()];
    }

    public int getTotalAdditionalPrimary(CharacterPrimaryAttribute primaryAttribute) {
        int totalAdditionalPrimary = 0;
        for (int[] additionalPrimary : additionalPrimaryAttribute) {
            totalAdditionalPrimary += additionalPrimary[primaryAttribute.ordinal()];
        }
        return totalAdditionalPrimary;
    }

    public int getPrimary(CharacterPrimaryAttribute primaryAttribute) {
        return getBasePrimary(primaryAttribute) + getTotalAdditionalPrimary(primaryAttribute);
    }

    public int[] getPrimary() {
        int[] totalPrimary = new int[CharacterPrimaryAttribute.values().length];
        for (CharacterPrimaryAttribute primaryAttribute : CharacterPrimaryAttribute.values()) {
            totalPrimary[primaryAttribute.ordinal()] += (basePrimaryAttribute[primaryAttribute.ordinal()]
                    + getTotalAdditionalPrimary(primaryAttribute));
        }

        return totalPrimary;
    }

    public float getBaseDerived(CharacterDerivedAttribute derivedAttribute) {
        return baseDerivedAttribute[derivedAttribute.ordinal()];
    }

    public float getTotalAdditionalDerived(CharacterDerivedAttribute derivedAttribute) {
        float totalAdditionalDerived = 0;
        for (float[] additionalDerived : additionalDerivedAttribute) {
            totalAdditionalDerived += additionalDerived[derivedAttribute.ordinal()];
        }
        return totalAdditionalDerived;
    }

    public float getDerived(CharacterDerivedAttribute derivedAttribute) {
        return getBaseDerived(derivedAttribute) + getTotalAdditionalDerived(derivedAttribute);
    }

    public float[] getDerived() {
        float[] totalDerived = new float[CharacterDerivedAttribute.values().length];
        for (CharacterDerivedAttribute derivedAttribute : CharacterDerivedAttribute.values()) {
            totalDerived[derivedAttribute.ordinal()] += (baseDerivedAttribute[derivedAttribute.ordinal()]
                    + getTotalAdditionalDerived(derivedAttribute));
        }
        return totalDerived;
    }

}
