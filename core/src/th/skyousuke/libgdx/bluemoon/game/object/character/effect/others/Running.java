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

package th.skyousuke.libgdx.bluemoon.game.object.character.effect.others;

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Created by Skyousuke <surasek@gmail.com> on 29/6/2559.
 */
public class Running extends AbstractCharacterEffect {

    private float bonusMovingSpeed;

    @Override
    public void enter(AbstractCharacter character) {
        bonusMovingSpeed = character.getAttribute().getDerived(CharacterDerivedAttribute.MOVING_SPEED);
        character.getAttribute().changeAdditionalDerived(CharacterDerivedAttribute.MOVING_SPEED, bonusMovingSpeed);
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {
        if (character.getCharacterStatus().getStatus(CharacterStatusType.STAMINA) > 0) {
            character.getCharacterStatus().changeStatus(CharacterStatusType.STAMINA, -1.0f * activeTime);
        }
        else expire();
    }

    @Override
    public void exit(AbstractCharacter character) {
        character.getAttribute().changeAdditionalDerived(CharacterDerivedAttribute.MOVING_SPEED, -bonusMovingSpeed);
    }

    @Override
    public String getName() {
        return "Running";
    }
}
