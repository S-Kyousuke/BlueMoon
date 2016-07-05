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

public class Status {

    private final EnumMap<StatusType, Float> status;
    private final Attribute attribute;

    private final Array<AttributeAndStatusListener> listeners;

    // Create character status from character attribute and initialize all status to max value
    public Status(Attribute attribute) {
        status = new EnumMap<>(StatusType.class);
        this.attribute = attribute;
        listeners = new Array<>();

        for (StatusType type : StatusType.values()) {
            status.put(type, 0f);
        }
    }

    public void setToMax() {
        for (StatusType type : StatusType.values()) {
            setToMax(type);
        }
    }

    public void setToMax(StatusType statusType) {
        setValue(statusType, Float.MAX_VALUE);
    }

    public void setValue(StatusType statusType, float value) {
        float oldValue = getValue(statusType);
        float maxValue = 0f;
        switch (statusType) {
            case HEALTH:
                maxValue = attribute.getDerived(DerivedAttribute.MAX_HEALTH);
                break;
            case MANA:
                maxValue = attribute.getDerived(DerivedAttribute.MAX_MANA);
                break;
            case STAMINA:
                maxValue = attribute.getDerived(DerivedAttribute.MAX_STAMINA);
                break;
            case FULLNESS:
                maxValue = attribute.getDerived(DerivedAttribute.MAX_FULLNESS);
                break;
        }
        status.put(statusType, MathUtils.clamp(value, 0, maxValue));
        for (AttributeAndStatusListener listener : listeners) {
            listener.onStatusChange(statusType, oldValue, getValue(statusType));
        }
    }

    public float getValue(StatusType statusType) {
        return status.get(statusType);
    }

    public void addValue(StatusType statusType, float value) {
        setValue(statusType, getValue(statusType) + value);
    }

    public void addListener(AttributeAndStatusListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AttributeAndStatusListener listener) {
        listeners.removeValue(listener, true);
    }

}
