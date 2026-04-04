package fr.tayaut.ehc;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import java.util.function.Function;


public class ModItems {
    // ITEM heart container
    public static final HeartContainerItem HEART_CONTAINER = (HeartContainerItem) register(
        "heart_container",
        HeartContainerItem::new,
        new HeartContainerItem.Properties()
    );

    // ITEM heart piece
    public static final HeartPieceItem HEART_PIECE = (HeartPieceItem) register(
        "heart_piece",
        HeartPieceItem::new,
        new HeartContainerItem.Properties()
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
    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Créer une clé pour le registre, composée du nom de l'item donné en argument et autres infos diverses
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EddysHeartContainer.MOD_ID, name));
        // AJOUT : Lier la clé aux settings (Souvent requis en 1.21.2+)
        settings.setId(itemKey);
        // Fonction utilisée
        Item item = itemFactory.apply(settings);
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
        // Les deux lignes ici permettent de mettre les deux items dans la bonne catégorie en mode créa (je crois)
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(
            (itemGroup) -> itemGroup.accept(HEART_CONTAINER)
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(
            (itemGroup) -> itemGroup.accept(HEART_PIECE)
        );
    }
}
