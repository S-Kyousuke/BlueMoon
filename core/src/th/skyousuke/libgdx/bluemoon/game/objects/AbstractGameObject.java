/****************************************************************
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
 ****************************************************************/

package th.skyousuke.libgdx.bluemoon.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import th.skyousuke.libgdx.bluemoon.utils.collisions.CollisionCheck;
import th.skyousuke.libgdx.bluemoon.utils.collisions.NullCollsionCheck;

public abstract class AbstractGameObject {

    protected Vector2 scale;
    protected CollisionCheck collisionCheck;

    private Vector2 position;
    private Vector2 dimension;
    private Vector2 origin;
    private Rectangle bounds;
    private float rotation;
    private Vector2 velocity;
    private Vector2 friction;
    private Vector2 acceleration;

    public AbstractGameObject (float width, float height) {
        position = new Vector2();
        dimension = new Vector2();
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;

        velocity = new Vector2();
        friction = new Vector2();
        acceleration = new Vector2();

        collisionCheck = new NullCollsionCheck();

        if (width == 0 || height == 0)
            throw new IllegalArgumentException("Invaild object dimension");
        setDimension(width, height);
    }

    public void update (float deltaTime) {
        float oldPositionX = position.x;
        float oldPositionY = position.y;

        updateMotionX(deltaTime);
        updateMotionY(deltaTime);

        setPositionX(position.x + velocity.x * deltaTime);
        if (collisionCheck.isCollidesLeft() || collisionCheck.isCollidesRight()) {
            responseCollisionX(oldPositionX);
        }

        setPositionY(position.y + velocity.y * deltaTime);
        if (collisionCheck.isCollidesTop() || collisionCheck.isCollidesBottom()) {
            responseCollisionY(oldPositionY);
        }
    }

    //public abstract void render (SpriteBatch batch);

    protected void responseCollisionX (float oldPositionX) {
        setPositionX(oldPositionX);
    }

    protected void responseCollisionY (float oldPositionY) {
        setPositionY(oldPositionY);
    }

    protected void render(SpriteBatch batch, TextureRegion region) {
        batch.draw(region,
                position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y,
                1.0f, 1.0f, rotation);
    }

    protected void updateMotionX (float deltaTime) {
        if (velocity.x != 0) {
            if (velocity.x > 0) {
                velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
            }
            else
            {
                velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
            }
        }
        velocity.x += acceleration.x * deltaTime;
    }

    protected void updateMotionY (float deltaTime) {
        if (velocity.y != 0) {
            if (velocity.y > 0)
                velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
            else
                velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);

        }
        velocity.y += acceleration.y * deltaTime;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.set(position.x, position.y, dimension.x, dimension.y);
    }
    public void setPositionX(float x) {
        setPosition(x, position.y);
    }

    public void setPositionY(float y) {
        setPosition(position.x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    protected void setDimension(float width, float height) {
        dimension.x =  width * scale.x;
        dimension.y =  height * scale.y;

        origin.set(dimension.x / 2, dimension.y / 2);
    }

}
