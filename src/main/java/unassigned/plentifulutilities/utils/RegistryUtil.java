package unassigned.plentifulutilities.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.utils.registry.RegistryHandler;
import unassigned.plentifulutilities.blocks.base.ItemBlockBase;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class RegistryUtil {

    public static void registerBlock(Block block, ItemBlockBase itemBlock, String name) {
        block.setRegistryName(ModUtil.MODID, name);
        block.setUnlocalizedName(name);
        RegistryHandler.BLOCKS_LIST.add(block);

        itemBlock.setRegistryName(block.getRegistryName());
        itemBlock.setUnlocalizedName(block.getUnlocalizedName());
        itemBlock.setCreativeTab(PlentifulUtilities.creativeTab);
        RegistryHandler.ITEM_LIST.add(itemBlock);
    }

    public static void registerItem(Item item, String name){
        item.setRegistryName(ModUtil.MODID, name);
        item.setUnlocalizedName(name);
        item.setCreativeTab(PlentifulUtilities.creativeTab);
        RegistryHandler.ITEM_LIST.add(item);
    }
}
