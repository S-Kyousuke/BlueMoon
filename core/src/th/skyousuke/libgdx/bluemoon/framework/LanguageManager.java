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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Language manager class for localization.
 * Created by S.Kyousuke <surasek@gmail.com> on 15/7/2559.
 */
public class LanguageManager {

    public static final LanguageManager instance = new LanguageManager();

    public Array<LanguageListener> listeners;

    private Language currentLanguage;
    private I18NBundle currentBundle;

    private LanguageManager() {
        listeners = new Array<>();
        setCurrentLanguage(Language.THAI);
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(Language language) {
        if (currentLanguage != language) {
            currentLanguage = language;
            updateCurrentBundle();
            for (LanguageListener listener : listeners) {
                listener.onLanguageChange(currentLanguage);
            }
        }
    }

    public String getText(String key) {
        return currentBundle.get(key);
    }

    public String getFormattedText(String key, Object... args) {
        return currentBundle.format(key, args);
    }

    private void updateCurrentBundle() {
        switch (currentLanguage) {
            case ENGLISH:
                currentBundle = Assets.instance.englishLanguage;
                break;
            case THAI:
            default:
                currentBundle = Assets.instance.thaiLanguage;
        }
    }

    public void addListener(LanguageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LanguageListener listener) {
        listeners.removeValue(listener, true);
    }

}
