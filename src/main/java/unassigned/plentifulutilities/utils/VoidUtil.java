package unassigned.plentifulutilities.utils;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import unassigned.plentifulutilities.blocks.ModBlocks;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentifulutils/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/29/2018 for plentifulutils
 */
public final class VoidUtil {

    public static void spawnParticlesInMiddleChunk(World world, ChunkPos pos)
    {
        double x = ((pos.x) << 4) + 8.5;
        double z = ((pos.z) << 4) + 8.5;
        int y = world.getHeight((int)x,(int)z) + 5;

        ((WorldServer) world).spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, true, x, y, z, 1000, 0, 1, 0, 3D);
    }

    public static void spawnParticlesRandomWithinChunk(World world, ChunkPos pos)
    {
        double x = ((pos.x) << 4) + (world.rand.nextInt(8 + 1 + 8) - 8); //todo remove minus, its additive (0->16) not (-8->8) also: create item that warns player of unstable void events. Also create model for void hole, possibly make the size get smaller/bigger depending on the void in the area, and decay once void is stabliized. over all clean up code.
        double z = ((pos.z) << 4) + (world.rand.nextInt(8 + 1 + 8) - 8);
        int y = world.getHeight((int)x,(int)z) + world.rand.nextInt(16 + 1 - 2) + 2;

        ((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, true, x, y, z, 50, 0, 0, 0, 1D);
    }

    public static void setBlockStateInMiddleChunk(World world, ChunkPos pos)
    {
        double x = ((pos.x) << 4) + 8.5;
        double z = ((pos.z) << 4) + 8.5;
        int y = world.getHeight((int)x,(int)z) + 5;

        ((WorldServer) world).setBlockState(new BlockPos(x, y, z), ModBlocks.voidHole.getDefaultState());
    }
}
