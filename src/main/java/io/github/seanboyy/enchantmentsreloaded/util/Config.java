package io.github.seanboyy.enchantmentsreloaded.util;

import io.github.seanboyy.enchantmentsreloaded.EnchantmentsReloaded;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static float curse_removal_durability_factor;
    public static boolean curse_removal_durability_rounding_style;

    public static void bakeConfig() {
        curse_removal_durability_factor = CLIENT.CURSE_DURABILITY_FACTOR.get();
        curse_removal_durability_rounding_style = CLIENT.CURSE_ROUNDING_STYLE.get();
    }

    public static final class ClientConfig {
        public final ForgeConfigSpec.ConfigValue<Float> CURSE_DURABILITY_FACTOR;
        public final ForgeConfigSpec.ConfigValue<Boolean> CURSE_ROUNDING_STYLE;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            CURSE_DURABILITY_FACTOR = builder.comment("What percent of durability should removing a curse use?")
                    .translation(EnchantmentsReloaded.MOD_ID + ".config." + "curse_durability_factor")
                    .define("curse_durability_factor", 0.3333333333333F);
            CURSE_ROUNDING_STYLE = builder.comment("When decreasing durability from removing curses, should we round the result of multiplication up?")
                    .translation(EnchantmentsReloaded.MOD_ID + ".config." + "curse_durability_rounding_style")
                    .define("curse_durability_rounding_style", true);
        }
    }
}
