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

package th.skyousuke.libgdx.bluemoon.game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.util.Comparator;


public abstract class AbstractAnimatedObject extends AbstractGameObject {

    private final IntMap<Animation> animations;
    private final Array<AtlasRegion> regions;
    private int currentAnimation;
    private float animationTime;
    private boolean freeze;

    protected AbstractAnimatedObject(TextureAtlas atlas) {
        super(atlas.getRegions().first().getRegionWidth(),
                atlas.getRegions().first().getRegionHeight());

        animations = new IntMap<>();
        regions = new Array<>(atlas.getRegions());
        regions.sort(new regionComparator());

        resetAnimation();
        unFreezeAnimation();
    }

    @Override
    public void update(float deltaTime) {
        if (!freeze) animationTime += deltaTime;
        super.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        render(batch, animations.get(currentAnimation).getKeyFrame(animationTime));
    }

    protected void addAnimation(AnimationKey key, int regionStart, int size, PlayMode mode) {
        if (hasKey(key)) {
            Gdx.app.error(getName(), String.format("addAnimation() ERROR: Duplicate Key %s", key.name()));
        }
        Array<AtlasRegion> animationRegions = new Array<>();
        animationRegions.addAll(regions, regionStart, size);
        animations.put(key.ordinal(), new Animation(0, animationRegions, mode));
    }

    protected void freezeAnimation() {
        freeze = true;
    }

    protected void unFreezeAnimation() {
        freeze = false;
    }

    protected void resetAnimation() {
        animationTime = 0.0f;
    }

    public void setAnimation(AnimationKey key, float frameDuration) {
        currentAnimation = key.ordinal();
        animations.get(key.ordinal()).setFrameDuration(frameDuration);
    }

    public boolean isAnimationFinished(AnimationKey key) {
        return animations.get(key.ordinal()).isAnimationFinished(animationTime);
    }

    private boolean hasKey(AnimationKey key) {
        return animations.containsKey(key.ordinal());
    }

    private static class regionComparator implements Comparator<AtlasRegion> {
        @Override
        public int compare(AtlasRegion region1, AtlasRegion region2) {
            return region1.name.compareTo(region2.name);
        }
    }

}
