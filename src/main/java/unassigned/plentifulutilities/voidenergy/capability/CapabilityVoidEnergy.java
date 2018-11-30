package unassigned.plentifulutilities.voidenergy.capability;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
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
import unassigned.plentifulutilities.utils.capability.CapabilityProviderSerializable;
import unassigned.plentifulutilities.utils.ModUtil;
import unassigned.plentifulutilities.utils.VoidUtil;
import unassigned.plentifulutilities.voidenergy.base.IVoidStorage;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidHolder;
import unassigned.plentifulutilities.voidenergy.base.energy.IVoidHolderModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

/**
 * This code is not owned by me! This is a test derived from Choonster's TestMod3. This code, however, has been modified to fit my current needs -
 * and will be re-written with the knowledge obtained from using Choonster's code as a base.
 *
 * - I am aware that all ChunkData classes will be deprecated and removed in 1.13 - but as stated about, this is a test to help with my
 * understanding of how chunks, capabilities and holder classes work.
 */
public class CapabilityVoidEnergy {

    @CapabilityInject(IVoidHolder.class)
    public static final Capability<IVoidHolder> VOID_CHUNKS = null;

    @CapabilityInject(IVoidStorage.class)
    public static final Capability<IVoidStorage> CHUNK_VOID_STORAGE = null;

    public static final EnumFacing DEFAULT_FACING = null;

    public static final int DEFAULT_ENERGY = 5000;
    public static final int BASE_CAPACITY = 10000;

    public static final ArrayList<ChunkPos> LOADED_CHUNKS_POS = new ArrayList<>();

    private static final ResourceLocation ID = new ResourceLocation(ModUtil.MODID, "void_energy");

