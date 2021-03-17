package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class PacketItemCopied extends ModPacket {
    public PacketItemCopied(ItemStack copyableItem) {}

    @Override
    protected IPacketId getPacketId() {
        return PacketIdClient.ITEM_COPIED;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {

    }

    public static void readPacketData(PacketBuffer buf, PlayerEntity player) {

    }
}
