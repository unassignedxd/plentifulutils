package unassigned.plentifulutilities.voidenergy.capability;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.network.MessageUpdateVoidValue;
import unassigned.plentifulutilities.utils.CapabilityProviderSerializable;
import unassigned.plentifulutilities.utils.ModUtil;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidHolder;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidStorage;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This code is not owned by me! This is a test derived from Choonster's TestMod3. This code, however, has been modified to fit my current needs -
 * and will be re-written with the knowledge obtained from using Choonster's code as a base.
 *
 * - I am aware that all ChunkData classes will be deprecated and removed in 1.13 - but as stated about, this is a test to help with my
 * understanding of how chunks, capabilities and holder classes work.
 */
public class CapabilityVoidEnergy {

   // @CapabilityInject(IVoidHolder.class)
   // public static final Capability<IVoidHolder> CHUNK_VOID_ENERGY = null;

    @CapabilityInject(IVoidStorage.class)
    public static final Capability<IVoidStorage> CHUNK_VOID_STORAGE = null;

    public static final EnumFacing DEFAULT_FACING = null;

    public static final int DEFAULT_ENERGY = 1000;
    public static final int BASE_CAPACITY = 6000;

    public static final Map<ChunkPos, IVoidStorage> VOID_CHUNKS_LOADED = new ConcurrentHashMap<>();

    private static final ResourceLocation ID = new ResourceLocation(ModUtil.MODID, "void_energy");

