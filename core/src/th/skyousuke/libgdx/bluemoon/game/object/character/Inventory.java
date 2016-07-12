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

import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

import th.skyousuke.libgdx.bluemoon.game.object.item.AbstractItem;
import th.skyousuke.libgdx.bluemoon.game.object.item.equipment.AbstractEquipment;
import th.skyousuke.libgdx.bluemoon.game.object.item.equipment.EquipmentType;

/**
 * Game Inventory Class (Collection of items your character is currently carrying or equipping)
 * Created by Skyousuke <surasek@gmail.com> on 4/7/2559.
 */
public class Inventory {

    private final Array<AbstractItem> items;
    private final EnumMap<EquipmentType, AbstractEquipment> equipments;

    private int capacity;

    public Inventory() {
        items = new Array<>();
        equipments = new EnumMap<>(EquipmentType.class);

        capacity = 12;
    }

    public void addItem(AbstractItem item) {
        items.add(item);
    }

    public void removeItem(AbstractItem item) {
        items.removeValue(item, true);
    }

    public void equip(AbstractEquipment equipment) {
        if (!items.contains(equipment, true))
            return;
        else removeItem(equipment);
        final AbstractEquipment previousEquipment = equipments.remove(equipment.getType());
        if (previousEquipment != null)
            addItem(previousEquipment);
        equipments.put(equipment.getType(), equipment);
    }

    public void unequip(AbstractEquipment equipment) {
        if (equipments.remove(equipment.getType()) != null)
            addItem(equipment);
    }

    public int getCapacity() {
        return capacity;
    }

}
