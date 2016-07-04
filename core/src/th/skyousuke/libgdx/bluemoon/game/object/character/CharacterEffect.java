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

package th.skyousuke.libgdx.bluemoon.game.object.character;

import com.badlogic.gdx.utils.Array;

import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Character effect class for storing and applying all effects to character.
 * Created by Skyousuke <surasek@gmail.com> on 4/7/2559.
 */
public class CharacterEffect {

    private final Array<AbstractCharacterEffect> effects;
    private final Array<CharacterEffectListener> listeners;
    private final AbstractCharacter character;

    public CharacterEffect(AbstractCharacter character) {
        effects = new Array<>();
        listeners = new Array<>();
        this.character = character;
    }

    public void apply(float deltaTime) {
        for (AbstractCharacterEffect effect : effects) {
            effect.apply(character, deltaTime);
        }
    }

    public void add(AbstractCharacterEffect effect) {
        effect.enter(character);
        effects.add(effect);
        for (CharacterEffectListener listener : listeners) {
            listener.onEffectAdd(effect);
        }
    }

    public void remove(AbstractCharacterEffect effect) {
        if (effects.removeValue(effect, true)) {
            effect.exit(character);
        }
        for (CharacterEffectListener listener : listeners) {
            listener.onEffectRemove(effect);
        }
    }

    public boolean has(AbstractCharacterEffect effect) {
        return effects.contains(effect, true);
    }

    public void addListener(CharacterEffectListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CharacterEffectListener listener) {
        listeners.removeValue(listener, true);
    }

    public Array<AbstractCharacterEffect> getAll() {
        return effects;
    }

}
