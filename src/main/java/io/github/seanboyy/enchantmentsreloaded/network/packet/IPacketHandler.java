package io.github.seanboyy.enchantmentsreloaded.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public interface IPacketHandler {
    void readPacketData(PacketBuffer packetBuffer, PlayerEntity playerEntity);
}
