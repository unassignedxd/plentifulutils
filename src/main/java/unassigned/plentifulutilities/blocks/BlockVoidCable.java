package unassigned.plentifulutilities.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unassigned.plentifulutilities.blocks.base.BlockTEBase;
import unassigned.plentifulutilities.tile.TileEntityVoidCable;

import javax.annotation.Nullable;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/25/2018 for plentifulutils
 */
public class BlockVoidCable extends BlockTEBase {

    public BlockVoidCable(String name){
        super(Material.CLOTH, name);
        this.setHardness(1f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityVoidCable();
    }
}