    public static void register() {
        /*
        //todo remove
        CapabilityManager.INSTANCE.register(IVoidHolder.class, new Capability.IStorage<IVoidHolder>() {
            @Override
            public NBTBase writeNBT(final Capability<IVoidHolder> capability, final IVoidHolder instance, final EnumFacing side) {
                return new NBTTagCompound();
            }

            @Override
            public void readNBT(final Capability<IVoidHolder> capability, final IVoidHolder instance, final EnumFacing side, final NBTBase nbt) {

            }
        }, VoidEnergyHolder::new);
        */
        //new chunk based capability
        CapabilityManager.INSTANCE.register(IVoidStorage.class, new Capability.IStorage<IVoidStorage>() {
            @Override
            public NBTBase writeNBT(final Capability<IVoidStorage> capability, final IVoidStorage instance, final EnumFacing side) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("voidStored", instance.getVoidStored());
                tag.setInteger("ticksElapsed", instance.getTicksElapsed());
                tag.setInteger("maxVoidStored", instance.getMaxVoidStored());
                return tag;
            }

            @Override
            public void readNBT(final Capability<IVoidStorage> capability, final IVoidStorage instance, final EnumFacing side, final NBTBase nbt) {
                if (!(instance instanceof VoidEnergy))
                    throw new IllegalArgumentException("Given incorrect instance from implementation");

                ((VoidEnergy) instance).setVoidEnergy(((NBTTagCompound)nbt).getInteger("voidStored"));
                ((VoidEnergy) instance).setTicksElapsed(((NBTTagCompound)nbt).getInteger("ticksElapsed"));
                ((VoidEnergy) instance).setMaxVoidEnergy(((NBTTagCompound)nbt).getInteger("maxVoidStored"));
            }
        }, () -> null);
    }
    /*
    @Deprecated
    @Nullable
    public static IVoidHolder getVoidEnergyHolder(final World world){
        return getCapability(world, CHUNK_VOID_ENERGY, DEFAULT_FACING);
    }
    */
    @Nullable
    public static IVoidStorage getVoidEnergy(final Chunk chunk) {
        return getCapability(chunk, CHUNK_VOID_STORAGE, DEFAULT_FACING);
    }

    @Nullable
    public static <T> T getCapability(@Nullable ICapabilityProvider provider, Capability<T> capability, @Nullable EnumFacing facing) {
        return provider != null && provider.hasCapability(capability, facing) ? provider.getCapability(capability, facing) : null;
    }

    @Mod.EventBusSubscriber(modid = ModUtil.MODID)
    public static class VoidHandler {

        //todo: remove deprecations
        /*
        @Deprecated
        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<World> event) {
            final IVoidHolder voidHolder = new VoidEnergyHolder();
            event.addCapability(ID, new CapabilityProviderSimple<>(voidHolder, CHUNK_VOID_ENERGY, DEFAULT_FACING));
        }
        */
        @SubscribeEvent
        public static void attachChunkCapabiliies(final AttachCapabilitiesEvent<Chunk> event){
            final Chunk chunk = event.getObject();
            final IVoidStorage voidStorage = new VoidEnergy(BASE_CAPACITY, DEFAULT_ENERGY, chunk.getWorld(), chunk.getPos());
            event.addCapability(ID, new CapabilityProviderSerializable<>(CHUNK_VOID_STORAGE, DEFAULT_FACING, voidStorage));
        }

        /*
        @Deprecated
        @SubscribeEvent
        public static void chunkDataLoad(final ChunkDataEvent.Load event) {
            final World world = event.getWorld();
            final ChunkPos chunkPos = event.getChunk().getPos();

            final VoidEnergy voidEnergy = new VoidEnergy(DEFAULT_CAPACITY, world, chunkPos);

            final NBTTagCompound chunkData = event.getData();
            if (chunkData.hasKey(ID.toString(), Constants.NBT.TAG_INT)) {
                final NBTTagInt energyTag = (NBTTagInt) chunkData.getTag(ID.toString());
                voidEnergy.deserializeNBT(energyTag);
            }

            ((IVoidHolderModifiable) voidHolder).setVoidEnergy(chunkPos, voidEnergy);
        }
        */

        /*
        @Deprecated
        @SubscribeEvent
        public static void chunkDataSave(final ChunkDataEvent.Save event) {
            final IVoidStorage voidStorage = getVoidEnergy(event.getChunk());
            if (!(voidStorage instanceof VoidEnergy)) return;

            event.getData().setTag(ID.toString(), ((VoidEnergy) voidStorage).serializeNBT());
        }
        */

        @SubscribeEvent
        public static void chunkWatch(final ChunkWatchEvent.Watch event) {
            final EntityPlayerMP player = event.getPlayer();
            final IVoidStorage voidStorage = getVoidEnergy(event.getChunkInstance());
            if (voidStorage == null) return;

            PlentifulUtilities.network.sendTo(new MessageUpdateVoidValue(voidStorage), player);
        }

        @SubscribeEvent
        public static void chunkUnload(final ChunkEvent.Unload event) {
            ChunkPos chunkPos = event.getChunk().getPos();
            IVoidStorage voidStorage = getVoidEnergy(event.getChunk());
            VOID_CHUNKS_LOADED.remove(chunkPos);
        }

        @SubscribeEvent
        public static void chunkLoad(final ChunkEvent.Load event) {
            ChunkPos chunkPos = event.getChunk().getPos();
            IVoidStorage voidStorage = getVoidEnergy(event.getChunk());
            VOID_CHUNKS_LOADED.put(chunkPos, voidStorage);
        }

        @SubscribeEvent
        public static void onWorldTick(final TickEvent.WorldTickEvent event){
            World world = event.world;

            if(!world.isRemote)
            {
                for(Map.Entry<ChunkPos, IVoidStorage> entry : VOID_CHUNKS_LOADED.entrySet())
                {
                    IVoidStorage voidStorage = entry.getValue();
                    if(voidStorage.getVoidStored() < BASE_CAPACITY)
                    {
                    //    ((VoidEnergy) voidStorage).receiveVoid((world.rand.nextInt(10)+ 1), false);
                    //    PlentifulUtilities.network.sendToAll(new MessageUpdateVoidValue(voidStorage));
                    }
                }
            }
        }
    }
}
