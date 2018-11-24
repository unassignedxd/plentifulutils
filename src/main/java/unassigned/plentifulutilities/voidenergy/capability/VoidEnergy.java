package unassigned.plentifulutilities.voidenergy.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.network.MessageUpdateVoidValue;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidStorage;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
public class VoidEnergy extends VoidStorage implements IVoidStorage, INBTSerializable<NBTTagCompound> {

    private final World world;

    private final ChunkPos chunkPos;

    public VoidEnergy(final int capacity, final World world, final ChunkPos chunkPos){
        super(capacity);
        this.world = world;
        this.chunkPos = chunkPos;

        energy = capacity;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("VoidStored", getVoidStored());
        tag.setInteger("TicksElapsed", getTicksElapsed());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        energy = nbt.getInteger("VoidStored");
        ticks = nbt.getInteger("TicksElapsed");
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    @Override
    public int receiveVoid(int maxReceive, boolean simulate) {
        final int voidReceived = super.receiveVoid(maxReceive, simulate);

        if(!simulate && voidReceived != 0) onVoidChanged();

        return voidReceived;
    }

    @Override
    public int extractVoid(int maxExtract, boolean simulate) {
        final int voidExtracted = super.extractVoid(maxExtract, simulate);

        if (!simulate && voidExtracted != 0) onVoidChanged();

        return voidExtracted;
    }

    public void setVoidEnergy(final int energy){
        this.energy = energy;
        onVoidChanged();
    }

    @Override
    public void setTicksElapsed(final int ticks){
        this.ticks = ticks;
        onVoidChanged();
    }

    /**
     *  This section of the code was derived from Choonster. This code is not to be published publicly.
     *
     * **/
    protected void onVoidChanged() {
        final World world = getWorld();
        final ChunkPos chunkPos = getChunkPos();
        if(world.isRemote) return;

        final BlockPos chunkOrigin = chunkPos.getBlock(0,0,0);
        if(world.isBlockLoaded(chunkOrigin)) { world.getChunkFromChunkCoords(chunkPos.x, chunkPos.z).markDirty(); }

        final PlayerChunkMapEntry playerchunkMapEntry = ((WorldServer) world).getPlayerChunkMap().getEntry(chunkPos.x, chunkPos.z);
        if(playerchunkMapEntry == null) return;

        final IMessage message = new MessageUpdateVoidValue(this);
        PlentifulUtilities.network.sendToAllTracking(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), chunkOrigin.getX(), chunkOrigin.getY(), chunkOrigin.getZ(), 0));
    }

}
