package unassigned.plentifulutilities.voidenergy.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.network.MessageUpdateVoidValue;
import unassigned.plentifulutilities.voidenergy.base.IVoidStorage;
import unassigned.plentifulutilities.voidenergy.base.VoidStorage;

import javax.annotation.Nullable;

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

    private final Chunk[] nearbyChunks = new Chunk[4]; //four adjacent chunks (above[y+1], right[x+1], bottom[z-1], left[z-1])

    public VoidEnergy(final int capacity, final int baseEnergy, final World world, final ChunkPos chunkPos){
        super(capacity);
        this.world = world;
        this.chunkPos = chunkPos;

        energy = baseEnergy;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("VoidStored", getVoidStored());
        tag.setInteger("MaxVoidStored", getMaxVoidStored());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        energy = nbt.getInteger("VoidStored");
        capacity = nbt.getInteger("MaxVoidStored");
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

        onVoidChanged();
        return voidReceived;
    }

    @Override
    public int extractVoid(int maxExtract, boolean simulate) {
        final int voidExtracted = super.extractVoid(maxExtract, simulate);

        if (!simulate && voidExtracted != 0) onVoidChanged();

        return voidExtracted;
    }

    @Nullable
    @Override
    public Chunk[] getNearbyChunks(){
        World world = this.getWorld();
        ChunkPos orginChunk = this.getChunkPos();

        @Nullable
        Chunk topChunk = world.getChunkFromChunkCoords(orginChunk.x+0, orginChunk.z+1);
        @Nullable
        Chunk rightChunk = world.getChunkFromChunkCoords(orginChunk.x+1, orginChunk.z+0);
        @Nullable
        Chunk bottomChunk = world.getChunkFromChunkCoords(orginChunk.x+0, orginChunk.z-1);
        @Nullable
        Chunk leftChunk = world.getChunkFromChunkCoords(orginChunk.x-1, orginChunk.z+0);

        return new Chunk[] {topChunk, rightChunk, bottomChunk, leftChunk }; //0 - top, 1 - right, 2 - bottom, 3 - left; NOTE these chunks CAN BE NULL, as they can be considered UNLOADED
    }

    public void setVoidEnergy(final int energy){
        this.energy = energy;
        onVoidChanged();
    }

    public void setMaxVoidEnergy(final int maxVoidEnergy){
        this.capacity = maxVoidEnergy;
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
