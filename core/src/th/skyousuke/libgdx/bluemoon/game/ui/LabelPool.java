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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Pool;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.game.ui.LabelPool.PooledLabel;

/**
 * A pool of custom skin LibGDX text label that can be reused to avoid allocation.
 * Created by Skyousuke <surasek@gmail.com> on 12/7/2559.
 */
public class LabelPool extends Pool<PooledLabel> {

    private static final LabelPool pool = new LabelPool();
    private static final LabelStyle labelStyle = Assets.instance.customSkin.get(LabelStyle.class);

    private LabelPool() {
    }

    public static PooledLabel obtainLabel() {
        return obtainLabel("");
    }

    public static PooledLabel obtainLabel(CharSequence text) {
        return pool.obtain(text);
    }

    @Override
    protected PooledLabel newObject() {
        return new PooledLabel(labelStyle);
    }

    public PooledLabel obtain(CharSequence text) {
        PooledLabel label = super.obtain();
        if (label.getStyle() != labelStyle) {
            Gdx.app.log("", "พบการเปลี่ยน Style");
            label.setStyle(labelStyle);
        }
        label.setPosition(0, 0);
        label.setText(text);
        return label;
    }

    public class PooledLabel extends Label {

        public PooledLabel(LabelStyle style) {
            super("", style);
        }

        @Override
        public boolean remove() {
            LabelPool.this.free(this);
            return super.remove();
        }

    }

}
