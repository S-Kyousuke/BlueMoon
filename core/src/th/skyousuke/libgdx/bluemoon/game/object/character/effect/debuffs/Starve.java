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

package th.skyousuke.libgdx.bluemoon.game.object.character.effect.debuffs;

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.DerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.PrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.StatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Debuff applied to character when character has no fullness for a long time.
 * Created by Skyousuke <surasek@gmail.com> on 2/7/2559.
 */
public class Starve extends AbstractCharacterEffect {

    protected int lastStrengthPenalty;
    protected int lastVitalityPenalty;
    protected int lastAgilityPenalty;

    protected float strengthPenaltyPercent;
    protected float vitalityPenaltyPercent;
    protected float agilityPenaltyPercent;

    protected float lastHealRegeneration;
    protected float lastManaRegeneration;

    @Override
    public void enter(AbstractCharacter character) {
        strengthPenaltyPercent = 0.8f;
        vitalityPenaltyPercent = 0.8f;
        agilityPenaltyPercent = 0.3f;
    }

    @Override
    public void exit(AbstractCharacter character) {
        character.getAttribute().addAdditionalPrimary(PrimaryAttribute.STRENGTH, lastStrengthPenalty);
        character.getAttribute().addAdditionalPrimary(PrimaryAttribute.VITALITY, lastVitalityPenalty);
        character.getAttribute().addAdditionalPrimary(PrimaryAttribute.AGILITY, lastAgilityPenalty);
        character.getAttribute()
                .addAdditionalDerived(DerivedAttribute.HEALTH_REGENERATION, lastHealRegeneration);
        character.getAttribute()
                .addAdditionalDerived(DerivedAttribute.MANA_REGENERATION, lastManaRegeneration);
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {
        updatePenaltyValue(character);
        damageToPlayer(character, activeTime);
        expireIfHasFullness(character);
    }

    private void damageToPlayer(AbstractCharacter character, float activeTime) {
        character.getStatus().addValue(StatusType.HEALTH, -0.005f * character.getAttribute()
                .getDerived(DerivedAttribute.MAX_HEALTH) * activeTime);
    }

    protected void expireIfHasFullness(AbstractCharacter character) {
        if (character.getStatus().getValue(StatusType.FULLNESS) > 0)
            character.getEffect().remove(this);
    }

    protected void updatePenaltyValue(AbstractCharacter character) {
        int strengthPenalty = (int) (character.getAttribute()
                .getBasePrimary(PrimaryAttribute.STRENGTH) * strengthPenaltyPercent);
        int vitalityPenalty = (int) (character.getAttribute()
                .getBasePrimary(PrimaryAttribute.VITALITY) * vitalityPenaltyPercent);
        int agilityPenalty = (int) (character.getAttribute()
                .getBasePrimary(PrimaryAttribute.AGILITY) * agilityPenaltyPercent);

        if (lastStrengthPenalty != strengthPenalty) {
            character.getAttribute().addAdditionalPrimary(PrimaryAttribute.STRENGTH, lastStrengthPenalty);
            lastStrengthPenalty = strengthPenalty;
            character.getAttribute().addAdditionalPrimary(PrimaryAttribute.STRENGTH, -lastStrengthPenalty);
        }
        if (lastVitalityPenalty != vitalityPenalty) {
            character.getAttribute().addAdditionalPrimary(PrimaryAttribute.VITALITY, lastVitalityPenalty);
            lastVitalityPenalty = vitalityPenalty;
            character.getAttribute().addAdditionalPrimary(PrimaryAttribute.VITALITY, -lastVitalityPenalty);
        }
        if (lastAgilityPenalty != agilityPenalty) {
            character.getAttribute().addAdditionalPrimary(PrimaryAttribute.AGILITY, lastAgilityPenalty);
            lastAgilityPenalty = agilityPenalty;
            character.getAttribute().addAdditionalPrimary(PrimaryAttribute.AGILITY, -lastAgilityPenalty);
        }

        float healRegeneration = character.getAttribute().getBaseDerived(DerivedAttribute.HEALTH_REGENERATION);
        float manaRegeneration = character.getAttribute().getBaseDerived(DerivedAttribute.MANA_REGENERATION);

        if (lastHealRegeneration != healRegeneration) {
            character.getAttribute()
                    .addAdditionalDerived(DerivedAttribute.HEALTH_REGENERATION, lastHealRegeneration);
            lastHealRegeneration = healRegeneration;
            character.getAttribute()
                    .addAdditionalDerived(DerivedAttribute.HEALTH_REGENERATION, -lastHealRegeneration);
        }
        if (lastManaRegeneration != manaRegeneration) {
            character.getAttribute()
                    .addAdditionalDerived(DerivedAttribute.MANA_REGENERATION, lastManaRegeneration);
            lastManaRegeneration = manaRegeneration;
            character.getAttribute()
                    .addAdditionalDerived(DerivedAttribute.MANA_REGENERATION, -lastManaRegeneration);
        }
    }

    @Override
    public String getName() {
        return "Starve";
    }
}
