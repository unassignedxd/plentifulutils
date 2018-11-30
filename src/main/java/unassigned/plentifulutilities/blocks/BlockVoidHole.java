package unassigned.plentifulutilities.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import scala.Int;
import unassigned.plentifulutilities.blocks.base.BlockTEBase;
import unassigned.plentifulutilities.tile.TileEntityVoidHole;

import javax.annotation.Nullable;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentifulutils/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/29/2018 for plentifulutils
 */
public class BlockVoidHole extends BlockTEBase {

    public BlockVoidHole(String name){
        super(Material.PORTAL, name);
        this.setBlockUnbreakable();
        this.setResistance(Int.MaxValue());
        this.setLightOpacity(0);
        this.setLightLevel(12f);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityVoidHole();
    }
}
