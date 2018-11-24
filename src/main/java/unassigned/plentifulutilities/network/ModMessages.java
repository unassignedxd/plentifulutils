package unassigned.plentifulutilities.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;
import unassigned.plentifulutilities.PlentifulUtilities;

/**
 * This code is under the GNU General Public License v3.0
 * You can see more licensing information at:
 * https://github.com/unassignedxd/plentiful-utilities/blob/master/LICENSE
 * ---
 * Copyright © 2018-2019 unassigned
 * ---
 * Created on 11/23/2018 for plentifulutils
 */
public class ModMessages {

    private static int messageID = 1;

    public static void registerMessages() {
        registerMessage(MessageUpdateVoidValue.Handler.class, MessageUpdateVoidValue.class, Side.CLIENT);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ,REPLY>> messageHandler, Class<REQ> requestMessageType, Side receivingSide){
        PlentifulUtilities.network.registerMessage(messageHandler, requestMessageType, messageID++, receivingSide);
    }
}
