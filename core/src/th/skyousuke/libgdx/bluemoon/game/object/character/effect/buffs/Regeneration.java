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
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

public class Regeneration extends AbstractCharacterEffect {

    private final float restorePerSecond;
    private final RegenerationType type;

    public Regeneration(RegenerationType type, float restorePerSecond, float duration) {
        super(duration);
        this.restorePerSecond = restorePerSecond;
        this.type = type;
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {
        switch (type) {
            case HEALTH:
                character.getStatus()
                        .change(CharacterStatusType.HEALTH, restorePerSecond * activeTime);
                break;
            case MANA:
                character.getStatus()
                        .change(CharacterStatusType.MANA, restorePerSecond * activeTime);
                break;
            case STAMINA:
                character.getStatus()
                        .change(CharacterStatusType.STAMINA, restorePerSecond * activeTime);
                break;
            case FULLNESS:
                character.getStatus()
                        .change(CharacterStatusType.FULLNESS, restorePerSecond * activeTime);
                break;
        }
    }

    @Override
    public String getName() {
        return type.name().substring(0, 1) + type.name().toLowerCase().substring(1) + " Regeneration";
    }
}
