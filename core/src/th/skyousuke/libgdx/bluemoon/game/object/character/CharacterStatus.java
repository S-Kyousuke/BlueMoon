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

import java.util.EnumMap;

public class CharacterStatus {

    private final EnumMap<CharacterStatusType, Float> allStatus;
    private final CharacterAttribute characterAttribute;

    private CharacterListener characterListener;

    // Create character status from character attribute and initialize all status to max value
    public CharacterStatus(CharacterAttribute characterAttribute) {
        allStatus = new EnumMap<>(CharacterStatusType.class);
        this.characterAttribute = characterAttribute;
        characterListener = new NullCharacterListener();

        for (CharacterStatusType type : CharacterStatusType.values()) {
            allStatus.put(type, 0f);
        }
        for (CharacterStatusType statusType : CharacterStatusType.values()) {
            max(statusType);
        }
    }

    public void max(CharacterStatusType statusType) {
        set(statusType, Float.MAX_VALUE);
    }

    public void set(CharacterStatusType statusType, float value) {
        float minValue = 0f;
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
        allStatus.put(statusType, MathUtils.clamp(value, minValue, maxValue));
        characterListener.onStatusChange(statusType);
    }

    public float get(CharacterStatusType statusType) {
        return allStatus.get(statusType);
    }

    public void change(CharacterStatusType statusType, float changeValue) {
        set(statusType, get(statusType) + changeValue);
    }

    public void setCharacterListener(CharacterListener characterListener) {
        this.characterListener = characterListener;
    }

}
