package unassigned.plentifulutilities;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import unassigned.plentifulutilities.items.ModItems;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/24/2018 for plentifulutils
 */
public class PlentifulUtilitiesTab extends CreativeTabs {

    public PlentifulUtilitiesTab(){
        super("plentifulutils");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.item_voidSensor);
    }
}
