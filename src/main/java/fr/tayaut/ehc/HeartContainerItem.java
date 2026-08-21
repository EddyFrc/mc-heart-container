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

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide()) {
            return super.use(world, user, hand);
        }

        AttributeInstance userMaxHealth = user.getAttribute(Attributes.MAX_HEALTH);
        if (userMaxHealth == null) {
            // n'est pas supposer arriver parce qu'une LivingEntiety a toujours cette propriété en principe
            user.sendSystemMessage(Component.literal("An error occured, please contact the mod author and provide logs"));
            EddysHeartContainer.LOGGER.error("MAX_HEALTH du joueur est null, il faut investiguer");
            return super.use(world, user, hand);
        }

        // TODO: vie max data driven
        if (!(userMaxHealth.getBaseValue() < 40f)) {
            user.sendSystemMessage(Component.literal("You reached the limit for maximum health!"));
            EddysHeartContainer.LOGGER.debug("Nombre de coeurs maximal atteint pour {}", user.getName().tryCollapseToString());
            return super.use(world, user, hand);
        }

        user.getItemInHand(hand).shrink(1);
        // Modifie la valeur de base de santé maximale (+2 points = +1 coeur)
        userMaxHealth.setBaseValue(userMaxHealth.getBaseValue() + 2);
        user.setHealth((float) userMaxHealth.getValue());

        return super.use(world, user, hand);
    }
}
