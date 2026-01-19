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
     * On injecte notre code DANS la méthode 'grantCriterion'.
     * C'est la méthode appelée par Minecraft chaque fois qu'un critère de progrès est rempli
     * (ex: "miner de la pierre" pour le progrès "L'âge de pierre").
     *
     * CHOIX TECHNIQUE : @Inject au début (HEAD) et à la fin (RETURN) ?
     * Non, ici on utilise une stratégie plus fine. On veut déclencher l'événement UNIQUEMENT
     * si le progrès vient de passer de "non terminé" à "terminé".
     *
     * Si on déclenchait à chaque critère, l'événement spammerait pour les progrès complexes.
     */
    @Inject(method = "grantCriterion", at = @At("HEAD"))
    private void onGrantCriterionHead(AdvancementEntry advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        // Cette injection est une astuce technique.
        // Pour savoir si le progrès VIENT d'être fini, il faut comparer l'état AVANT et APRÈS.
        // Cependant, faire passer des variables locales entre deux injections est complexe.
        //
        // Une approche plus simple et pragmatique consiste à vérifier l'état après l'exécution.
        // Voir la méthode ci-dessous.
    }

    /**
     * Injection après l'exécution de la méthode (At RETURN).
     * On vérifie si l'opération a réussi et si le progrès est désormais complété.
     */
    @Inject(method = "grantCriterion", at = @At("RETURN"))
    private void onGrantCriterionReturn(AdvancementEntry advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        // 1. On vérifie si l'appel à grantCriterion a retourné 'true'.
        // Cela signifie que quelque chose a changé (un critère a été validé).
        if (cir.getReturnValue()) {

            // 2. On récupère l'objet progression pour cet advancement spécifique.
            AdvancementProgress progress = this.getProgress(advancement);

            // 3. LA CONDITION CLÉ :
            // Est-ce que le progrès est COMPLÈTEMENT terminé (isDone) ?
            // Et petite subtilité : pour éviter les doublons (si un mod force le progrès plusieurs fois),
            // on s'assure que c'est bien le moment où il se termine.
            // Note : Dans grantCriterion, si c'est 'true', c'est que le progrès a avancé.
            // Si progress.isDone() est vrai, c'est que c'est terminé.
            // Minecraft gère déjà le fait de ne pas re-valider un critère déjà acquis.

            if (progress.isDone()) {

                // 4. On déclenche notre événement personnalisé.
                // On passe le joueur (owner) et l'avancement concerné.
                // Tous les mods abonnés recevront cette notification.
                PlayerAdvancementCallback.EVENT.invoker().onAdvancement(this.owner, advancement);
            }
        }
    }
}