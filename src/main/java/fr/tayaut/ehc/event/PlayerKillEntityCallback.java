package fr.tayaut.ehc.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;


@FunctionalInterface
public interface PlayerKillEntityCallback {

    /**
     * Déclenché quand un joueur tue un mob
     */
    Event<PlayerKillEntityCallback> EVENT = EventFactory.createArrayBacked(PlayerKillEntityCallback.class,
            (listeners) -> (entity, player) -> {
                for (PlayerKillEntityCallback listener : listeners) {
                    listener.onKill(entity, player);
                }
            });

    /**
     * La méthode qui sera appelée quand l'événement survient.
     *
     * @param entity L'entité qui a été tuée. Ne peut pas être nulle.
     * @param player Le joueur qui a tué l'entité. Peut être nul si le tueur n'est pas un joueur.
     */
    void onKill(LivingEntity entity, PlayerEntity player);
}
