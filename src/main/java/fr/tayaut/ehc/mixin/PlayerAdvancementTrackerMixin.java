package fr.tayaut.ehc.mixin;

import fr.tayaut.ehc.event.PlayerAdvancementCallback;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
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
@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {

    // On "Shadow" (récupère) le champ 'owner' pour savoir à quel joueur appartient ce tracker.
    @Shadow private ServerPlayerEntity owner;

    // On "Shadow" la méthode getProgress pour vérifier l'état du progrès.
    @Shadow public abstract AdvancementProgress getProgress(AdvancementEntry advancement);

    /**
     * Injection après l'exécution de la méthode (At RETURN).
     * On vérifie si l'opération a réussi et si le progrès est désormais complété.
     */
    @Inject(method = "grantCriterion", at = @At("RETURN"))
    private void onGrantCriterionReturn(AdvancementEntry advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        // On vérifie si l'appel à grantCriterion a retourné 'true'.
        // Cela signifie que quelque chose a changé (un critère a été validé).
        if (cir.getReturnValue()) {

            // Est-ce que le progrès est complètement terminé ?
            AdvancementProgress progress = this.getProgress(advancement);
            if (progress.isDone()) {

                // Déclencher l'event
                PlayerAdvancementCallback.EVENT.invoker().onAdvancement(this.owner, advancement);
            }
        }
    }
}