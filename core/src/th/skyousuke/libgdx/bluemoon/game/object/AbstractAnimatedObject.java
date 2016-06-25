/****************************************************************
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ****************************************************************/

package th.skyousuke.libgdx.bluemoon.game.object;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import java.util.Comparator;

public abstract class AbstractAnimatedObject extends AbstractGameObject {

    private IntMap<Animation> animations;
    private int currentAnimation;

    private Array<AtlasRegion> regions;
    private TextureRegion currentRegion;

    private float animationTime;
    private boolean freeze;

    public AbstractAnimatedObject(TextureAtlas atlas) {
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
        setAnimation();
        updateKeyFrame(deltaTime);
        super.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        render(batch, currentRegion);
    }

    protected void addAnimation(int key, float frameDuration, int regionStart, int size, PlayMode mode) {
        Array<AtlasRegion> animationRegions = new Array<>();
        animationRegions.addAll(regions, regionStart, size);
        animations.put(key, new Animation(frameDuration, animationRegions, mode));
    }

    protected abstract void setAnimation();

    protected void freezeAnimation() {
        freeze = true;
    }

    protected void unFreezeAnimation() {
        freeze = false;
    }

    protected void resetAnimation() {
        animationTime = 0.0f;
    }

    public void setCurrentAnimation(int key) {
        resetAnimation();
        currentAnimation = key;
    }

    public boolean isAnimationFinished(int key) {
        return animations.get(key).isAnimationFinished(animationTime);
    }

    protected void updateKeyFrame(float deltaTime) {
        if (!freeze) animationTime += deltaTime;
        currentRegion = animations.get(currentAnimation).getKeyFrame(animationTime);
    }

    private static class regionComparator implements Comparator<AtlasRegion> {
        @Override
        public int compare(AtlasRegion region1, AtlasRegion region2) {
            return region1.name.compareTo(region2.name);
        }
    }

}
