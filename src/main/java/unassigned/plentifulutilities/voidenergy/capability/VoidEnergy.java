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
import unassigned.plentifulutilities.blocks.ModBlocks;
import unassigned.plentifulutilities.network.MessageUpdateVoidValue;
import unassigned.plentifulutilities.utils.VoidUtil;
import unassigned.plentifulutilities.voidenergy.base.IVoidStorage;
import unassigned.plentifulutilities.voidenergy.base.VoidStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;

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
    private int dangerEventTicks;

    public VoidEnergy(final int capacity, final int baseEnergy, final World world, final ChunkPos chunkPos){
        super(capacity);
        this.world = world;
        this.chunkPos = chunkPos;

        this.dangerEventTicks = 0;
        energy = baseEnergy;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("VoidStored", getVoidStored());
        tag.setInteger("DangerTicks", getDangerTicks());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        energy = nbt.getInteger("VoidStored");
        dangerEventTicks = nbt.getInteger("DangerTicks");
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

    @Override
    public int getMaxVoidStored() {
        return this.capacity;
    }

    @Nullable
    @Override
    public ArrayList<Chunk> getNearbyChunks(){
        World world = this.getWorld();
        ChunkPos orginChunk = this.getChunkPos();
        ArrayList<Chunk> chunks = new ArrayList<>();
        /*
        @Nullable
        Chunk topChunk = world.getChunkFromChunkCoords(orginChunk.x+0, orginChunk.z+1);
        @Nullable
        Chunk rightChunk = world.getChunkFromChunkCoords(orginChunk.x+1, orginChunk.z+0);
        @Nullable
        Chunk bottomChunk = world.getChunkFromChunkCoords(orginChunk.x+0, orginChunk.z-1);
        @Nullable
        Chunk leftChunk = world.getChunkFromChunkCoords(orginChunk.x-1, orginChunk.z+0);
        */
        for(int x = -1; x <= 1; x++)
        {
            for(int z = -1; z <= 1; z++)
            {
                chunks.add(world.getChunkFromChunkCoords(orginChunk.x+x, orginChunk.z+z));
            }
        } //this considers chunks in a 3x3 area [this will include the ORGIN chunk]

        return chunks;
    }

    public void setVoidEnergy(final int energy){
        this.energy = energy;
        onVoidChanged();
    }

    @Override
    public int getDangerTicks() { return this.dangerEventTicks; }

    public void setDangerTicks(final int loadedTicks) {
        this.dangerEventTicks = loadedTicks;
        onVoidChanged();
    }
    /*
        Call this every void update.
     */
    public void updateVoid(int curTick)
    {
        int thresholdLow = CapabilityVoidEnergy.VoidHandler.LOWER_THRESHOLD;
        int thresholdHigh = CapabilityVoidEnergy.VoidHandler.UPPER_THRESHOLD;
        int dangerHigh = CapabilityVoidEnergy.VoidHandler.DANGER_HIGH_THRESHOLD;
        int dangerLow = CapabilityVoidEnergy.VoidHandler.DANGER_LOW_THRESHOLD;

        if(getVoidStored() < thresholdLow || getVoidStored() > thresholdHigh)
        {
            if(this.getNearbyChunks() == null) return;

            for(Chunk chunk : this.getNearbyChunks())
            {
                if(chunk == null || (chunk.getPos() == chunkPos)) return;

                IVoidStorage nearStorage = CapabilityVoidEnergy.getVoidEnergy(chunk);
                if(nearStorage == null) return;

                if(getVoidStored() > thresholdHigh)
                {
                    int scaledAmt = (int)((float)(this.getVoidStored() - thresholdHigh) / 1000) + 1;
                    this.extractVoid(scaledAmt, false);
                    nearStorage.receiveVoid(scaledAmt, false);
                }else if(getVoidStored() < thresholdLow)
                {
                    int scaledAmt = (int)((float)(thresholdLow - this.getVoidStored()) / 1000) + 1;
                    this.receiveVoid(scaledAmt, false);
                    nearStorage.extractVoid(scaledAmt, false);
                }

            }
        }

        if(getVoidStored() < dangerLow || getVoidStored() > dangerHigh)
        {
            updateDangerTicks(dangerHigh, dangerLow);
        }
    }

    public void updateDangerTicks(int dangerHigh, int dangerLow) {
        this.dangerEventTicks++;

        if(this.energy > dangerHigh)
        {
            VoidUtil.spawnParticlesRandomWithinChunk(this.world, this.chunkPos, 10);

            if(dangerEventTicks == 60)
            {
                VoidUtil.spawnParticlesInMiddleChunk(this.world, this.chunkPos);
            }
            if(dangerEventTicks == 64)
            {
                VoidUtil.setBlockStateInMiddleChunk(this.world, this.chunkPos, ModBlocks.voidHole);
            }

        }else if(this.energy < dangerLow)
        {
        }

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
