package unassigned.plentifulutilities.utils.tasks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentifulutils/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/29/2018 for plentifulutils
 *
 * This code was helped created by V0idWa1k3r
 */
public class ExplosionTask implements INBTSerializable<NBTTagCompound> {

    public int timestamp;
    public BlockPos spawnPos;
    public float strength;

    public static ExplosionTask newTask(int currentTick, int delay, BlockPos pos, float strength)
    {
        ExplosionTask ret = new ExplosionTask();
        ret.timestamp = currentTick + delay;
        ret.spawnPos = pos;
        ret.strength = strength;
        return ret;
    }

    public boolean shouldFire(int ticks)
    {
        return ticks >= this.timestamp;
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("timestamp", this.timestamp);
        compound.setInteger("spawnX", this.spawnPos.getX());
        compound.setInteger("spawnY", this.spawnPos.getY());
        compound.setInteger("spawnZ", this.spawnPos.getZ());
        compound.setFloat("strength", this.strength);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.timestamp = nbt.getInteger("timestamp");
        this.spawnPos = new BlockPos(nbt.getInteger("spawnX"), nbt.getInteger("spawnY"), nbt.getInteger("spawnZ"));
        this.strength = nbt.getFloat("strength");
    }
}
