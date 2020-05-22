package io.github.seanboyy.enchantmentsreloaded.network.packet;

import io.github.seanboyy.enchantmentsreloaded.network.IPacketId;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.network.PacketBuffer;

public abstract class ModPacket {
    public final Pair<PacketBuffer, Integer> getPacketData() {
        IPacketId packetId = getPacketId();
        int packetIdOrdinal = packetId.ordinal();
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeByte(packetIdOrdinal);
        writePacketData(packetBuffer);
        return Pair.of(packetBuffer, packetIdOrdinal);
    }

    protected abstract IPacketId getPacketId();

    protected abstract void writePacketData(PacketBuffer packetBuffer);
}
