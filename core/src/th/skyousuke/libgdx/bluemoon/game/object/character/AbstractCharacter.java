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

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import th.skyousuke.libgdx.bluemoon.framework.Direction;
import th.skyousuke.libgdx.bluemoon.game.object.AbstractAnimatedObject;
import th.skyousuke.libgdx.bluemoon.game.object.AnimationKey;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.buffs.Full;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.debuffs.Hungry;
import th.skyousuke.libgdx.bluemoon.game.object.character.states.AttackingState;
import th.skyousuke.libgdx.bluemoon.game.object.character.states.IdlingState;

public abstract class AbstractCharacter extends AbstractAnimatedObject implements AttributeAndStatusListener {

    private static final float FRICTION = 1000f;

    private CharacterState state;
    private Direction viewDirection;
    private boolean movable;

    private CharacterStatus status;
    private CharacterAttribute attribute;
    private CharacterEffect effect;
    private Inventory inventory;

    protected AbstractCharacter(TextureAtlas atlas) {
        super(atlas);

        attribute = new CharacterAttribute();
        status = new CharacterStatus(attribute);
        effect = new CharacterEffect(this);
        inventory = new Inventory();

        status.addListener(this);
        status.setToMax();

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
        updateEffectsAndStatus(deltaTime);
        state.update(deltaTime);
        super.update(deltaTime);
    }

    private void updateEffectsAndStatus(float deltaTime) {
        effect.apply(deltaTime);
        status.addValue(CharacterStatusType.HEALTH,
                attribute.getDerived(CharacterDerivedAttribute.HEALTH_REGENERATION) * deltaTime * 0.1f);
        status.addValue(CharacterStatusType.MANA,
                attribute.getDerived(CharacterDerivedAttribute.MANA_REGENERATION) * deltaTime * 0.1f);
        status.addValue(CharacterStatusType.FULLNESS,
                -attribute.getDerived(CharacterDerivedAttribute.FULLNESS_DRAIN) * deltaTime * 0.1f);
    }

    public void handleInput() {
        state.handleInput();
    }

    public void move(Direction direction) {
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

    public Direction viewDirection() {
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

    }

}
