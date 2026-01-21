package fr.tayaut.ehc.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface PlayerAdvancementCallback {

    /**
     * Déclenché quand un joueur débloque un progrès
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