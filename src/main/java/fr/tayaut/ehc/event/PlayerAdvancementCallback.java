package fr.tayaut.ehc.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Interface fonctionnelle définissant le callback pour l'obtention d'un progrès.
 * C'est cette interface que tu vas implémenter via une lambda pour exécuter ton script.
 */
@FunctionalInterface
public interface PlayerAdvancementCallback {

    /**
     * L'objet EVENT est le point d'entrée pour s'enregistrer.
     * On utilise l'API Event de Fabric qui est thread-safe et performante.
     * * Le EventFactory.createArrayBacked permet de gérer plusieurs abonnés (listeners).
     * Si plusieurs mods ou scripts s'abonnent, ils seront tous exécutés séquentiellement.
     */
    Event<PlayerAdvancementCallback> EVENT = EventFactory.createArrayBacked(PlayerAdvancementCallback.class,
        (listeners) -> (player, advancement) -> {
            for (PlayerAdvancementCallback listener : listeners) {
                listener.onAdvancement(player, advancement);
            }
        });

    /**
     * La méthode qui sera appelée quand l'événement survient.
     *
     * @param player Le joueur qui vient de débloquer le progrès (Côté Serveur).
     * @param advancement L'objet AdvancementEntry contenant l'ID et les données du progrès.
     */
    void onAdvancement(ServerPlayerEntity player, AdvancementEntry advancement);
}