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

package th.skyousuke.libgdx.bluemoon.game.object;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import th.skyousuke.libgdx.bluemoon.game.GameLocation;

/**
 * Steering agent class for testing Steering Behaviors ai system.
 * Created by Skyousuke <surasek@gmail.com> on 19/7/2559.
 */
public abstract class AbstractSteeringAnimatedObject extends AbstractAnimatedObject implements Steerable<Vector2> {

    private static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<>(new Vector2());

    private boolean tagged;

    private float maxLinearSpeed = 100;
    private float maxLinearAcceleration = 200;

    private float maxAngularSpeed = 5;
    private float maxAngularAcceleration = 10;

    private boolean independentFacing;

    private SteeringBehavior<Vector2> steeringBehavior;

    public AbstractSteeringAnimatedObject(TextureAtlas atlas, boolean independentFacing) {
        super(atlas);
        this.independentFacing = independentFacing;

    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return rotation * MathUtils.degreesToRadians;
    }

    @Override
    public void setOrientation(float orientation) {
        rotation = orientation * MathUtils.radiansToDegrees;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return (getBounds().width + getBounds().height) / 4f;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new GameLocation();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return maxLinearSpeed / 10;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        throw new UnsupportedOperationException();
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
        setApplyFriction(false);
    }

    public void removeSteeringBehavior() {
        steeringBehavior = null;
        setApplyFriction(true);
    }

    protected void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        linearVelocity.mulAdd(steering.linear, time).limit(getMaxLinearSpeed());

        if (independentFacing) {
            rotation += (angularVelocity * time) * MathUtils.radiansToDegrees;
            angularVelocity += steering.angular * time;
        } else {
            if (isMoving()) {
                float newOrientation = vectorToAngle(linearVelocity);
                angularVelocity = (newOrientation - rotation * MathUtils.degreesToRadians) * time;
                rotation = newOrientation * MathUtils.radiansToDegrees;
            }
        }
    }

    public boolean isMoving() {
        return !linearVelocity.isZero(getZeroLinearSpeedThreshold());
    }

    @Override
    public void update(float deltaTime) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, deltaTime);
        }
        super.update(deltaTime);
    }

}
