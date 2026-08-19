package fr.tayaut.ehc;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class Util {
    public static void giveItemStack(@NonNull ServerPlayer player, ItemStack itemStack) {
        // en gros là on regarde juste si on peut donner l'item au joueur
        // (si l'inventaire n'est pas plein et pas déjà d'item dedans)
        if (player.getInventory().getSlotWithRemainingSpace(itemStack) == -1
            && player.getInventory().getFreeSlot() == -1) {
            player.drop(itemStack, false);
        } else {
            player.getInventory().add(itemStack);
        }
    }
}
