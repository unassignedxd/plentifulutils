package unassigned.plentifulutilities.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import unassigned.plentifulutilities.blocks.base.BlockBase;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class ModBlocks {

    public static Block testBlock;

    public static void init(){
        testBlock = new BlockBase(Material.ROCK,"block_test");
    }
}
