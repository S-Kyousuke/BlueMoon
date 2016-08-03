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
 * Language enum.
 * Created by S. Kyousuke <surasek@gmail.com> on 9/8/2559.
 */
public enum Language {
    ENGLISH,
    THAI;

    public static final Language[] values = Language.values();

    public static Language getValue(int ordinal) {
        return values[ordinal];
    }

    public String toString() {
        switch (this) {
            case ENGLISH:
                return I18NManager.instance.getText("english");
            case THAI:
                return I18NManager.instance.getText("thai");
            default:
                return null;
        }
    }
}