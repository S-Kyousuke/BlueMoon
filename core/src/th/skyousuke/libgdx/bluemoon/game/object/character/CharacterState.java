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

public class CharacterState {

    private float[] allState;
    private CharacterAttribute characterAttribute;

    public CharacterState(CharacterAttribute characterAttribute) {
        allState = new float[StateType.values().length];
        this.characterAttribute = characterAttribute;
    }

    public void setState(StateType stateType, float value) {
        float minValue = 0f;
        float maxValue;

        switch (stateType) {
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

        allState[stateType.ordinal()] = MathUtils.clamp(value, minValue, maxValue);
    }

    public float getState(StateType stateType) {
        return allState[stateType.ordinal()];
    }

    public void addValue(StateType stateType, float changeValue) {
        setState(stateType, getState(stateType) + changeValue);
    }

    public enum StateType {
        HEALTH,
        MANA,
        STAMINA,
        FULLNESS
    }

}
