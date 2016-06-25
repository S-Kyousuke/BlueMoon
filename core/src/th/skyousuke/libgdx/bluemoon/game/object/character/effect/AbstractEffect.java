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

    protected final AbstractCharacter character;
    protected float remainingTime;

    public AbstractEffect(AbstractCharacter character, float duration) {
        this.character = character;
        remainingTime = duration;
        enterEffect();
    }

    public void apply(float deltaTime) {
        if (isExpire()) return;
        if (remainingTime > deltaTime) {
            overTimeEffect(deltaTime);
            remainingTime -= deltaTime;
        } else {
            overTimeEffect(remainingTime);
            remainingTime = 0;
            exitEffect();
        }
    }

    public boolean isExpire() {
        return remainingTime == 0;
    }

    protected abstract void enterEffect();

    protected abstract void overTimeEffect(float activeTime);

    protected abstract void exitEffect();

}
