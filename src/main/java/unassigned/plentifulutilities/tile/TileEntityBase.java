package unassigned.plentifulutilities.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import unassigned.plentifulutilities.tile.interfacing.IGiveEnergy;
import unassigned.plentifulutilities.tile.interfacing.IGiveVoid;
import unassigned.plentifulutilities.utils.ModUtil;
import unassigned.plentifulutilities.utils.TEDispatcher;
import unassigned.plentifulutilities.utils.TileUtil;

import javax.annotation.Nullable;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public class TileEntityBase extends TileEntity implements ITickable { //ITickable to allow the TE to update across ticks

    public final String name;
    public boolean dropInv;  //if true, this TE will drop its inventory
    protected TileEntity[] tilesNear = new TileEntity[6]; //to reference the tiles near the TE for calculations
    protected int ticksAlive;

    public TileEntityBase(String name){
        this.name = name;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("DropInv", this.dropInv);
        compound.setInteger("TicksAlive", this.ticksAlive);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.dropInv = compound.getBoolean("DropInv");
        this.ticksAlive = compound.getInteger("TicksAlive");
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.getPos(), -1, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);

        return compound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return !oldState.getBlock().isAssociatedBlock(newState.getBlock());
    }

    public final void sendUpdate(){ if(world != null && !world.isRemote) TEDispatcher.packetToNearbyPlayers(this); }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("container."+ ModUtil.MODID+"."+this.name+".name"); //this will format into container.plentifulutils.x.name - the correct for translation
    }

    private boolean doesGiveEnergy = this instanceof IGiveEnergy;

    @Override
    public void update() {
        if(doesGiveEnergy)
        {
            IGiveEnergy give = (IGiveEnergy)this;
            if(give.doesGiveEnergy())
            {
                int energyTotal = give.getEnergyToShare();
                if(energyTotal > 0)
                {
                    EnumFacing[] curSides = give.getGiveSides();

                    int amountToGive = energyTotal/curSides.length;
                    if(energyTotal <= 0) amountToGive = energyTotal;

                    for(EnumFacing side : curSides)
                    {
                        TileEntity tileToCheck = this.tilesNear[side.ordinal()];
                        if(tileToCheck != null && give.canShareToTile(tileToCheck))
                        {
                            TileUtil.doEnergyInteraction(this, tileToCheck, side, amountToGive);
                        }
                    }
                }
            }
        }
    }

    public void saveData() {
        for(EnumFacing side : EnumFacing.VALUES)
        {
            BlockPos pos = this.pos.offset(side);
            if(this.world.isBlockLoaded(pos))
            {
                this.tilesNear[side.ordinal()] = this.world.getTileEntity(pos);
            }
        }
    }

    public boolean shouldSaveData() {
        return this instanceof IGiveEnergy || this instanceof IGiveVoid;
    }

    protected boolean sendUpdateWithInterval(){
        if(this.ticksAlive%2 == 0)  //going to have configurability to stop machines updating too often, in hopes to prevent lag
        {
            this.sendUpdate();
            return true;
        } else { return false; }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY)
        {
            IEnergyStorage storage = this.getEnergyStorage(facing);
            if(storage == null)
            {
                return (T)storage;
            }
        }
        else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            IItemHandler iHandler = this.getItemHandler(facing);
            if(iHandler == null)
            {
                return (T)iHandler;
            }
        }
        return super.getCapability(capability, facing);
    }

    public IEnergyStorage getEnergyStorage(EnumFacing facing){ return null; }
    public IItemHandler getItemHandler(EnumFacing facing){ return null; }

}
