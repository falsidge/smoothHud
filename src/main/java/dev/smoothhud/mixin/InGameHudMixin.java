package dev.smoothhud.mixin;

import dev.smoothhud.Config;
import dev.smoothhud.SmoothHudMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
	@Unique
	private float currentX = 0;
	@Unique
	private long lastTickTime = 0;

	@Inject(
			method = "renderItemHotbar",
			at = @At("HEAD")
	)
	private void onRenderHotbar(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			float targetX = player.getInventory().selected * 20;

			long currentTime = System.currentTimeMillis();
			float deltaTime = (currentTime - lastTickTime) / 1000f;
			lastTickTime = currentTime;

			if (Math.abs(targetX - currentX) > 0.1f) {
				float diff = targetX - currentX;
				currentX += diff * deltaTime * Config.speed;
			}
		}
	}

	@ModifyArgs(
			method = "renderItemHotbar",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1)
	)
	private void mod(Args args) {
		int baseX = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 182) / 2 - 1;
		args.set(1, Math.round(baseX + currentX));
	}
}
