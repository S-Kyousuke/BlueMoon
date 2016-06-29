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

package th.skyousuke.libgdx.bluemoon.game.object.character.effect;

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;

public abstract class AbstractEffect {

    protected float remainingTime;

    protected AbstractEffect(float duration) {
        remainingTime = duration;
    }

    // forever effect
    protected AbstractEffect() {
        this(Float.MAX_VALUE);
    }

    public void apply(AbstractCharacter character, float deltaTime) {
        if (isExpire()) return;
        if (remainingTime == Float.MAX_VALUE) {
            overTimeEffect(character, deltaTime);
        } else {
            if (remainingTime > deltaTime) {
                overTimeEffect(character, deltaTime);
                remainingTime -= deltaTime;
            } else {
                overTimeEffect(character, remainingTime);
                remainingTime = 0;
            }
        }
    }

    public boolean isExpire() {
        return remainingTime == 0;
    }

    public void enter(AbstractCharacter character) {

    }

    public void exit(AbstractCharacter character) {

    }

    protected abstract void overTimeEffect(AbstractCharacter character, float activeTime);

}
