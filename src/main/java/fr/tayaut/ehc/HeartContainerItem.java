package fr.tayaut.ehc;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.*;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class HeartContainerItem extends Item {

    public HeartContainerItem(Settings settings) {
        super(settings);
    }

    /**
     * Utilise le réceptacle de coeur
     * @param world the world the item was used in
     * @param user the player who used the item
     * @param hand the hand used
     * @return
     */
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient()) {
            EntityAttributeInstance userMaxHealth = user.getAttributeInstance(EntityAttributes.MAX_HEALTH);
            if (userMaxHealth != null) {
                // Check si le nombre max de coeurs est déjà atteint
                if (userMaxHealth.getBaseValue() < 40f) {
                    // Supprime l'objet de l'inventaire (utilisation)
                    user.getStackInHand(hand).decrement(1);
                    // Modifie la valeur de base de santé maximale (+2 points = +1 coeur)
                    userMaxHealth.setBaseValue(userMaxHealth.getBaseValue() + 2);
                    user.setHealth((float) userMaxHealth.getValue());
                } else {
                    // Si le nombre max est atteint, l'utilisation de l'objet ne fait rien
                    user.sendMessage(Text.literal("Nombre de coeurs maximal atteint"), true);
                    EddysHeartContainer.LOGGER.debug("Nombre de coeurs maximal atteint pour {}", user.getName().getLiteralString());
                }
            } else {
                // On n'est pas supposé arriver ici parce qu'une entité vivante a toujours cette propriété en principe
                user.sendMessage(Text.literal("Une erreur s'est produite, merci de contacter l'auteur du mod"), true);
                EddysHeartContainer.LOGGER.error("MAX_HEALTH du joueur est null, il faut investiguer");
            }
        }

        return super.use(world, user, hand);
    }
}
