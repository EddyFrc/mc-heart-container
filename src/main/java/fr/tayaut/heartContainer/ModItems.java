package fr.tayaut.heartContainer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;


public class ModItems {
    // ITEM heart container
    public static final HeartContainerItem HEART_CONTAINER = (HeartContainerItem) register(
        "heart_container",
        HeartContainerItem::new,
        new HeartContainerItem.Settings()
    );

    // ITEM heart piece
    public static final HeartPieceItem HEART_PIECE = (HeartPieceItem) register(
        "heart_piece",
        HeartPieceItem::new,
        new HeartContainerItem.Settings()
    );

    /**
     * Enregistre l'item. Il faut fournir une fonction qui retourne une instance de <code>Item</code>
     *
     * @param name        Nom de l'item à register
     * @param itemFactory Fonction qui doit prendre une instance de <code>Item.Settings</code> en entrée
     *                    et fournir une instance de <code>Item</code> en sortie
     * @param settings    Paramètres à appliquer à l'item
     * @return Instance de l'item résultant de l'exécution de <code>itemFactory</code>
     */
    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Créer une clé pour le registre, composée du nom de l'item donné en argument et autres infos diverses
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(HeartContainer.MOD_ID, name));
        // AJOUT : Lier la clé aux settings (Souvent requis en 1.21.2+)
        settings.registryKey(itemKey);
        // Fonction utilisée
        Item item = itemFactory.apply(settings);
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
        // Les deux lignes ici permettent de mettre les deux items dans la bonne catégorie en mode créa (je crois)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(
            (itemGroup) -> itemGroup.add(HEART_CONTAINER)
        );
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(
            (itemGroup) -> itemGroup.add(HEART_PIECE)
        );
    }
}
