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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Monster base class.
 * Created by Skyousuke <surasek@gmail.com> on 27/6/2559.
 */
public class AbstractMonster extends AbstractCharacter implements Telegraph {

    public AbstractMonster(TextureAtlas atlas) {
        super(atlas);
    }

    @Override
    public void interact() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case MessageType.WALK_UP:
                break;
            case MessageType.WALK_DOWN:
                break;
            case MessageType.WALK_LEFT:
                break;
            case MessageType.WALK_RIGHT:
                break;
            case MessageType.SAY_MSG:
                Gdx.app.log("Monster say", "Hello!");
                return true;
        }
        return false;
    }

}
