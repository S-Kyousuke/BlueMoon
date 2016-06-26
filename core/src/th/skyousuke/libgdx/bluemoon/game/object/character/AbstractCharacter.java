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

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import th.skyousuke.libgdx.bluemoon.game.object.AbstractAnimatedObject;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterAttribute.DerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterState.StateType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;
import th.skyousuke.libgdx.bluemoon.utils.Direction;

public abstract class AbstractCharacter extends AbstractAnimatedObject {

    private static final float FRICTION = 500f;

    protected Direction viewDirection;
    protected boolean movable;

    protected CharacterState state;
    protected CharacterAttribute attribute;
    protected Array<AbstractEffect> effects;



    public AbstractCharacter(TextureAtlas atlas) {
        super(atlas);

        attribute = new CharacterAttribute();
        state = new CharacterState(attribute);
        effects = new Array<>();

        viewDirection = Direction.DOWN;
        movable = true;

        friction.set(FRICTION, FRICTION);
    }

    @Override
    public void update(float deltaTime) {

        //Apply any effects on character
        drainFullness(deltaTime);
        for (AbstractEffect effect : effects) {
            effect.apply(deltaTime);
        }

        // Moving Logic
        super.update(deltaTime);
    }

    public void move(Direction direction) {

        // Exit method if can't move.
        if (!movable) return;

        viewDirection = direction;
        float movingSpeed = attribute.getDerived(DerivedAttribute.MOVING_SPEED);
        switch (direction) {
            case LEFT:
                velocity.x = -movingSpeed;
                break;
            case RIGHT:
                velocity.x = movingSpeed;
                break;
            case UP:
                velocity.y = movingSpeed;
                break;
            case DOWN:
                velocity.y = -movingSpeed;
                break;
        }
        velocity.setLength(movingSpeed);
    }


    public void changeState(StateType stateType, float value) {
        state.addValue(stateType, value);
    }

    public void drainFullness(float deltaTime) {
        changeState(StateType.FULLNESS, -attribute.getDerived(DerivedAttribute.FULLNESS_DRAIN) * deltaTime);
    }

    public CharacterAttribute getAttribute() {
        return attribute;
    }

    public CharacterState getState() {
        return state;
    }

    public Direction getViewDirection() {
        return viewDirection;
    }


    @Override
    protected void updateAnimation() {

        switch (viewDirection) {
            case LEFT:
                setAnimation(AnimationKey.WALK_LEFT);
                break;
            case RIGHT:
                setAnimation(AnimationKey.WALK_RIGHT);
                break;
            case UP:
                setAnimation(AnimationKey.WALK_UP);
                break;
            case DOWN:
                setAnimation(AnimationKey.WALK_DOWN);
                break;
        }

    }

}
