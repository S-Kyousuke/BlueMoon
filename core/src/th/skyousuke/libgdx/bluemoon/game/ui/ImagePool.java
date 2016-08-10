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

package th.skyousuke.libgdx.bluemoon.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Scaling;

import th.skyousuke.libgdx.bluemoon.game.ui.ImagePool.PooledImage;

/**
 * A pool of LibGDX image actor that can be reused to avoid allocation.
 * @author S.Kyousuke <surasek@gmail.com>
 */
public class ImagePool extends Pool<PooledImage>  {

    private static final ImagePool pool = new ImagePool();

    private ImagePool() {
    }

    public static PooledImage obtainImage(TextureRegion region) {
        return obtainImage(new TextureRegionDrawable(region));
    }

    public static PooledImage obtainImage(Drawable drawable) {
        final PooledImage image = pool.obtain();
        image.clear();
        image.setDrawable(drawable);
        image.setScaling(Scaling.stretch);
        image.setAlign(Align.center);
        image.setSize(image.getPrefWidth(), image.getPrefHeight());
        return image;
    }

    @Override
    protected PooledImage newObject() {
        return new PooledImage();
    }

    public class PooledImage extends Image {

        public PooledImage() {
        }

        @Override
        public boolean remove() {
            ImagePool.this.free(this);
            return super.remove();
        }
    }
}
