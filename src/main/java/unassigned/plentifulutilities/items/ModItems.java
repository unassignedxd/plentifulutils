package unassigned.plentifulutilities.items;

import net.minecraft.item.Item;
import unassigned.plentifulutilities.items.base.ItemBase;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public final class ModItems {

    public static Item testItem;

    public static void init() {
        testItem = new ItemBase("item_test");
    }
}
