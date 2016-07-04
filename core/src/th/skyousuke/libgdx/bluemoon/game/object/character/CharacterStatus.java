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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

public class CharacterStatus {

    private final EnumMap<CharacterStatusType, Float> status;
    private final CharacterAttribute characterAttribute;

    private final Array<CharacterAttributeAndStatusListener> listeners;

    // Create character status from character attribute and initialize all status to max value
    public CharacterStatus(CharacterAttribute characterAttribute) {
        status = new EnumMap<>(CharacterStatusType.class);
        this.characterAttribute = characterAttribute;
        listeners = new Array<>();

        for (CharacterStatusType type : CharacterStatusType.values()) {
            status.put(type, 0f);
        }
    }

    public void setToMax() {
        for (CharacterStatusType type : CharacterStatusType.values()) {
            setToMax(type);
        }
    }

    public void setToMax(CharacterStatusType statusType) {
        setValue(statusType, Float.MAX_VALUE);
    }

    public void setValue(CharacterStatusType statusType, float value) {
        float oldValue = getValue(statusType);
        float maxValue = 0f;
        switch (statusType) {
            case HEALTH:
                maxValue = characterAttribute.getDerived(CharacterDerivedAttribute.MAX_HEALTH);
                break;
            case MANA:
                maxValue = characterAttribute.getDerived(CharacterDerivedAttribute.MAX_MANA);
                break;
            case STAMINA:
                maxValue = characterAttribute.getDerived(CharacterDerivedAttribute.MAX_STAMINA);
                break;
            case FULLNESS:
                maxValue = characterAttribute.getDerived(CharacterDerivedAttribute.MAX_FULLNESS);
                break;
        }
        status.put(statusType, MathUtils.clamp(value, 0, maxValue));
        for (CharacterAttributeAndStatusListener listener : listeners) {
            listener.onStatusChange(statusType, oldValue, getValue(statusType));
        }
    }

    public float getValue(CharacterStatusType statusType) {
        return status.get(statusType);
    }

    public void addValue(CharacterStatusType statusType, float value) {
        setValue(statusType, getValue(statusType) + value);
    }

    public void addListener(CharacterAttributeAndStatusListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CharacterAttributeAndStatusListener listener) {
        listeners.removeValue(listener, true);
    }

}
