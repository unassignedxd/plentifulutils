package unassigned.plentifulutilities.voidenergy.capability;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.network.MessageUpdateVoidValue;
import unassigned.plentifulutilities.utils.CapabilityProviderSimple;
import unassigned.plentifulutilities.utils.ModUtil;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidHolder;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidHolderModifiable;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidStorage;

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
public class CapabilityVoidEnergy {

    @CapabilityInject(IVoidHolder.class)
    public static final Capability<IVoidHolder> CHUNK_VOID_ENERGY = null;

    public static final EnumFacing DEFAULT_FACING = null;

    public static final int DEFAULT_CAPACITY = 1000;

    private static final ResourceLocation ID = new ResourceLocation(ModUtil.MODID, "void_energy");

    public static void register(){
        CapabilityManager.INSTANCE.register(IVoidHolder.class, new Capability.IStorage<IVoidHolder>() {
            @Override
            public NBTBase writeNBT(Capability<IVoidHolder> capability, IVoidHolder instance, EnumFacing side) {
                return new NBTTagCompound();
            }

            @Override
            public void readNBT(Capability<IVoidHolder> capability, IVoidHolder instance, EnumFacing side, NBTBase nbt) {}
        }, VoidEnergyHolder::new);
    }

    @Nullable
    public static IVoidHolder getVoidEnergyHolder(final World world){
        return getCapability(world, CHUNK_VOID_ENERGY, DEFAULT_FACING);
    }

    @Nullable
    public static <T> T getCapability(@Nullable ICapabilityProvider provider, Capability<T> capability, @Nullable EnumFacing facing) {
        return provider != null && provider.hasCapability(capability, facing) ? provider.getCapability(capability, facing) : null;
    }

    @Nullable
    public static IVoidStorage getVoidEnergy(final World world, final ChunkPos chunkPos){
        final IVoidHolder voidHolder = getVoidEnergyHolder(world);
        if(voidHolder == null) return null;

        return voidHolder.getVoidEnergy(chunkPos);
    }

    public static IVoidStorage getVoidEnergy(final Chunk chunk){ return getVoidEnergy(chunk.getWorld(), chunk.getPos()); }


    @Mod.EventBusSubscriber(modid = ModUtil.MODID)
    private static class VoidEventHandler {

        @SubscribeEvent
        public static void onAttachCapabilities(final AttachCapabilitiesEvent<World> event){
            final IVoidHolder voidHolder = new VoidEnergyHolder();
            event.addCapability(ID, new CapabilityProviderSimple<>(voidHolder, CHUNK_VOID_ENERGY, DEFAULT_FACING));
        }

        @SubscribeEvent
        public static void onChunkDataLoad(final ChunkDataEvent.Load event){
            final World world = event.getWorld();
            final ChunkPos chunkPos = event.getChunk().getPos();

            final IVoidHolder voidHolder = getVoidEnergyHolder(world);
            if(!(voidHolder instanceof IVoidHolderModifiable)) return;

            final VoidEnergy voidEnergy = new VoidEnergy(DEFAULT_CAPACITY, world, chunkPos);

            final NBTTagCompound chunkData = event.getData();
            if(chunkData.hasKey(ID.toString(), Constants.NBT.TAG_INT)){
                final NBTTagInt voidTag = (NBTTagInt) chunkData.getTag(ID.toString());
                voidEnergy.deserializeNBT(voidTag);
            }

            ((IVoidHolderModifiable) voidHolder).setVoidEnergy(chunkPos, voidEnergy);
        }

        @SubscribeEvent
        public static void onChunkLoad(final ChunkEvent.Load event){
            final World world = event.getWorld();
            final ChunkPos chunkPos = event.getChunk().getPos();

            final IVoidHolder voidHolder = getVoidEnergyHolder(world);
            if (!(voidHolder instanceof IVoidHolderModifiable)) return;

            if(voidHolder.getVoidEnergy(chunkPos) != null) return;

            final IVoidStorage voidStorage = new VoidEnergy(DEFAULT_CAPACITY, world, chunkPos);
            ((IVoidHolderModifiable) voidHolder).setVoidEnergy(chunkPos, voidStorage);
        }

        @SubscribeEvent
        public static void onChunkDataSave(final ChunkDataEvent.Save event){
            final IVoidStorage voidStorage = getVoidEnergy(event.getChunk());
            if(!(voidStorage instanceof VoidEnergy)) return;

            event.getData().setTag(ID.toString(), ((VoidEnergy) voidStorage).serializeNBT());
        }

        @SubscribeEvent
        public static void onChunkUnload(final ChunkEvent.Unload event){
            final IVoidHolder voidHolder = getVoidEnergyHolder(event.getWorld());
            if(!(voidHolder instanceof IVoidHolderModifiable)) return;

            ((IVoidHolderModifiable) voidHolder).removeVoidEnergy(event.getChunk().getPos());
        }

        @SubscribeEvent
        public static void onPlayerChunkWatch(final ChunkWatchEvent.Watch event) {
            final EntityPlayerMP player = event.getPlayer();
            final IVoidStorage voidStorage = getVoidEnergy(player.getEntityWorld(), event.getChunkInstance().getPos());
            if(voidStorage == null) return;

            PlentifulUtilities.network.sendTo(new MessageUpdateVoidValue(voidStorage), player);
        }
    }
}
