package unassigned.plentifulutilities.blocks.base;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import unassigned.plentifulutilities.PlentifulUtilities;
import unassigned.plentifulutilities.blocks.client.IHasModel;
import unassigned.plentifulutilities.tile.TileEntityBase;
import unassigned.plentifulutilities.tile.TileEntityInventoryBase;
import unassigned.plentifulutilities.utils.ItemUtil;
import unassigned.plentifulutilities.utils.RegistryUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public abstract class BlockTEBase extends BlockContainer implements IHasModel {

    private final String name;

    public BlockTEBase(Material mat, String name) {
        super(mat);
        this.name=name;

        this.registerBlock();
    }

    private void registerBlock() {
        RegistryUtil.registerBlock(this, this.getItemBlock(), this.getBaseName());
    }

    protected String getBaseName(){
        return this.name;
    }

    protected ItemBlockBase getItemBlock() {
        return new ItemBlockBase(this);
    }

    public void registerRendering() {
        PlentifulUtilities.proxy.addRenderRegister(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    private void dropSlots(World world, BlockPos blockPos){
        if(!world.isRemote)
        {
            TileEntity te = world.getTileEntity(blockPos);
            if(te instanceof TileEntityInventoryBase)
            {
                TileEntityInventoryBase theTile = (TileEntityInventoryBase)te;
                if(theTile.inv.getSlots() > 0)
                {
                    for(int i = 0; i < theTile.inv.getSlots(); i++)
                    {
                        this.dropItemFromInventory(i, theTile, world, blockPos);
                    }
                }
            }
        }
    }

    private void dropItemFromInventory(int slot, TileEntityInventoryBase tile, World world, BlockPos blockPos){
        ItemStack stack = tile.inv.getStackInSlot(slot);
        if(ItemUtil.isValid(stack))
        {
            float rX = world.rand.nextFloat()*.8F+.1F;
            float rY = world.rand.nextFloat()*.8F+.1F;
            float rZ = world.rand.nextFloat()*.8F+.1F;
            EntityItem itemDrop = new EntityItem(world, blockPos.getX()+rX, blockPos.getY()+rY, blockPos.getZ()+rZ, stack.copy());
            itemDrop.motionX = world.rand.nextGaussian()*.5f;
            itemDrop.motionY = world.rand.nextGaussian()*.5f;
            itemDrop.motionZ = world.rand.nextGaussian()*.5f;
            world.spawnEntity(itemDrop);
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);

        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityBase){
            TileEntityBase base = (TileEntityBase)tile;
            if(base.shouldSaveData()){
                base.saveData();
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(!player.capabilities.isCreativeMode)
        {
            TileEntity tile = worldIn.getTileEntity(pos);
            if(tile instanceof TileEntityBase && ((TileEntityBase)tile).dropInv){ /* no functionality atm */ }
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityBase)
        {
            TileEntityBase base = (TileEntityBase)tile;
            if(!base.dropInv)
            {
                NBTTagCompound data = new NBTTagCompound();
                base.writeToNBT(data);

                List<String> keysToRemove = new ArrayList<>();
                for(String key : data.getKeySet()){
                    NBTBase tag = data.getTag(key);
                    if(tag instanceof NBTTagInt){
                        if(((NBTTagInt)tag).getInt() == 0){
                            keysToRemove.add(key);
                        }
                    }
                }
                for(String key : keysToRemove){
                    data.removeTag(key);
                }

                ItemStack stack = new ItemStack(this.getItemDropped(state, tile.getWorld().rand, fortune), 1, this.damageDropped(state));
                if(!data.hasNoTags()){
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setTag("Data", data);
                }

                drops.add(stack);
            }
        }else { super.getDrops(drops, world, pos, state, fortune); }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(this.shouldDropInventory(worldIn, pos)) { this.dropSlots(worldIn, pos); }

        super.breakBlock(worldIn, pos, state);
    }

    public boolean shouldDropInventory(World world, BlockPos pos) { return true; }
}
