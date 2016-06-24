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

package th.skyousuke.libgdx.bluemoon.game.object.character;

import com.badlogic.gdx.math.MathUtils;

public class CharacterStatus {

    private float[] status;
    private CharacterAttribute characterAttribute;

    public CharacterStatus(CharacterAttribute characterAttribute) {
        status = new float[StatusType.values().length];
        this.characterAttribute = characterAttribute;
    }

    public void setStatus(StatusType statusType, float value) {
        float minValue = 0f;
        float maxValue;

        switch (statusType) {
            case HEALTH:
                maxValue = characterAttribute.getDerived(CharacterAttribute.DerivedAttribute.MAX_HEALTH);
                break;
            case MANA:
                maxValue = characterAttribute.getDerived(CharacterAttribute.DerivedAttribute.MAX_MANA);
                break;
            case STAMINA:
                maxValue = characterAttribute.getDerived(CharacterAttribute.DerivedAttribute.MAX_STAMINA);
                break;
            case FULLNESS:
                maxValue = 100f;
                break;
            default:
                maxValue = Float.MAX_VALUE;
        }

        status[statusType.ordinal()] = MathUtils.clamp(value, minValue, maxValue);
    }

    public float getStatus(StatusType statusType) {
        return status[statusType.ordinal()];
    }

    public void changeStatus(StatusType statusType, float changeValue) {
        setStatus(statusType, getStatus(statusType) + changeValue);
    }

    public enum StatusType {
        HEALTH,
        MANA,
        STAMINA,
        FULLNESS
    }

}
