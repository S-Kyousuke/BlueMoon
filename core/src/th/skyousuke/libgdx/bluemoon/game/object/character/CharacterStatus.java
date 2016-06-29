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

    private final float[] allStatus;
    private final CharacterAttribute characterAttribute;

    public CharacterStatus(CharacterAttribute characterAttribute) {
        allStatus = new float[CharacterStatusType.values().length];
        this.characterAttribute = characterAttribute;
        setToMax();
    }

    public void setToMax() {
        setStatus(CharacterStatusType.HEALTH, Float.MAX_VALUE);
        setStatus(CharacterStatusType.MANA, Float.MAX_VALUE);
        setStatus(CharacterStatusType.STAMINA, Float.MAX_VALUE);
        setStatus(CharacterStatusType.FULLNESS, Float.MAX_VALUE);
    }

    public void setStatus(CharacterStatusType statusType, float value) {
        float minValue = 0f;
        float maxValue = Float.MAX_VALUE;

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
        allStatus[statusType.ordinal()] = MathUtils.clamp(value, minValue, maxValue);
    }

    public float getStatus(CharacterStatusType statusType) {
        return allStatus[statusType.ordinal()];
    }

    public void addValue(CharacterStatusType statusType, float changeValue) {
        setStatus(statusType, getStatus(statusType) + changeValue);

    }

}
