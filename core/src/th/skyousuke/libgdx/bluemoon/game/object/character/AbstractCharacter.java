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

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import th.skyousuke.libgdx.bluemoon.game.object.AbstractAnimatedObject;
import th.skyousuke.libgdx.bluemoon.game.object.AnimationKey;
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

        addAnimation(AnimationKey.IDLE_LEFT, 0, 0, 1, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.IDLE_RIGHT, 0, 1, 1, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.IDLE_UP, 0, 2, 1, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.IDLE_DOWN, 0, 3, 1, Animation.PlayMode.LOOP);

        addAnimation(AnimationKey.WALK_DOWN, 0.25f, 4, 4, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_UP, 0.25f, 8, 4, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_LEFT, 0.25f, 12, 4, Animation.PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_RIGHT, 0.25f, 16, 4, Animation.PlayMode.LOOP);
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
        float movingSpeed = attribute.getDerived(CharacterDerivedAttribute.MOVING_SPEED);
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


    public void changeState(CharacterStateType stateType, float value) {
        state.addValue(stateType, value);
    }

    public void drainFullness(float deltaTime) {
        changeState(CharacterStateType.FULLNESS, -attribute.getDerived(CharacterDerivedAttribute.FULLNESS_DRAIN) *
                deltaTime);
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
        boolean moving = !velocity.isZero();
        if (moving) {
            walkAnimation();
        } else {
            idleAnimation();
        }
    }

    private void idleAnimation() {
        switch (viewDirection) {
            case LEFT:
                setAnimation(AnimationKey.IDLE_LEFT);
                break;
            case RIGHT:
                setAnimation(AnimationKey.IDLE_RIGHT);
                break;
            case UP:
                setAnimation(AnimationKey.IDLE_UP);
                break;
            case DOWN:
                setAnimation(AnimationKey.IDLE_DOWN);
                break;
        }
    }

    private void walkAnimation() {
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
