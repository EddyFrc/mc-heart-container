package fr.tayaut.ehc.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface de rappel (callback) pour l'événement où un joueur tue une autre entité vivante.
 * Les mods peuvent implémenter cette interface et s'enregistrer pour recevoir des notifications.
 */
@FunctionalInterface
public interface PlayerKillEntityCallback {

    /**
     * L'événement est déclenché lorsqu'un joueur tue une entité.
     * <p>
     * L'événement est créé en utilisant EventFactory, ce qui permet à plusieurs mods de s'y abonner
     * sans conflit. Chaque callback enregistré sera appelé séquentiellement.
     */
    Event<PlayerKillEntityCallback> EVENT = EventFactory.createArrayBacked(PlayerKillEntityCallback.class,
            (listeners) -> (entity, player) -> {
                for (PlayerKillEntityCallback listener : listeners) {
                    listener.onKill(entity, player);
                }
            });

    /**
     * Méthode appelée lorsqu'une entité est tuée par un joueur.
     *
     * @param entity L'entité qui a été tuée. Ne peut pas être nulle.
     * @param player Le joueur qui a tué l'entité. Peut être nul si le tueur n'est pas un joueur.
     */
    void onKill(LivingEntity entity, PlayerEntity player);
}
