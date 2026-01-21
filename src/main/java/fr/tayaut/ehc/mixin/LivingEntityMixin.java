package fr.tayaut.ehc.mixin;

import fr.tayaut.ehc.event.PlayerKillEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ce mixin a pour but d'injecter du code dans la méthode `onDeath` pour déclencher l'event PlayerKillEntityCallback
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /**
     * Injection dans la méthode `onDeath`
     *
     * @param source La source des dégâts qui ont causé la mort.
     * @param ci Les informations de rappel (callback info).
     */
    @Inject(at = @At("HEAD"), method = "onDeath")
    private void onDeath(DamageSource source, CallbackInfo ci) {
        // On récupère l'attaquant depuis la source des dégâts.
        // La source des dégâts contient de nombreuses informations, dont l'entité qui est à l'origine des dégâts.
        if (source.getAttacker() instanceof PlayerEntity) {
            // Si l'attaquant est une instance de PlayerEntity, cela signifie qu'un joueur a tué l'entité.

            // On "cast" (convertit) l'attaquant en PlayerEntity pour pouvoir le passer à notre événement.
            PlayerEntity player = (PlayerEntity) source.getAttacker();

            // On "cast" (convertit) l'instance actuelle de LivingEntity (qui est `this` dans ce contexte)
            // pour la passer à notre événement.
            LivingEntity entity = (LivingEntity) (Object) this;

            // On déclenche l'événement.
            // Tous les callbacks enregistrés sur `PlayerKillEntityCallback.EVENT` seront appelés.
            PlayerKillEntityCallback.EVENT.invoker().onKill(entity, player);
        }
    }
}
