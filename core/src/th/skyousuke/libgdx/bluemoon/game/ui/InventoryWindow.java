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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import th.skyousuke.libgdx.bluemoon.framework.LanguageManager;
import th.skyousuke.libgdx.bluemoon.game.object.character.AbstractCharacter;
import th.skyousuke.libgdx.bluemoon.game.object.character.Inventory;

/**
 * Character inventory window class.
 * Created by S.Kyousuke <surasek@gmail.com> on 6/7/2559.
 */
public class InventoryWindow extends Window {

    private static final int ITEMS_PER_ROW = 4;
    private final InventoryWindowStyle style;
    private Inventory inventory;
    //private Array<Image> itemSlots;

    public InventoryWindow(Skin skin) {
        super(skin);
        style = skin.get(InventoryWindowStyle.class);
        //itemSlots = new Array<>();

        padLeft(10f);
        padRight(10f);
        padBottom(10f);
    }

    public void setCharacter(AbstractCharacter character) {
        inventory = character.getInventory();
        update();
        setTitle(LanguageManager.instance.getFormattedText("inventoryWindowTitle", character.getName()));
    }

    public void initContent() {
    }

    private void update() {
        int slotCount = 0;
        clearChildren();
        if (inventory != null) {
            while (slotCount < inventory.getCapacity()) {
                final boolean firstColumn = (slotCount % ITEMS_PER_ROW == 0);
                if (firstColumn) {
                    row().padTop(10f);
                    add(new ItemSlot(style.itemSlot));
                } else {
                    add(new ItemSlot(style.itemSlot)).padLeft(10f);
                }
                ++slotCount;
            }
        }
        pack();
    }

    public static class InventoryWindowStyle {
        public Drawable itemSlot;
    }

    private class ItemSlot extends Image {

        public static final int SIZE = 36;

        public ItemSlot(Drawable drawable) {
            super(drawable);
            drawable.setMinHeight(SIZE);
            drawable.setMinWidth(SIZE);
            setSize(SIZE, SIZE);
        }

    }

}
