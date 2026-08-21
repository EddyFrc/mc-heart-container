package fr.tayaut.ehc;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import static net.minecraft.data.advancements.AdvancementSubProvider.createPlaceholder;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {

    public AdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        AdvancementHolder getHeartPiece = Advancement.Builder.advancement()
            .display(
                ModItems.HEART_PIECE,
                Component.literal("Ta-tadadadaaaa!"),
                Component.literal("Get a Heart Piece"),
                Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("got_heart_piece", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HEART_PIECE))
            .parent(createPlaceholder(Identifier.withDefaultNamespace("adventure/root").toString()))
            .save(consumer, Identifier.fromNamespaceAndPath(EddysHeartContainer.MOD_ID, "get_heart_piece").toString());

        AdvancementHolder getHeartContainer = Advancement.Builder.advancement()
            .display(
                ModItems.HEART_CONTAINER,
                Component.literal("Someday you will be surrounded with love..."),
                Component.literal("Find or craft a Heart Container"),
                Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                AdvancementType.GOAL,
                true,
                true,
                false
            )
            .addCriterion("got_heart_container", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HEART_CONTAINER))
            .parent(getHeartPiece)
            .save(consumer, Identifier.fromNamespaceAndPath(EddysHeartContainer.MOD_ID, "get_heart_container").toString());
    }
}
