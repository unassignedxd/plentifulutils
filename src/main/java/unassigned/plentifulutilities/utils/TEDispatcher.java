package unassigned.plentifulutilities.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public final class TEDispatcher {

    public static void packetToNearbyPlayers(TileEntity tileEntity) {
        WorldServer world = (WorldServer) tileEntity.getWorld();
        PlayerChunkMapEntry entry = world.getPlayerChunkMap().getEntry(tileEntity.getPos().getX() >> 4, tileEntity.getPos().getZ() >> 4);
        if(entry == null) return;

        for(EntityPlayerMP playerNear : entry.getWatchingPlayers()){
            ((EntityPlayerMP) playerNear).connection.sendPacket(tileEntity.getUpdatePacket());
        }
    }
}
