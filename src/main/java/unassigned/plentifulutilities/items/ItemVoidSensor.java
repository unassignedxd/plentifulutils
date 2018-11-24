package unassigned.plentifulutilities.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import unassigned.plentifulutilities.items.base.ItemBase;
import unassigned.plentifulutilities.utils.ModUtil;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidStorage;
import unassigned.plentifulutilities.voidenergy.capability.CapabilityVoidEnergy;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
public class ItemVoidSensor extends ItemBase {

    public ItemVoidSensor(String name){
        super(name);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!worldIn.isRemote){
            final Chunk chunk = worldIn.getChunkFromBlockCoords(new BlockPos(playerIn));
            final ChunkPos chunkPos = chunk.getPos();
            final IVoidStorage voidStorage = CapabilityVoidEnergy.getVoidEnergy(chunk);

            if(voidStorage != null){
                playerIn.sendMessage(new TextComponentTranslation("message." + ModUtil.MODID + ":void_energy.get", chunkPos, voidStorage.getVoidStored()));
            }else {
                playerIn.sendMessage(new TextComponentTranslation("message." + ModUtil.MODID + ":void_energy.not_found", chunkPos));
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
