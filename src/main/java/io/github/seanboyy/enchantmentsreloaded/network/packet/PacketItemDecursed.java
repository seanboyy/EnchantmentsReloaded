package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public class PacketItemDecursed extends ModPacket {
    public PacketItemDecursed() {}

    @Override
    public IPacketId getPacketId() {
        return PacketIdClient.ITEM_DECURSED;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {

    }

    public static void readPacketData(PacketBuffer buf, PlayerEntity player) {

    }
}
