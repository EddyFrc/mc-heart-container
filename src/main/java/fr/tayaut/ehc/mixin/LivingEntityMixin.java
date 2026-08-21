package fr.tayaut.ehc.mixin;

import fr.tayaut.ehc.event.PlayerKillEntityCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "die")
    private void onDeath(DamageSource source, CallbackInfo ci) {
        // déclencher le callback PlayerKillEntityCallback à chaque fois qu'un joueur tue une entité quelconque
        if (source.getEntity() instanceof Player) {
            PlayerKillEntityCallback.EVENT.invoker().onKill(
                (LivingEntity) (Object) this, // evil mixin reinterpret cast magic
                (Player) source.getEntity()
            );
        }
    }
}
