package fr.tayaut.ehc;

import fr.tayaut.ehc.event.PlayerAdvancementCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
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

        // ENREGISTREMENT DU CALLBACK
        // C'est ici qu'on "s'abonne" à l'événement "le joueur vient de "débloquer" un progrès".
        PlayerAdvancementCallback.EVENT.register((player, advancement) -> {

            // Récupérer l'ID de l'achievement (ex: "minecraft:story/mine_stone")
            String advancementId = advancement.id().toString();
            if (advancementId.equals("minecraft:end/kill_dragon")
                || advancementId.equals("minecraft:nether/create_beacon")
                || advancementId.equals("minecraft:adventure/hero_of_the_village")
                || advancementId.equals("minecraft:nether/all_potions")
                || advancementId.equals("minecraft:adventure/adventuring_time")
                || advancementId.equals("minecraft:adventure/kill_all_mobs")) {
                ItemStack heartContainer = new ItemStack(ModItems.HEART_CONTAINER);
                if (!player.getInventory().insertStack(heartContainer)) {
                    player.dropItem(heartContainer, false);
                    // TODO: ça ça marche pô
                }
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
