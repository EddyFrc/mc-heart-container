package fr.tayaut.ehc.mixin;

import fr.tayaut.ehc.event.PlayerAdvancementCallback;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementTrackerMixin {

    @Shadow private ServerPlayer player;

    @Shadow public abstract AdvancementProgress getOrStartProgress(AdvancementHolder advancement);

    // trigger pour PlayerAdvancementCallback
    @Inject(method = "award", at = @At("RETURN"))
    private void onGrantCriterionReturn(AdvancementHolder advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        // true == état du critère modifié
        if (cir.getReturnValue()) {
            AdvancementProgress progress = this.getOrStartProgress(advancement);
            if (progress.isDone()) {
                PlayerAdvancementCallback.EVENT.invoker().onAdvancement(this.player, advancement);
            }
        }
    }
}