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

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import th.skyousuke.libgdx.bluemoon.framework.Direction;
import th.skyousuke.libgdx.bluemoon.game.object.AbstractSteeringAnimatedObject;
import th.skyousuke.libgdx.bluemoon.game.object.AnimationKey;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.buffs.Full;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.debuffs.Hungry;
import th.skyousuke.libgdx.bluemoon.game.object.character.states.AttackingState;
import th.skyousuke.libgdx.bluemoon.game.object.character.states.IdlingState;

public abstract class AbstractCharacter extends AbstractSteeringAnimatedObject implements AttributeAndStatusListener {

    private static final float FRICTION = 1000f;

    private CharacterState state;
    private Direction viewDirection;

    private CharacterStatus status;
    private CharacterAttribute attribute;
    private CharacterEffect effect;
    private Inventory inventory;

    protected AbstractCharacter(TextureAtlas atlas) {
        super(atlas);
        setIndependentFacing(true);

        attribute = new CharacterAttribute();
        status = new CharacterStatus(attribute);
        effect = new CharacterEffect(this);
        inventory = new Inventory();

        status.addListener(this);
        status.setToMax();
        attribute.addListener(this);

        setState(new IdlingState(this));
        viewDirection = Direction.DOWN;
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
        updateEffectsAndStatus(deltaTime);
        state.update(deltaTime);
        super.update(deltaTime);
    }

    private void updateEffectsAndStatus(float deltaTime) {
        effect.apply(deltaTime);
        status.addValue(CharacterStatusType.HEALTH,
                attribute.getDerived(CharacterDerivedAttribute.HEALTH_REGEN) * deltaTime * 0.1f);
        status.addValue(CharacterStatusType.MANA,
                attribute.getDerived(CharacterDerivedAttribute.MANA_REGEN) * deltaTime * 0.1f);
        status.addValue(CharacterStatusType.FULLNESS,
                -attribute.getDerived(CharacterDerivedAttribute.FULLNESS_DRAIN) * deltaTime * 0.1f);
    }

    public void handleInput() {
        state.handleInput();
    }

    public void move(Direction direction) {
        float movingSpeed = getMaxLinearSpeed();
        viewDirection = direction;
        switch (direction) {
            case LEFT:
                this.linearVelocity.x = -movingSpeed;
                break;
            case RIGHT:
                this.linearVelocity.x = movingSpeed;
                break;
            case UP:
                this.linearVelocity.y = movingSpeed;
                break;
            case DOWN:
                this.linearVelocity.y = -movingSpeed;
                break;
        }
        this.linearVelocity.limit(movingSpeed);
    }

    public CharacterStatus getStatus() {
        return status;
    }

    public CharacterAttribute getAttribute() {
        return attribute;
    }

    public CharacterEffect getEffect() {
        return effect;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Direction getViewDirection() {
        return viewDirection;
    }

    public boolean isMoving() {
        return (linearVelocity.len() > getMaxLinearSpeed()/5);
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

    @Override
    public void onStatusChange(CharacterStatusType statusType, float oldValue, float newValue) {
        switch (statusType) {
            case FULLNESS:
                if (oldValue > 0 && newValue == 0)
                    effect.add(new Hungry(1));
                if (oldValue < 40 && newValue >= 40) {
                    effect.add(new Full());
                }
                break;
        }
    }

    @Override
    public void onMaxStatusChange(CharacterStatusType statusType, float oldValue, float newValue) {

    }

    @Override
    public void onPrimaryAttributeChange(CharacterPrimaryAttribute primaryAttribute, int oldValue, int newValue) {

    }

    @Override
    public void onDerivedAttributeChange(CharacterDerivedAttribute derivedAttribute, float oldValue, float newValue) {
        if (derivedAttribute == CharacterDerivedAttribute.MOVING_SPEED) {
            setMaxLinearSpeed(newValue);
            setMaxLinearAcceleration(newValue * 2);
        }
    }

    @Override
    protected void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        super.applySteering(steering, time);
        if (isMoving()) {
            final float angle = getLinearVelocity().angle();
            if (angle > 225 && angle < 315) viewDirection = Direction.DOWN;
            else if (angle > 135) viewDirection = Direction.LEFT;
            else if (angle > 45) viewDirection = Direction.UP;
            else viewDirection = Direction.RIGHT;
        }
    }

}
