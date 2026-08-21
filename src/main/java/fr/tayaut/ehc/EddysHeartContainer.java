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
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EddysHeartContainer implements ModInitializer {

    public static final String MOD_ID = "ehc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_50_PERCENT = new ArrayList<>(List.of(
        BuiltInLootTables.BASTION_TREASURE.identifier()
    ));

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_20_PERCENT = new ArrayList<>(List.of(
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

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_10_PERCENT = new ArrayList<>(List.of(
        BuiltInLootTables.ANCIENT_CITY.identifier(),
        BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON.identifier()
    ));

    private static final ArrayList<String> HEART_CONTAINER_REWARD_ADVANCEMENTS = new ArrayList<>(List.of(
        "minecraft:adventure/hero_of_the_village"
    ));

    private static final ArrayList<String> HEART_PIECE_REWARD_ADVANCEMENTS = new ArrayList<>(List.of(
        "minecraft:nether/all_potions",
        "minecraft:adventure/bullseye",
        "minecraft:adventure/adventuring_time",
        "minecraft:adventure/kill_all_mobs",
        "adventure/two_birds_one_arrow",
        "husbandry/complete_catalogue",
        "husbandry/bred_all_animals",
        "husbandry/balanced_diet",
        "husbandry/whole_pack"
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

        PlayerAdvancementCallback.EVENT.register((player, advancement) -> {
            String advancementId = advancement.id().toString();

            if (HEART_CONTAINER_REWARD_ADVANCEMENTS.contains(advancementId)) {
                Util.giveItemStack(player, new ItemStack(ModItems.HEART_CONTAINER));

            } else if (HEART_PIECE_REWARD_ADVANCEMENTS.contains(advancementId)) {
                Util.giveItemStack(player, new ItemStack(ModItems.HEART_PIECE));
            }
        });

        PlayerKillEntityCallback.EVENT.register((entity, player) -> {
            IEntityDataSaver saver = (IEntityDataSaver) player;
            // les ender dragon, wither, elder guardian et warden donnent tous un réceptacle la première fois qu'ils sont tués (une fois par joueur)
            if (entity instanceof EnderDragon && !saver.ehc$onDragonKilled()
                || entity instanceof WitherBoss && !saver.ehc$onWitherKilled()
                || entity instanceof ElderGuardian && !saver.ehc$onElderGuardianKilled()
                || entity instanceof Warden && !saver.ehc$onWardenKilled()) {

                ItemStack heartContainer = new ItemStack(ModItems.HEART_CONTAINER);
                player.level().addFreshEntity(new ItemEntity(player.level(), entity.getX(), entity.getY(), entity.getZ(), heartContainer));
            }
        });

        LOGGER.info("Hello, this is Eddy's Heart Container mod, everything seems to work fine :)");
    }

    private static void addHeartPieceItemToLoot(LootTable.Builder tableBuilder, float chance) {
        LootPool.Builder poolBuilder = LootPool.lootPool()
            .when(LootItemRandomChanceCondition.randomChance(chance))
            .add(LootItem.lootTableItem(ModItems.HEART_PIECE));

        tableBuilder.withPool(poolBuilder);
    }
}
