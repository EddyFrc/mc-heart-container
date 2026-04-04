package fr.tayaut.ehc;

import fr.tayaut.ehc.data.IEntityDataSaver;
import fr.tayaut.ehc.event.PlayerAdvancementCallback;
import fr.tayaut.ehc.event.PlayerKillEntityCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EddysHeartContainer implements ModInitializer {

    public static final String MOD_ID = "ehc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_50_PERCENT = new ArrayList<>(Collections.singleton(
        BuiltInLootTables.BASTION_TREASURE.identifier()
    ));

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_20_PERCENT = new ArrayList<>(Arrays.asList(
        BuiltInLootTables.STRONGHOLD_CROSSING.identifier(),
        BuiltInLootTables.STRONGHOLD_CORRIDOR.identifier(),
        BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY.identifier(),
        BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY.identifier(),
        BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE.identifier(),
        BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY.identifier(),
        BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY.identifier(),
        BuiltInLootTables.ANCIENT_CITY_ICE_BOX.identifier(),
        BuiltInLootTables.BASTION_TREASURE.identifier(),
        BuiltInLootTables.DESERT_PYRAMID.identifier(),
        BuiltInLootTables.JUNGLE_TEMPLE.identifier(),
        BuiltInLootTables.WOODLAND_MANSION.identifier(),
        BuiltInLootTables.END_CITY_TREASURE.identifier()
    ));

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_10_PERCENT = new ArrayList<>(Arrays.asList(
        BuiltInLootTables.ANCIENT_CITY.identifier(),
        BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON.identifier()
    ));

    @Override
    public void onInitialize() {
        ModItems.initialize();

        LootTableEvents.MODIFY.register((registryKey, tableBuilder, source, wrapperLookup) -> {
            if (LOOT_TABLE_IDS_50_PERCENT.contains(registryKey.identifier())) {
                addHeartPieceItemToLoot(tableBuilder, 0.5f);
            } else if (LOOT_TABLE_IDS_20_PERCENT.contains(registryKey.identifier())) {
                addHeartPieceItemToLoot(tableBuilder, 0.2f);
            } else if (LOOT_TABLE_IDS_10_PERCENT.contains(registryKey.identifier())) {
                addHeartPieceItemToLoot(tableBuilder, 0.1f);
            }
        });

        // enregistrement du callback "progrès"
        PlayerAdvancementCallback.EVENT.register((player, advancement) -> {

            // Récupérer l'ID de l'achievement (ex: "minecraft:story/mine_stone")
            String advancementId = advancement.id().toString();
            if (advancementId.equals("minecraft:adventure/hero_of_the_village")
                || advancementId.equals("minecraft:nether/all_potions")
                || advancementId.equals("minecraft:adventure/adventuring_time")
                || advancementId.equals("minecraft:adventure/kill_all_mobs")) {

                ItemStack heartContainer = new ItemStack(ModItems.HEART_CONTAINER);
                // en gros là on regarde juste si on peut donner l'item au joueur
                // (si l'inventaire n'est pas plein et pas déjà de heart container dedans)
                if (player.getInventory().getSlotWithRemainingSpace(heartContainer) == -1  // non je copie absolument pas le code de mojang :)
                    && player.getInventory().getFreeSlot() == -1) {
                    // Les deux renvoient -1 -> pas possible de give, on va juste drop l'item par terre
                    player.drop(heartContainer, false);
                } else {
                    // un des deux renvoit un emplacement valide : tout est ok, donnons l'item au joueur
                    player.getInventory().add(heartContainer);
                }

            }
        });

        // enregistrement du callback "entité morte"
        PlayerKillEntityCallback.EVENT.register(((entity, player) -> {
            IEntityDataSaver saver = (IEntityDataSaver) player;
            // si l'entité qui est morte est un dragon et qu'il n'a pas encore été tué par ce joueur
            if (entity instanceof EnderDragon && !saver.ehc$onDragonKilled()
                // ou alors un wither, etc.
                || entity instanceof WitherBoss && !saver.ehc$onWitherKilled()
                || entity instanceof ElderGuardian && !saver.ehc$onElderGuardianKilled()) {
                // faire spawn un réceptacle de coeur sur le mob
                ItemStack heartContainer = new ItemStack(ModItems.HEART_CONTAINER);
                player.level().addFreshEntity(new ItemEntity(player.level(), entity.getX(), entity.getY(), entity.getZ(), heartContainer));
            }
        }));

        LOGGER.info("Hello, this is Eddy's Heart Container mod, everything seems to work fine :)");
    }

    private static void addHeartPieceItemToLoot(LootTable.Builder tableBuilder, float chance) {
        LootPool.Builder poolBuilder = LootPool.lootPool()
            .when(LootItemRandomChanceCondition.randomChance(chance))
            .add(LootItem.lootTableItem(ModItems.HEART_PIECE));

        tableBuilder.withPool(poolBuilder);
    }
}
