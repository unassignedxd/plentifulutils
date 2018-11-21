package unassigned.plentifulutilities.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/20/2018 for plentifulutils
 */
public final class TileUtil {

    public static void doEnergyInteraction(TileEntity tileFrom, TileEntity tileTo, EnumFacing sideTo, int maxTransfer) {
        if(maxTransfer > 0)
        {
            EnumFacing opp = sideTo.getOpposite();
            IEnergyStorage handlerFrom = tileFrom.getCapability(CapabilityEnergy.ENERGY, sideTo);
            IEnergyStorage handlerTo = tileTo.getCapability(CapabilityEnergy.ENERGY, opp);
            if(handlerFrom != null && handlerTo != null)
            {
                int drainAmt = handlerFrom.extractEnergy(maxTransfer, true);
                if(drainAmt > 0)
                {
                    int fill = handlerTo.receiveEnergy(drainAmt, false);
                    handlerFrom.extractEnergy(fill, false);
                }
            }
        }
    }
}
