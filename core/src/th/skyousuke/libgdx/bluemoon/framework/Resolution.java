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

package th.skyousuke.libgdx.bluemoon.framework;

/**
 * Resolution enum.
 * @author S.Kyousuke <surasek@gmail.com>.
 */
public enum Resolution {
    FULLSCREEN,
    GAME_WORD,
    HD,
    FULL_HD;

    private static final Resolution[] values = Resolution.values();

    public static Resolution getValue(int ordinal) {
        return values[ordinal];
    }

    @Override
    public String toString() {
        switch (this) {
            case FULLSCREEN:
                return I18NManager.instance.getText("fullscreen");
            case GAME_WORD:
                return "1024x576";
            case HD:
                return "1280x720";
            case FULL_HD:
                return "1920x1080";
            default:
                return null;
        }
    }
}