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

/**
 * Ce Mixin cible la classe 'PlayerAdvancementTracker'.
 * C'est la classe interne de Minecraft qui stocke quels progrès un joueur a obtenus.
 * Chaque joueur connecté possède une instance de cette classe.
 */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementTrackerMixin {

    // On "Shadow" (récupère) le champ 'owner' pour savoir à quel joueur appartient ce tracker.
    @Shadow private ServerPlayer player;

    // On "Shadow" la méthode getProgress pour vérifier l'état du progrès.
    @Shadow public abstract AdvancementProgress getOrStartProgress(AdvancementHolder advancement);

    /**
     * Injection après l'exécution de la méthode (At RETURN).
     * On vérifie si l'opération a réussi et si le progrès est désormais complété.
     */
    @Inject(method = "award", at = @At("RETURN"))
    private void onGrantCriterionReturn(AdvancementHolder advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        // On vérifie si l'appel à grantCriterion a retourné 'true'.
        // Cela signifie que quelque chose a changé (un critère a été validé).
        if (cir.getReturnValue()) {

            // Est-ce que le progrès est complètement terminé ?
            AdvancementProgress progress = this.getOrStartProgress(advancement);
            if (progress.isDone()) {

                // Déclencher l'event
                PlayerAdvancementCallback.EVENT.invoker().onAdvancement(this.player, advancement);
            }
        }
    }
}