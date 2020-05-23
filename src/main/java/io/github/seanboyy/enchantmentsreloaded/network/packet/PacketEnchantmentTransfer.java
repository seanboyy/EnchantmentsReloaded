package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public class PacketEnchantmentTransfer extends ModPacket {
    public PacketEnchantmentTransfer() {}

    @Override
    protected IPacketId getPacketId() {
        return PacketIdClient.ENCHANTMENT_TRANSFERRED;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {}

    public static void readPacketData(PacketBuffer buf, PlayerEntity player) {}
}
