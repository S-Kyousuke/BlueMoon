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

package th.skyousuke.libgdx.bluemoon.game.object.character.states;

import th.skyousuke.libgdx.bluemoon.game.object.AnimationKey;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterState;

/**
 * Character Attacking state.
 * Created by Skyousuke <surasek@gmail.com> on 27/6/2559.
 */
public class AttackingState extends CharacterState {

    public AttackingState(AbstractCharacter character) {
        super(character);
    }

    @Override
    protected void updateCharacter(float deltaTime) {
        if (character.isAnimationFinished(AnimationKey.ATK_DOWN)
                && character.isAnimationFinished(AnimationKey.ATK_UP)
                && character.isAnimationFinished(AnimationKey.ATK_LEFT)
                && character.isAnimationFinished(AnimationKey.ATK_RIGHT)) {
            character.setState(new IdlingState(character));
        }
    }

    @Override
    protected void setAnimation() {
        float attackTimeFactor = 0.25f;
        float attackTime = attackTimeFactor / character.getAttribute()
                .getDerived(CharacterDerivedAttribute.ATTACK_SPEED);

        switch (character.getViewDirection()) {
            case LEFT:
                character.setAnimation(AnimationKey.ATK_LEFT, attackTime);
                break;
            case RIGHT:
                character.setAnimation(AnimationKey.ATK_RIGHT, attackTime);
                break;
            case UP:
                character.setAnimation(AnimationKey.ATK_UP, attackTime);
                break;
            case DOWN:
                character.setAnimation(AnimationKey.ATK_DOWN, attackTime);
                break;
        }
    }
}
