package fr.tayaut.ehc;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class HeartContainerItem extends Item {

    public HeartContainerItem(Properties settings) {
        super(settings);
    }

    /**
     * Utilise le réceptacle de coeur
     * @param world the world the item was used in
     * @param user the player who used the item
     * @param hand the hand used
     * @return ActionResult
     */
    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {

        if (!world.isClientSide()) {
            AttributeInstance userMaxHealth = user.getAttribute(Attributes.MAX_HEALTH);
            if (userMaxHealth != null) {
                // Check si le nombre max de coeurs est déjà atteint
                if (userMaxHealth.getBaseValue() < 40f) {
                    // Supprime l'objet de l'inventaire (utilisation)
                    user.getItemInHand(hand).shrink(1);
                    // Modifie la valeur de base de santé maximale (+2 points = +1 coeur)
                    userMaxHealth.setBaseValue(userMaxHealth.getBaseValue() + 2);
                    user.setHealth((float) userMaxHealth.getValue());
                } else {
                    // Si le nombre max est atteint, l'utilisation de l'objet ne fait rien
                    user.sendSystemMessage(Component.literal("Nombre de coeurs maximal atteint"));
                    EddysHeartContainer.LOGGER.debug("Nombre de coeurs maximal atteint pour {}", user.getName().tryCollapseToString());
                }
            } else {
                // On n'est pas supposé arriver ici parce qu'une entité vivante a toujours cette propriété en principe
                user.sendSystemMessage(Component.literal("Une erreur s'est produite, merci de contacter l'auteur du mod"));
                EddysHeartContainer.LOGGER.error("MAX_HEALTH du joueur est null, il faut investiguer");
            }
        }

        return super.use(world, user, hand);
    }
}
