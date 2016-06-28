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
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterPrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;

public class Full extends AbstractEffect {

    private final int[] primaryBonus;
    private boolean active;

    public Full(AbstractCharacter character, float duration) {
        super(character, duration);
        primaryBonus = new int[CharacterPrimaryAttribute.values().length];
    }

    @Override
    protected void enterEffect() {
        character.getAttribute().addAdditionalPrimary(primaryBonus);
    }

    @Override
    protected void overTimeEffect(float activeTime) {
        primaryBonus[CharacterPrimaryAttribute.STRENGTH.ordinal()] =
                (int) (character.getAttribute().getBasePrimary(CharacterPrimaryAttribute.STRENGTH) * 0.20);
        primaryBonus[CharacterPrimaryAttribute.VITALITY.ordinal()] =
                (int) (character.getAttribute().getBasePrimary(CharacterPrimaryAttribute.VITALITY) * 0.20);
        primaryBonus[CharacterPrimaryAttribute.AGILITY.ordinal()] =
                (int) (character.getAttribute().getBasePrimary(CharacterPrimaryAttribute.AGILITY) * 0.05);
    }

    @Override
    protected void exitEffect() {
        character.getAttribute().removeAdditionalPrimary(primaryBonus);
    }

}
