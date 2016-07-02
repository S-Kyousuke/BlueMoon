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

package th.skyousuke.libgdx.bluemoon.game.object.character.effect.debuffs;

import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Created by Skyousuke <surasek@gmail.com> on 2/7/2559.
 */
public class Hungry extends Starve {

    private float levelActiveTime;
    private int level = 1;
    private AbstractCharacterEffect effectAfterDispose;

    public Hungry(int level) {
        this.level = level;
    }

    @Override
    public void enter(AbstractCharacter character) {
    }

    @Override
    public void exit(AbstractCharacter character) {
        super.exit(character);
        if (effectAfterDispose != null)
            character.addEffect(effectAfterDispose);
    }

    @Override
    protected void overTimeEffect(AbstractCharacter character, float activeTime) {
        updateLevel(character, activeTime);
        updatePenaltyPercent();
        updatePenaltyValue(character);
        expireIfHasFullness(character);
    }

    private void updateLevel(AbstractCharacter character, float activeTime) {
        levelActiveTime += activeTime;
        float timeToLevelUp = Float.MAX_VALUE;
        switch (level) {
            case 1:
                timeToLevelUp = 5;
                break;
            case 2:
                timeToLevelUp = 10;
                break;
            case 3:
                timeToLevelUp = 15;
                break;
        }
        if (levelActiveTime >= timeToLevelUp) {
            dispose();
            if (level == 3) {
                effectAfterDispose = new Starve();
            } else {
                ++level;
                effectAfterDispose = new Hungry(level);
            }
        }
    }

    private void updatePenaltyPercent() {
        switch (level) {
            case 1:
                strengthPenaltyPercent = 0;
                vitalityPenaltyPercent = 0;
                agilityPenaltyPercent = 0;
                break;
            case 2:
                strengthPenaltyPercent = 0.1f;
                vitalityPenaltyPercent = 0.1f;
                agilityPenaltyPercent = 0.0333f;
                break;
            case 3:
                strengthPenaltyPercent = 0.3f;
                vitalityPenaltyPercent = 0.3f;
                agilityPenaltyPercent = 0.1f;
                break;
        }
    }

    @Override
    public String getName() {
        return "Hungry " + Integer.toString(level);
    }

}
