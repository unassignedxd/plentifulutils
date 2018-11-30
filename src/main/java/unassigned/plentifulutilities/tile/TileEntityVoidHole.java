package unassigned.plentifulutilities.tile;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import unassigned.plentifulutilities.utils.tasks.ExplosionTask;
import unassigned.plentifulutilities.voidenergy.base.IVoidStorage;
import unassigned.plentifulutilities.voidenergy.base.VoidStorage;
import unassigned.plentifulutilities.voidenergy.capability.CapabilityVoidEnergy;
import unassigned.plentifulutilities.voidenergy.capability.VoidEnergy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentifulutils/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/29/2018 for plentifulutils
 */
public class TileEntityVoidHole extends TileEntityBase {

    public ArrayList<ExplosionTask> tasks = Lists.newArrayList();

    int destroyTicks;

    public TileEntityVoidHole(){
        super("void_hole");
    }

    @Override
    public void update() {
        super.update();

        if(!world.isRemote)
        {
            Chunk chunkIn = world.getChunkFromBlockCoords(this.getPos());
            IVoidStorage storageIn = CapabilityVoidEnergy.getVoidEnergy(chunkIn);
            if(storageIn != null)
            {
                if(storageIn.getVoidStored() > CapabilityVoidEnergy.VoidHandler.DANGER_HIGH_THRESHOLD)
                {
                    handleFX(storageIn);
                }else {
                    destroyTicks++;

                    if(destroyTicks % 10 == 0)
                    {
                        ((WorldServer) world).spawnParticle(EnumParticleTypes.PORTAL, true, this.getPos().getX()+.5, this.getPos().getY()+1, this.getPos().getZ()+.5, 50, 0, 0, 0, 0.45D);
                    }

                    if(destroyTicks >= 140)
                    {
                        destroyEvent(storageIn);
                    }
                }
            }

            Iterator<ExplosionTask> iterator = tasks.iterator();
            while(iterator.hasNext())
            {
                ExplosionTask task = iterator.next();
                if(task.shouldFire(ticksAlive))
                {
                    world.newExplosion(null, task.spawnPos.getX(), task.spawnPos.getY(), task.spawnPos.getZ(), task.strength, false, true);
                    iterator.remove();
                }
            }
        }
    }

    public void destroyEvent(IVoidStorage storageIn) {
        world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ENDERDRAGON_HURT, SoundCategory.BLOCKS, 1f, 0.2f);
        ((WorldServer) world).spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, this.getPos().getX()+.5, this.getPos().getY()+1, this.getPos().getZ()+.5, 5, 0, 0, 0, 0.2D);
        ((WorldServer) world).spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.getPos().getX()+.5, this.getPos().getY()+1, this.getPos().getZ()+.5, 500, 0, 0, 0, 1D);
        ((VoidEnergy) storageIn).setDangerTicks(0);
        storageIn.extractVoid(storageIn.getVoidStored()/3, false);
        world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
    }

    public void handleFX(IVoidStorage storage){
        if(ticksAlive % 10 == 0)
        {
            ((WorldServer) world).spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, true, this.getPos().getX()+.5, this.getPos().getY()+1, this.getPos().getZ()+.5, 50, 0, 0, 0, 1D);

        }
        if(ticksAlive % 100 == 0)
        {
            int randX = this.pos.getX()+(world.rand.nextInt(8 + 1 + 8) - 8);
            int randZ = this.pos.getZ()+(world.rand.nextInt(8 + 1 + 8) - 8);
            int groundY = (world.getHeight(randX, randZ));
            int strength = world.rand.nextInt(5 + 1 - 2) + 2;

            ((WorldServer) world).spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, true, randX, groundY, randZ, 1000, 0, 0, 0, 3D);
            storage.extractVoid(strength*50, false);
            tasks.add(ExplosionTask.newTask(this.ticksAlive, 40, new BlockPos(randX, groundY, randZ), strength));
        }
    }
}
