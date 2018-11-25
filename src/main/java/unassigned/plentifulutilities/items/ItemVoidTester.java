package unassigned.plentifulutilities.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import unassigned.plentifulutilities.items.base.ItemBase;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidStorage;
import unassigned.plentifulutilities.voidenergy.capability.CapabilityVoidEnergy;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/24/2018 for plentifulutils
 */
public class ItemVoidTester extends ItemBase {

    public ItemVoidTester(String name){
        super(name);
        this.setMaxStackSize(1);
    }

    private void addRemoveVoidEnergy(World world, EntityPlayer player, int amt){
        final Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(player));
        final ChunkPos chunkPos = chunk.getPos();
        final IVoidStorage voidStorage = CapabilityVoidEnergy.getVoidEnergy(chunk);

        if(voidStorage != null)
        {
            if(player.isSneaking())
            {
                voidStorage.extractVoid(amt, false);
            }else
            {
                voidStorage.receiveVoid(amt, false);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!worldIn.isRemote)
        {
            addRemoveVoidEnergy(worldIn, playerIn, 50);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