    public static void register() {
        CapabilityManager.INSTANCE.register(IVoidHolder.class, new Capability.IStorage<IVoidHolder>() {
            @Override
            public NBTBase writeNBT(final Capability<IVoidHolder> capability, final IVoidHolder instance, final EnumFacing side) {
                return new NBTTagCompound();
            }

            @Override
            public void readNBT(final Capability<IVoidHolder> capability, final IVoidHolder instance, final EnumFacing side, final NBTBase nbt) {

            }
        }, VoidEnergyHolder::new);
        //new chunk based capability
        CapabilityManager.INSTANCE.register(IVoidStorage.class, new Capability.IStorage<IVoidStorage>() {
            @Override
            public NBTBase writeNBT(final Capability<IVoidStorage> capability, final IVoidStorage instance, final EnumFacing side) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("voidStored", instance.getVoidStored());
                tag.setInteger("dangerTicks", instance.getDangerTicks());
                return tag;
            }

            @Override
            public void readNBT(final Capability<IVoidStorage> capability, final IVoidStorage instance, final EnumFacing side, final NBTBase nbt) {
                if (!(instance instanceof VoidEnergy))
                    throw new IllegalArgumentException("Given incorrect instance from implementation");

                ((VoidEnergy) instance).setVoidEnergy(((NBTTagCompound)nbt).getInteger("voidStored"));
                ((VoidEnergy) instance).setDangerTicks(((NBTTagCompound)nbt).getInteger("dangerTicks"));
            }
        }, () -> null);
    }

    @Nullable
    public static IVoidHolder getVoidEnergyHolder(final World world){
        return getCapability(world, VOID_CHUNKS, DEFAULT_FACING);
    }

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

        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<World> event) {
            final IVoidHolder voidHolder = new VoidEnergyHolder();
            event.addCapability(ID, new CapabilityProviderSerializable<>(VOID_CHUNKS, DEFAULT_FACING, voidHolder));
        }

        @SubscribeEvent
        public static void attachChunkCapabiliies(final AttachCapabilitiesEvent<Chunk> event){
            final Chunk chunk = event.getObject();
            final IVoidStorage voidStorage = new VoidEnergy(BASE_CAPACITY, DEFAULT_ENERGY, chunk.getWorld(), chunk.getPos());
            event.addCapability(ID, new CapabilityProviderSerializable<>(CHUNK_VOID_STORAGE, DEFAULT_FACING, voidStorage));
        }

        @SubscribeEvent
        public static void chunkWatch(final ChunkWatchEvent.Watch event) {
            final EntityPlayerMP player = event.getPlayer();
            final IVoidStorage voidStorage = getVoidEnergy(event.getChunkInstance());
            if (voidStorage == null) return;

            PlentifulUtilities.network.sendToDimension(new MessageUpdateVoidValue(voidStorage), player.dimension);
        }

        @SubscribeEvent
        public static void chunkLoad(final ChunkEvent.Load event) {
            final World world = event.getWorld();
            final Chunk chunk = event.getChunk();
            final ChunkPos chunkPos = chunk.getPos();

            final IVoidHolder chunkEnergyHolder = getVoidEnergyHolder(world);
            if (!(chunkEnergyHolder instanceof IVoidHolderModifiable)) return;

            if (chunkEnergyHolder.getVoidEnergy(chunkPos) != null) return;

            final IVoidStorage chunkEnergy = getVoidEnergy(chunk);
            if (chunkEnergy != null) {
                ((IVoidHolderModifiable) chunkEnergyHolder).setVoidEnergy(chunkPos, chunkEnergy);
            }
        }

        @SubscribeEvent
        public static void chunkUnload(final ChunkEvent.Unload event) {
            final IVoidHolder chunkEnergyHolder = getVoidEnergyHolder(event.getWorld());
            if (!(chunkEnergyHolder instanceof IVoidHolderModifiable)) return;

            ((IVoidHolderModifiable) chunkEnergyHolder).removeVoidEnergy(event.getChunk().getPos());
        }

        private static int globalVoidEvent; //this doesn't need to save to NBT as its actual value is unimportant
        private static int localTick; //this is updated once a second (every globalvoidevent % 20)
        public static int LOWER_THRESHOLD = (int)(DEFAULT_ENERGY / 2); //The lower threshold where void regen will occur
        public static int UPPER_THRESHOLD = (int)(DEFAULT_ENERGY * 1.5); //The upper threshold where void will disperse
        public static int DANGER_HIGH_THRESHOLD = (int)(DEFAULT_ENERGY * 1.8); //The high danger threshold where void will become unstable
        public static int DANGER_LOW_THRESHOLD = (int)(DEFAULT_ENERGY / 5); //The low danger threshold where void will become unstable

        @SubscribeEvent
        public static void onWorldTick(final TickEvent.WorldTickEvent event) {
            World world = event.world;
            Random rand = world.rand;
            globalVoidEvent++;

            if(globalVoidEvent % 20 == 0)
            {
                localTick++;
                for(ChunkPos pos : VoidEnergyHolder.getVoidEnergies().keySet())
                {
                    if(pos == null) return;



                    IVoidStorage thisStorage = getVoidEnergy(world.getChunkFromChunkCoords(pos.x, pos.z));
                    if(thisStorage == null) return;

                    ((VoidEnergy) thisStorage).updateVoid(localTick);
                    /*
                    if(thisStorage.getVoidStored() < LOWER_THRESHOLD)
                    {
                        if(globalVoidEvent % 160 == 0) { thisStorage.receiveVoid(1, false); } //idle regen

                        if(thisStorage.getNearbyChunks() != null)
                        {
                            for(Chunk chunk : thisStorage.getNearbyChunks())
                            {
                                if(chunk == null) return;

                                IVoidStorage otherStorage = getVoidEnergy(chunk);
                                if(otherStorage == null) return;

                                if(otherStorage.getVoidStored() > LOWER_THRESHOLD)
                                {
                                    int scaledAmt = (int)((float)(LOWER_THRESHOLD - thisStorage.getVoidStored()) / 500) + 1; //2500 - 2500 = 0 -> 1; 2500 - 2000 > 500 / 500 = 1 -> 2;
                                    thisStorage.receiveVoid(scaledAmt, false);
                                    otherStorage.extractVoid(scaledAmt, false);
                                }
                            }
                        }

                        if(thisStorage.getVoidStored() < DANGER_LOW_THRESHOLD)
                        {
                            ((VoidEnergy)thisStorage).updateDangerTicks();
                        }
                    }

                    if(thisStorage.getVoidStored() > UPPER_THRESHOLD)
                    {
                        if(globalVoidEvent % 160 == 0) { thisStorage.extractVoid(1, false); } //idle loss

                        if(thisStorage.getNearbyChunks() != null)
                        {
                            for(Chunk chunk : thisStorage.getNearbyChunks())
                            {
                                if(chunk == null) return;

                                IVoidStorage otherStorage = getVoidEnergy(chunk);
                                if(otherStorage == null) return;

                                //No if check as this will give its energy to anything possible as its 'pre-unstable'
                                int scaledAmt = (int)((float)(thisStorage.getVoidStored() - UPPER_THRESHOLD) / 500) + 1; //math: 7500 - 7500 > 0 / 1000 = 0; 8000 - 7500 > 500 / 500 = 1. 9000 - 7500 > 1500 / 500 > 3; The goal of this is to release more energy based on how high the energy is.
                                otherStorage.receiveVoid(scaledAmt, false);
                                thisStorage.extractVoid(scaledAmt, false);
                            }
                        }
                        if(thisStorage.getVoidStored() > DANGER_HIGH_THRESHOLD)
                        {
                            ((VoidEnergy)thisStorage).updateDangerTicks();
                        }
                    }
*/

                }

            }

        }

    }
}
