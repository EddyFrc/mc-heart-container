package fr.tayaut.ehc;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class Util {
    public static void giveItemStack(@NonNull ServerPlayerEntity player, ItemStack itemStack) {
        // en gros là on regarde juste si on peut donner l'item au joueur
        // (si l'inventaire n'est pas plein et pas déjà d'item dedans)
        if (player.getInventory().getOccupiedSlotWithRoomForStack(itemStack) == -1
            && player.getInventory().getEmptySlot() == -1) {
            player.dropItem(itemStack, false);
        } else {
            player.getInventory().insertStack(itemStack);
        }
    }
}
