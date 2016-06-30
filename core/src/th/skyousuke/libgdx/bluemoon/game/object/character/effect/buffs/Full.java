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

package th.skyousuke.libgdx.bluemoon.game.object.character.effect.buffs;

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterPrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

public class Full extends AbstractCharacterEffect {


    private int lastBonusStrength;
    private int lastBonusVitality;
    private int lastBonusAgility;

    private CharacterAttribute characterAttribute;

    @Override
    public void enter(AbstractCharacter character) {
        lastBonusStrength = (int)(characterAttribute.getBasePrimary(CharacterPrimaryAttribute.STRENGTH) * 0.20);
        lastBonusVitality = (int) (characterAttribute.getBasePrimary(CharacterPrimaryAttribute.VITALITY) * 0.20);
        lastBonusAgility = (int) (characterAttribute.getBasePrimary(CharacterPrimaryAttribute.AGILITY) * 0.05);

        characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.STRENGTH, lastBonusStrength);
        characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.VITALITY, lastBonusVitality);
        characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.AGILITY, lastBonusAgility);
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {

        int bonusStrength = (int)(characterAttribute.getBasePrimary(CharacterPrimaryAttribute.STRENGTH) * 0.20);
        int bonusVitality = (int)(characterAttribute.getBasePrimary(CharacterPrimaryAttribute.VITALITY) * 0.20);
        int bonusAgility = (int)(characterAttribute.getBasePrimary(CharacterPrimaryAttribute.AGILITY) * 0.05);

        if (lastBonusStrength != bonusStrength) {
            characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.STRENGTH, -lastBonusStrength);
            lastBonusStrength = bonusStrength;
            characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.STRENGTH, lastBonusStrength);
        }
        if (lastBonusVitality != bonusVitality) {
            characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.VITALITY, -lastBonusVitality);
            lastBonusVitality = bonusVitality;
            characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.VITALITY, lastBonusVitality);
        }
        if (lastBonusAgility != bonusAgility) {
            characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.AGILITY, -lastBonusAgility);
            lastBonusAgility = bonusAgility;
            characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.AGILITY, lastBonusAgility);
        }

    }

    @Override
    public void exit(AbstractCharacter character) {
        characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.STRENGTH, -lastBonusStrength);
        characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.VITALITY, -lastBonusVitality);
        characterAttribute.changeAdditionalPrimary(CharacterPrimaryAttribute.AGILITY, -lastBonusAgility);
    }

    @Override
    public String getName() {
        return "Full";
    }
}
