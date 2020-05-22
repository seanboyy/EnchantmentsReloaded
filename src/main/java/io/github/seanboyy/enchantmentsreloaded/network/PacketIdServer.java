package io.github.seanboyy.enchantmentsreloaded.network;

public enum PacketIdServer implements IPacketId{
    ITEM_DECURSE_REQUEST,
    ENCHANTMENT_MODIFY_REQUEST;

    public static final PacketIdServer[] VALUES;

    static {
        VALUES = values();
    }
}
