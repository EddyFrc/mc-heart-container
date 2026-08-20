package fr.tayaut.ehc;

import fr.tayaut.ehc.data.IEntityDataSaver;
import fr.tayaut.ehc.event.PlayerAdvancementCallback;
import fr.tayaut.ehc.event.PlayerKillEntityCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EddysHeartContainer implements ModInitializer {

    public static final String MOD_ID = "ehc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_50_PERCENT = new ArrayList<>(Collections.singleton(
        LootTables.BASTION_TREASURE_CHEST.getValue()
    ));

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_20_PERCENT = new ArrayList<>(Arrays.asList(
        LootTables.STRONGHOLD_CROSSING_CHEST.getValue(),
        LootTables.STRONGHOLD_CORRIDOR_CHEST.getValue(),
        LootTables.DESERT_PYRAMID_ARCHAEOLOGY.getValue(),
        LootTables.DESERT_WELL_ARCHAEOLOGY.getValue(),
        LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY.getValue(),
        LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY.getValue(),
        LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY.getValue(),
        LootTables.ANCIENT_CITY_ICE_BOX_CHEST.getValue(),
        LootTables.BASTION_TREASURE_CHEST.getValue(),
        LootTables.DESERT_PYRAMID_CHEST.getValue(),
        LootTables.JUNGLE_TEMPLE_CHEST.getValue(),
        LootTables.WOODLAND_MANSION_CHEST.getValue(),
        LootTables.END_CITY_TREASURE_CHEST.getValue()
    ));

    private static final ArrayList<Identifier> LOOT_TABLE_IDS_10_PERCENT = new ArrayList<>(Arrays.asList(
        LootTables.ANCIENT_CITY_CHEST.getValue(),
        LootTables.TRAIL_RUINS_COMMON_ARCHAEOLOGY.getValue()
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
            if (LOOT_TABLE_IDS_50_PERCENT.contains(registryKey.getValue())) {
                addHeartPieceItemToLoot(tableBuilder, 0.5f);
            } else if (LOOT_TABLE_IDS_20_PERCENT.contains(registryKey.getValue())) {
                addHeartPieceItemToLoot(tableBuilder, 0.2f);
            } else if (LOOT_TABLE_IDS_10_PERCENT.contains(registryKey.getValue())) {
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
            if (entity instanceof EnderDragonEntity && !saver.ehc$onDragonKilled()
                || entity instanceof WitherEntity && !saver.ehc$onWitherKilled()
                || entity instanceof ElderGuardianEntity && !saver.ehc$onElderGuardianKilled()
                || entity instanceof WardenEntity && !saver.ehc$onWardenKilled()) {

                ItemStack heartContainer = new ItemStack(ModItems.HEART_CONTAINER);
                player.getEntityWorld().spawnEntity(new ItemEntity(player.getEntityWorld(), entity.getX(), entity.getY(), entity.getZ(), heartContainer));
            }
        });

        LOGGER.info("Hello, this is Eddy's Heart Container mod, everything seems to work fine :)");
    }

    private static void addHeartPieceItemToLoot(LootTable.Builder tableBuilder, float chance) {
        LootPool.Builder poolBuilder = LootPool.builder()
            .conditionally(RandomChanceLootCondition.builder(chance))
            .with(ItemEntry.builder(ModItems.HEART_PIECE));

        tableBuilder.pool(poolBuilder);
    }
}
