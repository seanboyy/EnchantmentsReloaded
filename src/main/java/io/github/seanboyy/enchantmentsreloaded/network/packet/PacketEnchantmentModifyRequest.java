package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.inventory.container.EnchantmentCraftingTableContainer;
import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.Network;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdServer;
import io.github.seanboyy.enchantmentsreloaded.util.EnchantmentModifierType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class PacketEnchantmentModifyRequest extends ModPacket {
    public final EnchantmentModifierType modifierType;

    public PacketEnchantmentModifyRequest(EnchantmentModifierType modifierType) {
        this.modifierType = modifierType;
    }

    @Override
    protected IPacketId getPacketId() {
        return PacketIdServer.ENCHANTMENT_MODIFY_REQUEST;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {
        packetBuffer.writeEnumValue(modifierType);
    }

    public static void readPacketData(PacketBuffer buf, PlayerEntity playerEntity) {
        EnchantmentModifierType modifierType = buf.readEnumValue(EnchantmentModifierType.class);
        if(playerEntity instanceof ServerPlayerEntity) {
            if(((ServerPlayerEntity)playerEntity).openContainer instanceof EnchantmentCraftingTableContainer) {
                ((EnchantmentCraftingTableContainer)((ServerPlayerEntity)playerEntity).openContainer).performModification(playerEntity, modifierType);
                PacketEnchantmentModified packetEnchantmentModified = new PacketEnchantmentModified(modifierType);
                Network.sendPacketToClient(packetEnchantmentModified, (ServerPlayerEntity)playerEntity);
            }
        }
    }
}
