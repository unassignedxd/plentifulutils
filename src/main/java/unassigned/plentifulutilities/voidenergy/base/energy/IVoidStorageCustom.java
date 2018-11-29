package unassigned.plentifulutilities.voidenergy.base.energy;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
public interface IVoidStorageCustom {

    int receiveVoid(int maxReceive, boolean simulate);

    int extractVoid(int maxExtract, boolean simulate);

    int getVoidStored();

    int getMaxVoidStored();
}
