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

package th.skyousuke.libgdx.bluemoon.game.object.character.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import th.skyousuke.libgdx.bluemoon.game.object.AnimationKey;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterState;
import th.skyousuke.libgdx.bluemoon.utils.Direction;

/**
 * Created by Skyousuke <surasek@gmail.com> on 27/6/2559.
 */
public class RunningState extends CharacterState {

    private float[] bonusDerivedAttribute;

    @Override
    public void enter(AbstractCharacter character) {
        bonusDerivedAttribute = new float[CharacterDerivedAttribute.values().length];
        bonusDerivedAttribute[CharacterDerivedAttribute.MOVING_SPEED.ordinal()] =
                character.getAttribute().getDerived(CharacterDerivedAttribute.MOVING_SPEED);
        character.getAttribute().addAdditionalDerived(bonusDerivedAttribute);
    }

    @Override
    public void exit(AbstractCharacter character) {
        character.getAttribute().removeAdditionalDerived(bonusDerivedAttribute);
    }

    @Override
    public void handleInput(AbstractCharacter character) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) character.move(Direction.UP);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) character.move(Direction.DOWN);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) character.move(Direction.LEFT);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) character.move(Direction.RIGHT);
        if (!Gdx.input.isKeyPressed(Input.Keys.Z)) character.setState(new WalkingState());
        if (Gdx.input.isKeyPressed(Input.Keys.X)) character.interact();
        if (Gdx.input.isKeyPressed(Input.Keys.C)) character.attack();
    }

    @Override
    protected void updateCharacter(AbstractCharacter character) {

    }

    @Override
    protected void setAnimation(AbstractCharacter character) {
        float runTimeFactor = 12.5f;
        float runTime = runTimeFactor / character.getAttribute().getDerived(CharacterDerivedAttribute.MOVING_SPEED);

        switch (character.getViewDirection()) {
            case LEFT:
                character.setAnimation(AnimationKey.WALK_LEFT, runTime);
                break;
            case RIGHT:
                character.setAnimation(AnimationKey.WALK_RIGHT, runTime);
                break;
            case UP:
                character.setAnimation(AnimationKey.WALK_UP, runTime);
                break;
            case DOWN:
                character.setAnimation(AnimationKey.WALK_DOWN, runTime);
                break;
        }

    }
}
