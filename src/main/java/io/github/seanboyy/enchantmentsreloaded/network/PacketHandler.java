package io.github.seanboyy.enchantmentsreloaded.network;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import io.github.seanboyy.enchantmentsreloaded.network.packet.IPacketHandler;
import io.github.seanboyy.enchantmentsreloaded.network.packet.PacketEnchantmentModifyRequest;
import io.github.seanboyy.enchantmentsreloaded.network.packet.PacketItemDecursedRequest;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumMap;

public class PacketHandler {
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(EnchantmentsReloaded.MOD_ID, "channel");

    public final EnumMap<PacketIdServer, IPacketHandler> serverHandlers = new EnumMap<>(PacketIdServer.class);

    public PacketHandler() {
        serverHandlers.put(PacketIdServer.ITEM_DECURSE_REQUEST, PacketItemDecursedRequest::readPacketData);
        serverHandlers.put(PacketIdServer.ENCHANTMENT_MODIFY_REQUEST, PacketEnchantmentModifyRequest::readPacketData);
    }

    public void onPacket(NetworkEvent.ClientCustomPayloadEvent event) {
        PacketBuffer packetBuffer = new PacketBuffer(event.getPayload());
        NetworkEvent.Context context = event.getSource().get();
        ServerPlayerEntity player = context.getSender();
        if(player == null) {
            EnchantmentsReloaded.LOGGER.error("Packet error, the sending player is missing for event: {}", event);
            return;
        }
        try {
            int packetIdOrdinal = packetBuffer.readByte();
            PacketIdServer packetId = PacketIdServer.VALUES[packetIdOrdinal];
            IPacketHandler packetHandler = this.serverHandlers.get(packetId);
            packetHandler.readPacketData(packetBuffer, player);
        } catch (RuntimeException e) {
            EnchantmentsReloaded.LOGGER.error("Packet error for event: {}", event, e);
        }
        event.getSource().get().setPacketHandled(true);
    }
}