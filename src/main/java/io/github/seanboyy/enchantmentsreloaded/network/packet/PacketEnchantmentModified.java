package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdClient;
import io.github.seanboyy.enchantmentsreloaded.util.EnchantmentModifierType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public class PacketEnchantmentModified extends ModPacket{
    public final EnchantmentModifierType modifierType;

    public PacketEnchantmentModified(EnchantmentModifierType modifierType) {
        this.modifierType = modifierType;
    }

    @Override
    protected IPacketId getPacketId() {
        return PacketIdClient.ENCHANTMENT_MODIFIED;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {
        packetBuffer.writeEnumValue(modifierType);
    }

    public static void readPacketData(PacketBuffer buf, PlayerEntity player) {
        EnchantmentModifierType modifierType = buf.readEnumValue(EnchantmentModifierType.class);
    }
}
