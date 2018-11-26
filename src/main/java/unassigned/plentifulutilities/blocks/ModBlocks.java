package unassigned.plentifulutilities.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import unassigned.plentifulutilities.blocks.base.BlockBase;
import unassigned.plentifulutilities.utils.ModUtil;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
@GameRegistry.ObjectHolder(ModUtil.MODID)
public class ModBlocks {

    public static Block testBlock;
    public static Block voidAccumulator;
    public static Block voidCable;

    public static void init(){
        testBlock = new BlockBase(Material.ROCK,"block_test");
        voidAccumulator = new BlockVoidAccumulator("block_void_accumulator");
        voidCable = new BlockVoidCable("block_void_cable");
    }
}
