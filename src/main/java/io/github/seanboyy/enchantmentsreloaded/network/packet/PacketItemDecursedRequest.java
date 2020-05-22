package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.inventory.container.CursebreakerContainer;
import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.github.seanboyy.enchantmentsreloaded.network.Network;
import io.github.seanboyy.enchantmentsreloaded.network.PacketIdServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

public class PacketItemDecursedRequest extends ModPacket{
    public PacketItemDecursedRequest(){}

    public static void readPacketData(PacketBuffer packetBuffer, PlayerEntity playerEntity) {
        if(playerEntity instanceof ServerPlayerEntity) {
            if(((ServerPlayerEntity)playerEntity).openContainer instanceof CursebreakerContainer) {
                ((CursebreakerContainer)((ServerPlayerEntity)playerEntity).openContainer).removeCurses(playerEntity);
                PacketItemDecursed packetItemDecursed = new PacketItemDecursed();
                Network.sendPacketToClient(packetItemDecursed, (ServerPlayerEntity)playerEntity);
            }
        }
    }

    @Override
    protected IPacketId getPacketId() {
        return PacketIdServer.ITEM_DECURSE_REQUEST;
    }

    @Override
    protected void writePacketData(PacketBuffer packetBuffer) {

    }
}
