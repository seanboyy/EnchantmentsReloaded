package io.github.seanboyy.enchantmentsreloaded.network;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.network.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumMap;

public class PacketHandlerClient {
    public final EnumMap<PacketIdClient, IPacketHandler> clientHandlers = new EnumMap<>(PacketIdClient.class);

    public PacketHandlerClient() {
        clientHandlers.put(PacketIdClient.ITEM_DECURSED, PacketItemDecursed::readPacketData);
        clientHandlers.put(PacketIdClient.ENCHANTMENT_MODIFIED, PacketEnchantmentModified::readPacketData);
        clientHandlers.put(PacketIdClient.ENCHANTMENT_TRANSFERRED, PacketEnchantmentTransfer::readPacketData);
        clientHandlers.put(PacketIdClient.ITEM_COPIED, PacketItemCopied::readPacketData);
    }

    public void onPacket(NetworkEvent.ServerCustomPayloadEvent event) {
        try {
            PacketBuffer packetBuffer = new PacketBuffer(event.getPayload());
            int packetIdOrdinal = packetBuffer.readByte();
            PacketIdClient packetId = PacketIdClient.VALUES[packetIdOrdinal];
            IPacketHandler packetHandler = this.clientHandlers.get(packetId);
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntity clientPlayerEntity = minecraft.player;
            if(clientPlayerEntity != null ){
                packetHandler.readPacketData(packetBuffer, clientPlayerEntity);
            }
        } catch (Exception e) {
            EnchantmentsReloaded.LOGGER.error("Packet error", e);
        }
        event.getSource().get().setPacketHandled(true);
    }
}
