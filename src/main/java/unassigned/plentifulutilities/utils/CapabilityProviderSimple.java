package unassigned.plentifulutilities.utils;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

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
public class CapabilityProviderSimple<HANDLER> implements ICapabilityProvider {

    protected final Capability<HANDLER> capability;

    protected final EnumFacing facing;

    protected final HANDLER instance;

    public CapabilityProviderSimple(final HANDLER instance, final Capability<HANDLER> capability, @Nullable final EnumFacing facing) {
        this.instance = instance;
        this.capability = capability;
        this.facing = facing;
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == getCapability();
    }

    @Override
    @Nullable
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == getCapability()) {
            return getCapability().cast(getInstance());
        }

        return null;
    }

    public final Capability<HANDLER> getCapability() {
        return capability;
    }

    @Nullable
    public EnumFacing getFacing() {
        return facing;
    }

    public final HANDLER getInstance() {
        return instance;
    }
}