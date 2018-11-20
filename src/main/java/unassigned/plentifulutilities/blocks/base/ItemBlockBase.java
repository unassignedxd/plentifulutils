package unassigned.plentifulutilities.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class ItemBlockBase extends ItemBlock {

    public ItemBlockBase(Block block) {
        super(block);
//      prevents weird interactions between other ItemBlocks.
        this.setHasSubtypes(false);
        this.setMaxDamage(0);
    }
}
