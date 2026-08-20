package fr.tayaut.ehc;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.data.advancement.AdvancementTabGenerator.reference;

public class AdvancementProvider extends FabricAdvancementProvider {

    public AdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry getHeartPiece = Advancement.Builder.create()
            .display(
                ModItems.HEART_PIECE,
                Text.literal("Ta-tadadadaaaa!"),
                Text.literal("Get a Heart Piece"),
                Identifier.ofVanilla("gui/advancements/backgrounds/adventure"),
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .criterion("got_heart_piece", InventoryChangedCriterion.Conditions.items(ModItems.HEART_PIECE))
            .parent(reference(Identifier.ofVanilla("adventure/root").toString()))
            .build(consumer, Identifier.of(EddysHeartContainer.MOD_ID, "get_heart_piece").toString());

        AdvancementEntry getHeartContainer = Advancement.Builder.create()
            .display(
                ModItems.HEART_CONTAINER,
                Text.literal("Someday you will be surrounded with love..."),
                Text.literal("Find or craft a Heart Container"),
                Identifier.ofVanilla("gui/advancements/backgrounds/adventure"),
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .criterion("got_heart_container", InventoryChangedCriterion.Conditions.items(ModItems.HEART_CONTAINER))
            .parent(getHeartPiece)
            .build(consumer, Identifier.of(EddysHeartContainer.MOD_ID, "get_heart_container").toString());
    }
}
