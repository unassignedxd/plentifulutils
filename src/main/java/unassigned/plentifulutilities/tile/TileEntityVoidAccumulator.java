package unassigned.plentifulutilities.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import unassigned.plentifulutilities.items.ItemVoidMesh;
import unassigned.plentifulutilities.items.ModItems;
import unassigned.plentifulutilities.utils.ItemUtil;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class TileEntityVoidAccumulator extends TileEntityInventoryBase {

    public static final int SLOT_VOID = 0;

    public int processTime;
    public int timeToProcess = 220;


    public TileEntityVoidAccumulator() { super("void_accumulator", 1); }

    @Override
    public void update() {
        super.update();
        processTime++;
        if(!world.isRemote && (processTime % timeToProcess == 0))
        {
            ItemStack slot = this.inv.getStackInSlot(SLOT_VOID);
            int val = world.rand.nextInt(3);

            if((slot.getItem() == ModItems.item_staticVoidShard || slot.getItem() == Items.AIR) && hasVoidCollector())
            {
                if(slot.getCount() < 64)
                {
                    slot.grow(val+slot.getCount() < 63 ? val+1 : 1);   //rusty on 1 line booleans -> condition ? false : true;
                }
            }
        }
    }

    public boolean hasVoidCollector(){
        for(TileEntity tileEntity : this.tilesNear)
        {
            if(tileEntity instanceof TileEntityVoidCollector) { return true; }
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.processTime = compound.getInteger("ProcessTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ProcessTime", this.processTime);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean shouldSyncInventory() { return true; }

}
