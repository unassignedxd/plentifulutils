package unassigned.plentifulutilities.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidStorage;
import unassigned.plentifulutilities.voidenergy.capability.CapabilityVoidEnergy;
import unassigned.plentifulutilities.voidenergy.capability.VoidEnergy;
import unassigned.plentifulutilities.voidenergy.capability.VoidStorage;

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
public class MessageUpdateVoidValue implements IMessage {

    private ChunkPos chunkPos;

    private int voidEnergy;
    private int ticks;

    public MessageUpdateVoidValue() {
    }

    public MessageUpdateVoidValue(IVoidStorage voidStorage) {
        this.chunkPos = voidStorage.getChunkPos();
        this.voidEnergy = voidStorage.getVoidStored();
        this.ticks = voidStorage.getTicksElapsed();
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf The buffer
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        final int chunkX = buf.readInt();
        final int chunkZ = buf.readInt();
        chunkPos = new ChunkPos(chunkX, chunkZ);
        voidEnergy = buf.readInt();
        ticks = buf.readInt();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf The buffer
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(chunkPos.x);
        buf.writeInt(chunkPos.z);
        buf.writeInt(voidEnergy);
        buf.writeInt(ticks);
    }

    public static class Handler implements IMessageHandler<MessageUpdateVoidValue, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(MessageUpdateVoidValue message, MessageContext ctx) {
            PlentifulUtilities.proxy.getThreadListener(ctx).addScheduledTask(() -> {
                final World world = PlentifulUtilities.proxy.getClientWorld();
                assert world != null;

                final IVoidStorage vEnergy = CapabilityVoidEnergy.getVoidEnergy(world.getChunkFromChunkCoords(message.chunkPos.x, message.chunkPos.z));
                if (!(vEnergy instanceof VoidEnergy)) return;

                ((VoidEnergy) vEnergy).setVoidEnergy(message.voidEnergy);
                ((VoidEnergy) vEnergy).setTicksElapsed(message.ticks);
            });

            return null;
        }
    }
}