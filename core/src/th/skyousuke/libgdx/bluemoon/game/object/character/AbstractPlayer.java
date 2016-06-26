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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import th.skyousuke.libgdx.bluemoon.utils.Direction;

public abstract class AbstractPlayer extends AbstractCharacter {

    public AbstractPlayer(TextureAtlas atlas) {
        super(atlas);
    }

    public abstract void interact();

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) move(Direction.UP);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) move(Direction.DOWN);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) move(Direction.LEFT);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) move(Direction.RIGHT);
        //if (Gdx.input.isKeyPressed(Input.Keys.Z));
        //if (Gdx.input.isKeyPressed(Input.Keys.X));
        //if (Gdx.input.isKeyPressed(Input.Keys.C));
    }

}
