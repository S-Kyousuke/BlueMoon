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
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterPrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Created by Skyousuke <surasek@gmail.com> on 2/7/2559.
 */
public class Starve extends AbstractCharacterEffect {

    protected int lastStrengthPenalty;
    protected int lastVitalityPenalty;
    protected int lastAgilityPenalty;

    protected float strengthPenaltyPercent;
    protected float vitalityPenaltyPercent;
    protected float agilityPenaltyPercent;

    @Override
    public void enter(AbstractCharacter character) {
        strengthPenaltyPercent = 0.8f;
        vitalityPenaltyPercent = 0.8f;
        agilityPenaltyPercent = 0.3f;
    }

    @Override
    public void exit(AbstractCharacter character) {
        character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.STRENGTH, lastStrengthPenalty);
        character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.VITALITY, lastVitalityPenalty);
        character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.AGILITY, lastAgilityPenalty);
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {
        updatePenaltyValue(character);
        damageToPlayer(character, activeTime);
        expireIfHasFullness(character);
    }

    private void damageToPlayer(AbstractCharacter character, float activeTime) {
        character.getStatus().change(CharacterStatusType.HEALTH,
                -0.005f * character.getAttribute().getDerived(CharacterDerivedAttribute.MAX_HEALTH) * activeTime);
    }

    protected void expireIfHasFullness(AbstractCharacter character) {
        if (character.getStatus().get(CharacterStatusType.FULLNESS) > 0)
            character.removeEffect(this);
    }

    protected void updatePenaltyValue(AbstractCharacter character) {
        int strengthPenalty = (int) (character.getAttribute()
                .getBasePrimary(CharacterPrimaryAttribute.STRENGTH) * strengthPenaltyPercent);
        int vitalityPenalty = (int) (character.getAttribute()
                .getBasePrimary(CharacterPrimaryAttribute.VITALITY) * vitalityPenaltyPercent);
        int agilityPenalty = (int) (character.getAttribute()
                .getBasePrimary(CharacterPrimaryAttribute.AGILITY) * agilityPenaltyPercent);

        if (lastStrengthPenalty != strengthPenalty) {
            character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.STRENGTH, lastStrengthPenalty);
            lastStrengthPenalty = strengthPenalty;
            character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.STRENGTH, -lastStrengthPenalty);
        }
        if (lastVitalityPenalty != vitalityPenalty) {
            character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.VITALITY, lastVitalityPenalty);
            lastVitalityPenalty = vitalityPenalty;
            character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.VITALITY, -lastVitalityPenalty);
        }
        if (lastAgilityPenalty != agilityPenalty) {
            character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.AGILITY, lastAgilityPenalty);
            lastAgilityPenalty = agilityPenalty;
            character.getAttribute().changeAdditionalPrimary(CharacterPrimaryAttribute.AGILITY, -lastAgilityPenalty);
        }
    }

    @Override
    public String getName() {
        return "Starve";
    }
}
