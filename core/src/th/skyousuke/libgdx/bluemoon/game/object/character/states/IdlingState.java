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

package th.skyousuke.libgdx.bluemoon.game.object.character.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import th.skyousuke.libgdx.bluemoon.framework.Direction;
import th.skyousuke.libgdx.bluemoon.game.object.AnimationKey;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterState;

/**
 * Character idling state
 * Created by Skyousuke <surasek@gmail.com> on 27/6/2559.
 */
public class IdlingState extends CharacterState {

    public IdlingState(AbstractCharacter character) {
        super(character);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) character.move(Direction.UP);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) character.move(Direction.DOWN);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) character.move(Direction.LEFT);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) character.move(Direction.RIGHT);
        if (Gdx.input.isKeyPressed(Input.Keys.X)) character.interact();
        if (Gdx.input.isKeyPressed(Input.Keys.C)) character.attack();
    }

    @Override
    public void updateCharacter(float deltaTime) {
        if (character.isMoving()) character.setState(new MovingState(character));
    }

    @Override
    public void setAnimation() {
        switch (character.viewDirection()) {
            case LEFT:
                character.setAnimation(AnimationKey.IDLE_LEFT, 0);
                break;
            case RIGHT:
                character.setAnimation(AnimationKey.IDLE_RIGHT, 0);
                break;
            case UP:
                character.setAnimation(AnimationKey.IDLE_UP, 0);
                break;
            case DOWN:
                character.setAnimation(AnimationKey.IDLE_DOWN, 0);
                break;
        }
    }

}
