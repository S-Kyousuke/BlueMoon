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

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Monster base class.
 * Created by S.Kyousuke <surasek@gmail.com> on 27/6/2559.
 */
public abstract class AbstractMonster extends AbstractCharacter implements Telegraph {

    private final Arrive<Vector2> arriveSteeringBehavior;

    public AbstractMonster(TextureAtlas atlas) {
        super(atlas);
        arriveSteeringBehavior = new Arrive<>(this)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0.001f)
                .setDecelerationRadius(30);
    }

    @Override
    public void interact() {

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case MessageType.WALK_UP:
                break;
            case MessageType.WALK_DOWN:
                break;
            case MessageType.WALK_LEFT:
                break;
            case MessageType.WALK_RIGHT:
                break;
            case MessageType.SAY_MSG:
                return true;
        }
        return false;
    }

    public void follow(AbstractCharacter character) {
        arriveSteeringBehavior.setTarget(character);
        setSteeringBehavior(arriveSteeringBehavior);
    }
}
