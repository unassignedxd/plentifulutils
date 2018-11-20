package unassigned.plentifulutilities.utils.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import unassigned.plentifulutilities.blocks.ModBlocks;
import unassigned.plentifulutilities.items.ModItems;
import java.util.ArrayList;
import java.util.List;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class RegistryHandler {

    public static final List<Block> BLOCKS_LIST = new ArrayList<>();
    public static final List<Item> ITEM_LIST = new ArrayList<>();
    public static final List<IRecipe> RECIPE_LIST = new ArrayList<>();

    @SubscribeEvent
    public void onItemRegistry(RegistryEvent.Register<Item> event) {
        ModItems.init();

        for (Item item : ITEM_LIST) {
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public void onBlockRegistry(RegistryEvent.Register<Block> event) {
        ModBlocks.init();

        for (Block block : BLOCKS_LIST) {
            event.getRegistry().register(block);
        }
    }
}
