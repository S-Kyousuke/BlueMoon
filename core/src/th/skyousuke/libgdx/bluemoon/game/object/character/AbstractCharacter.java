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

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import th.skyousuke.libgdx.bluemoon.game.object.AbstractAnimatedObject;
import th.skyousuke.libgdx.bluemoon.game.object.AnimationKey;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;
import th.skyousuke.libgdx.bluemoon.game.object.character.states.AttackingState;
import th.skyousuke.libgdx.bluemoon.game.object.character.states.IdlingState;
import th.skyousuke.libgdx.bluemoon.utils.Direction;

public abstract class AbstractCharacter extends AbstractAnimatedObject {


    private static final float FRICTION = 500f;

    private final Array<AbstractEffect> effects;
    private CharacterState state;
    private Direction viewDirection;
    private boolean movable;
    private CharacterStatus status;
    private CharacterAttribute attribute;

    protected AbstractCharacter(TextureAtlas atlas) {
        super(atlas);

        attribute = new CharacterAttribute();
        status = new CharacterStatus(attribute);
        effects = new Array<>();

        setState(new IdlingState(this));
        viewDirection = Direction.DOWN;
        movable = true;

        friction.set(FRICTION, FRICTION);

        addAnimation(AnimationKey.IDLE_LEFT, 0, 1, PlayMode.LOOP);
        addAnimation(AnimationKey.IDLE_RIGHT, 1, 1, PlayMode.LOOP);
        addAnimation(AnimationKey.IDLE_UP, 2, 1, PlayMode.LOOP);
        addAnimation(AnimationKey.IDLE_DOWN, 3, 1, PlayMode.LOOP);

        addAnimation(AnimationKey.WALK_DOWN, 4, 4, PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_UP, 8, 4, PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_LEFT, 12, 4, PlayMode.LOOP);
        addAnimation(AnimationKey.WALK_RIGHT, 16, 4, PlayMode.LOOP);

        addAnimation(AnimationKey.ATK_DOWN, 20, 4, PlayMode.NORMAL);
        addAnimation(AnimationKey.ATK_UP, 24, 4, PlayMode.NORMAL);
        addAnimation(AnimationKey.ATK_LEFT, 28, 4, PlayMode.NORMAL);
        addAnimation(AnimationKey.ATK_RIGHT, 32, 4, PlayMode.NORMAL);
    }

    @Override
    public void update(float deltaTime) {
        //Apply any effects on character
        drainFullness(deltaTime);
        Iterator<AbstractEffect> effectsIterator = effects.iterator();
        while (effectsIterator.hasNext()) {
            AbstractEffect effect = effectsIterator.next();
            if (effect.isExpire()) {
                effect.exit(this);
                effectsIterator.remove();
            }
            else effect.apply(this, deltaTime);
        }
        state.update(deltaTime);

        // Moving
        super.update(deltaTime);
    }

    public void handleInput() {
        state.handleInput();
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

    public void changeStatus(CharacterStatusType statusType, float value) {
        status.addValue(statusType, value);
    }

    public void drainFullness(float deltaTime) {
        changeStatus(CharacterStatusType.FULLNESS, -attribute.getDerived(CharacterDerivedAttribute.FULLNESS_DRAIN) *
                deltaTime);
    }

    public CharacterAttribute getAttribute() {
        return attribute;
    }

    public float getStatus(CharacterStatusType type) {
        return status.getStatus(type);
    }

    public Direction getViewDirection() {
        return viewDirection;
    }

    public boolean isMoving() {
        return !velocity.isZero();
    }

    public void setState(CharacterState state) {
        if (this.state != null) this.state.exit();
        this.state = state;
        this.state.enter();
        resetAnimation();
    }

    public void attack() {
        setState(new AttackingState(this));
    }

    public abstract void interact();

    public void addEffect(AbstractEffect effect) {
        effects.add(effect);
        effect.enter(this);
    }

    public void removeEffect(AbstractEffect effect) {
        effect.exit(this);
        effects.removeValue(effect, true);
    }

    public boolean hasEffect(AbstractEffect effect) {
        return effects.contains(effect, true);
    }

    public Array<AbstractEffect> getEffects() {
        return effects;
    }

}
