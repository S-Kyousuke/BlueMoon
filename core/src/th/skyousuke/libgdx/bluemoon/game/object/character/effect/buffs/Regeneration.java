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
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterState.StateType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;

public class Regeneration extends AbstractEffect {

    private final float restorePerSecond;
    private final RegenerationType type;

    public Regeneration(AbstractCharacter character, RegenerationType type, float restorePerSecond, float duration) {
        super(character, duration);
        this.restorePerSecond = restorePerSecond;
        this.type = type;
    }

    @Override
    protected void enterEffect() {
    }

    @Override
    protected void overTimeEffect(float activeTime) {
        switch (type) {
            case HEALTH:
                character.changeState(StateType.HEALTH, restorePerSecond * activeTime);
                break;
            case MANA:
                character.changeState(StateType.MANA, restorePerSecond * activeTime);
                break;
            case STAMINA:
                character.changeState(StateType.STAMINA, restorePerSecond * activeTime);
                break;
            case FULLNESS:
                character.changeState(StateType.FULLNESS, restorePerSecond * activeTime);
                break;
        }
    }

    @Override
    protected void exitEffect() {
    }

    public enum RegenerationType {
        HEALTH,
        MANA,
        STAMINA,
        FULLNESS
    }
}
