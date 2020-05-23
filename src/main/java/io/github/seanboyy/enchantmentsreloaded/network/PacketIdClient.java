package io.github.seanboyy.enchantmentsreloaded.network;

public enum PacketIdClient implements IPacketId {
    ITEM_DECURSED,
    ENCHANTMENT_MODIFIED,
    ENCHANTMENT_TRANSFERRED;

    public static final PacketIdClient[] VALUES;

    static {
        VALUES = values();
    }
}
