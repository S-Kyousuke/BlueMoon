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

package th.skyousuke.libgdx.bluemoon.game.object.character.effect.buffs;

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.DerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.StatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

public class Full extends AbstractCharacterEffect {

    private float lastBonusHealthRegeneration;
    private float lastBonusManaRegeneration;

    @Override
    public void enter(AbstractCharacter character) {
        lastBonusHealthRegeneration = character.getAttribute()
                .getBaseDerived(DerivedAttribute.HEALTH_REGENERATION) * 0.5f;
        lastBonusManaRegeneration = character.getAttribute()
                .getBaseDerived(DerivedAttribute.MANA_REGENERATION) * 0.5f;

        character.getAttribute().addAdditionalDerived(DerivedAttribute.HEALTH_REGENERATION,
                lastBonusHealthRegeneration);
        character.getAttribute().addAdditionalDerived(DerivedAttribute.MANA_REGENERATION,
                lastBonusManaRegeneration);
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {
        float bonusHealthRegeneration = character.getAttribute()
                .getBaseDerived(DerivedAttribute.HEALTH_REGENERATION) * 0.5f;
        float bonusManaRegeneration = character.getAttribute()
                .getBaseDerived(DerivedAttribute.MANA_REGENERATION) * 0.5f;

        if (lastBonusHealthRegeneration != bonusHealthRegeneration) {
            character.getAttribute().addAdditionalDerived(DerivedAttribute.HEALTH_REGENERATION,
                    -lastBonusHealthRegeneration);
            lastBonusHealthRegeneration = bonusHealthRegeneration;
            character.getAttribute().addAdditionalDerived(DerivedAttribute.HEALTH_REGENERATION,
                    lastBonusHealthRegeneration);
        }
        if (lastBonusManaRegeneration != bonusManaRegeneration) {
            character.getAttribute().addAdditionalDerived(DerivedAttribute.MANA_REGENERATION,
                    -lastBonusManaRegeneration);
            lastBonusManaRegeneration = bonusManaRegeneration;
            character.getAttribute().addAdditionalDerived(DerivedAttribute.MANA_REGENERATION,
                    lastBonusManaRegeneration);
        }

        character.getStatus().addValue(StatusType.STAMINA, character.getAttribute()
                .getDerived(DerivedAttribute.MAX_STAMINA) * 0.005f * activeTime);

        if (character.getStatus().getValue(StatusType.FULLNESS) < 40.0) dispose();
    }

    @Override
    public void exit(AbstractCharacter character) {
        character.getAttribute().addAdditionalDerived(DerivedAttribute.HEALTH_REGENERATION,
                -lastBonusHealthRegeneration);
        character.getAttribute().addAdditionalDerived(DerivedAttribute.MANA_REGENERATION,
                -lastBonusManaRegeneration);
    }

    @Override
    public String getName() {
        return "Full";
    }
}
