/****************************************************************
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ****************************************************************/

package th.skyousuke.libgdx.bluemoon.game.object.character.players;

import com.badlogic.gdx.graphics.g2d.Animation;

import th.skyousuke.libgdx.bluemoon.game.Assets;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractPlayer;

public class Player1 extends AbstractPlayer {

    public Player1() {
        super(Assets.instance.player1Altas);

        addAnimation(AnimationKey.WALK_DOWN, 0.25f, 0, 4, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_UP, 0.25f, 4, 4, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_LEFT, 0.25f, 8, 4, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_RIGHT, 0.25f, 12, 4, Animation.PlayMode.LOOP);
    }

    @Override
    public void interact() {

    }
}
