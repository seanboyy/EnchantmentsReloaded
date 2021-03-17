package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.inventory.container.CopyTableContainer;
import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.Network;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class PacketItemCopiedRequest extends ModPacket{
    public final ItemStack copyableItem;

    public PacketItemCopiedRequest(ItemStack copyableItem){
        this.copyableItem = copyableItem.copy();
    }

    @Override
    protected IPacketId getPacketId() {
        return PacketIdServer.ITEM_COPIED_REQUEST;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {
        packetBuffer.writeItemStack(copyableItem);
    }

    public static void readPacketData(PacketBuffer packetBuffer, PlayerEntity playerEntity) {
        ItemStack copyableItem = packetBuffer.readItemStack();
        if(playerEntity instanceof ServerPlayerEntity) {
            if(playerEntity.openContainer instanceof CopyTableContainer) {
                ((CopyTableContainer)playerEntity.openContainer).copyItem(playerEntity, copyableItem);
                PacketItemCopied packetItemCopied = new PacketItemCopied(copyableItem);
                Network.sendPacketToClient(packetItemCopied, (ServerPlayerEntity)playerEntity);
            }
        }
    }
}
