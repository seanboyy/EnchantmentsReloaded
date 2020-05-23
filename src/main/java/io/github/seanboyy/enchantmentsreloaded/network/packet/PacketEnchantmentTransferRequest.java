package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.inventory.container.TransferTableContainer;
import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.Network;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class PacketEnchantmentTransferRequest extends ModPacket{
    public PacketEnchantmentTransferRequest() {}

    @Override
    protected IPacketId getPacketId() {
        return PacketIdServer.ENCHANTMENT_TRANSFERRED_REQUEST;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {}

    public static void readPacketData(PacketBuffer buf, PlayerEntity playerEntity){
        if(playerEntity instanceof ServerPlayerEntity) {
            if(((ServerPlayerEntity)playerEntity).openContainer instanceof TransferTableContainer) {
                ((TransferTableContainer)((ServerPlayerEntity)playerEntity).openContainer).transferEnchantments(playerEntity);
                PacketEnchantmentTransfer packetEnchantmentTransfer = new PacketEnchantmentTransfer();
                Network.sendPacketToClient(packetEnchantmentTransfer, (ServerPlayerEntity)playerEntity);
            }
        }
    }
}
