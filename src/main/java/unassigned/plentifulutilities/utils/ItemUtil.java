package unassigned.plentifulutilities.utils;

import net.minecraft.item.ItemStack;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public final class ItemUtil {

    //Majority of this class is just a bunch of random functions to do with itemstacks - sorting, checking if they are valid, etc.
    //Purpose of this class is to reduce repetitive-ness throughout the mod.

    public static boolean isValid(ItemStack stack) { return !stack.isEmpty(); }
    public static ItemStack getEmpty() { return ItemStack.EMPTY; }

}
