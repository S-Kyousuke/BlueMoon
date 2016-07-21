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

package th.skyousuke.libgdx.bluemoon.game.object.character.monsters;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractMonster;

public class Slime extends AbstractMonster {

    public Slime() {
        super(Assets.instance.monster1Altas);
    }

    @Override
    public String getName() {
        return LanguageManager.instance.getText("slime");
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

}
