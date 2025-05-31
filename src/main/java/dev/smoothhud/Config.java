package dev.smoothhud;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

//
//public class Config {
//    public float speed = 25.0f;
//}


@EventBusSubscriber(modid = SmoothHudMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue SPEED_VALUE = BUILDER
            .comment("Speed of SmoothHud scrolling")
            .defineInRange("speed", 42, 0, Double.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static float speed;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        speed = SPEED_VALUE.get().floatValue();
    }
}