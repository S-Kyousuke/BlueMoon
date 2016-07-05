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

package th.skyousuke.libgdx.bluemoon.game.object.character.effect.others;

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.DerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.StatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Character running state
 * Created by Skyousuke <surasek@gmail.com> on 29/6/2559.
 */
public class Running extends AbstractCharacterEffect {

    private float bonusMovingSpeed;

    @Override
    public void enter(AbstractCharacter character) {
        bonusMovingSpeed = character.getAttribute().getDerived(DerivedAttribute.MOVING_SPEED) * 0.5f;
        character.getAttribute().addAdditionalDerived(DerivedAttribute.MOVING_SPEED, bonusMovingSpeed);
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {
        if (character.getStatus().getValue(StatusType.STAMINA) > 0) {
            character.getStatus().addValue(StatusType.STAMINA, -0.5f * activeTime);
        } else dispose();
    }

    @Override
    public void exit(AbstractCharacter character) {
        character.getAttribute().addAdditionalDerived(DerivedAttribute.MOVING_SPEED, -bonusMovingSpeed);
    }

    @Override
    public String getName() {
        return "Running";
    }
}
