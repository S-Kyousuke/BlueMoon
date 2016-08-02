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

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import th.skyousuke.libgdx.bluemoon.framework.Assets;
import th.skyousuke.libgdx.bluemoon.framework.NumberFormat;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatus;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;

/**
 * Status head-up display.
 * Created by S. Kyousuke <surasek@gmail.com> on 1/8/2559.
 */
public class CharacterHud extends Table {

    private static final int BAR_WIDTH = 200;
    private static final int BAR_HEIGHT = 20;

    private static final int EXP_BAR_WIDTH = 75;
    private static final int EXP_BAR_HEIGHT = 5;

    private static final int IMAGE_WIDTH = 75;
    private static final int IMAGE_HEIGHT = 75;

    private Label nameLabel;
    private Image image;
    private StatusBar experienceBar;

    private StatusBar healthBar;
    private StatusBar manaBar;
    private StatusBar staminaBar;
    private StatusBar fullnessBar;

    private AbstractCharacter character;

    public CharacterHud() {
        final Skin skin = Assets.instance.customSkin;
        final Drawable background = skin.getDrawable("uiBackgroundDraw");

        nameLabel = LabelPool.obtainLabel();
        nameLabel.setAlignment(Align.top);
        image = new Image(background);
        experienceBar =  new StatusBar(EXP_BAR_WIDTH, EXP_BAR_HEIGHT, background, skin.getDrawable("emeraldDraw"));

        healthBar = new StatusBar(BAR_WIDTH, BAR_HEIGHT, background, skin.getDrawable("darkRedDraw"));
        manaBar = new StatusBar(BAR_WIDTH, BAR_HEIGHT, background, skin.getDrawable("darkBlueDraw"));
        staminaBar = new StatusBar(BAR_WIDTH, BAR_HEIGHT, background, skin.getDrawable("darkYellowDraw"));
        fullnessBar = new StatusBar(BAR_WIDTH, BAR_HEIGHT, background, skin.getDrawable("darkOrangeDraw"));

        initHud();
    }

    private void initHud() {
        add(createLeftSection());
        add(createRightSection());
        pack();
    }

    private Table createLeftSection() {
        Table leftSection = new Table();
        leftSection.row();
        leftSection.stack(image, nameLabel).size(IMAGE_WIDTH, IMAGE_HEIGHT).row();
        leftSection.add(experienceBar);
        return leftSection;
    }

    private Table createRightSection() {
        Table rightSection = new Table();
        rightSection.row();
        rightSection.add(healthBar).row();
        rightSection.add(manaBar).row();
        rightSection.add(staminaBar).row();
        rightSection.add(fullnessBar);
        return rightSection;
    }

    public void setCharacter(AbstractCharacter character) {
        this.character = character;
        nameLabel.setText(character.getName());
        updateHeathBar(character.getStatus());
        updateManaBar(character.getStatus());
        updateStaminaBar(character.getStatus());
        updateFullness(character.getStatus());
        updateExperience(character.getExperience(), character.getExperienceNeedToReachNextLevel());
    }

    public void updateExperience(float experience, float experienceToReachNextLevel) {
        experienceBar.setBarPercent(experience / experienceToReachNextLevel);
    }

    public void updateStatus(CharacterStatusType statusType) {
        switch (statusType) {
            case HEALTH:
                updateHeathBar(character.getStatus());
                break;
            case MANA:
                updateManaBar(character.getStatus());
                break;
            case STAMINA:
                updateStaminaBar(character.getStatus());
                break;
            case FULLNESS:
                updateFullness(character.getStatus());
                break;
        }
    }

    private void updateHeathBar(CharacterStatus status) {
        final float health = status.getValue(CharacterStatusType.HEALTH);
        final float maxHealth = status.getMaxValue(CharacterStatusType.HEALTH);

        healthBar.setText(NumberFormat.formatFloat(health, 1) + '/' + NumberFormat.formatFloat(maxHealth, 1));
        healthBar.setBarPercent(health / maxHealth);
    }

    private void updateManaBar(CharacterStatus status) {
        final float mana = status.getValue(CharacterStatusType.MANA);
        final float maxMana = status.getMaxValue(CharacterStatusType.MANA);

        manaBar.setText(NumberFormat.formatFloat(mana, 1) + '/' + NumberFormat.formatFloat(maxMana, 1));
        manaBar.setBarPercent(mana / maxMana);
    }

    private void updateStaminaBar(CharacterStatus status) {
        final float stamina = status.getValue(CharacterStatusType.STAMINA);
        final float maxStamina = status.getMaxValue(CharacterStatusType.STAMINA);

        staminaBar.setText(NumberFormat.formatFloat(stamina, 1) + '/' + NumberFormat.formatFloat(maxStamina, 1));
        staminaBar.setBarPercent(stamina / maxStamina);
    }

    private void updateFullness(CharacterStatus status) {
        final float fullness = status.getValue(CharacterStatusType.FULLNESS);
        final float maxFullness = status.getMaxValue(CharacterStatusType.FULLNESS);

        fullnessBar.setText(NumberFormat.formatFloat(fullness, 1) + '/' + NumberFormat.formatFloat(maxFullness, 1));
        fullnessBar.setBarPercent(fullness / maxFullness);
    }

    private static class StatusBar extends Table {

        private Table bars;
        private Label label;

        public StatusBar(float width, float height, Drawable emptyBar, Drawable bar) {
            bars = new Table();
            bars.add(new Image(bar)).height(height);
            bars.add(new Image(emptyBar)).height(height);
            bars.pack();

            label = LabelPool.obtainLabel();
            label.setAlignment(Align.center);

            setSize(width, height);

            stack(bars, label).align(Align.left).size(width, height).expandX();
            setBarPercent(0);
        }

        /** Set bar percentage in faction form. */
        public void setBarPercent(float percent) {
            bars.getCells().first().width(getWidth() * percent);
            bars.getCells().peek().width(getWidth() * (1 - percent));
            bars.invalidate();
        }

        public float getBarPercent() {
            return bars.getCells().first().getPrefWidth() / getWidth();
        }

        public void setText(CharSequence text) {
            label.setText(text);
        }

    }

}
