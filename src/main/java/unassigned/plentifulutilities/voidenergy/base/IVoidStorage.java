package unassigned.plentifulutilities.voidenergy.base;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;

/**
 * This code is derived from IEnergyStorage owned by cofh and the forge team! This code has been modified to fit my needs, however, credit
 * should go towards the individuals stated above.
 */
public interface IVoidStorage {

    int receiveVoid(int maxReceive, boolean simulate);

    int extractVoid(int maxExtract, boolean simulate);

    int getVoidStored();

    int getMaxVoidStored();

    World getWorld();

    ChunkPos getChunkPos();

    ArrayList<Chunk> getNearbyChunks();

    int getDangerTicks();


}
