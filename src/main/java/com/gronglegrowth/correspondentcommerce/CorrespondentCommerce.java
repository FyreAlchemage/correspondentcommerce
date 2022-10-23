package com.gronglegrowth.correspondentcommerce;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrespondentCommerce implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("correspondentcommerce");

	public static final Item SHARD = new Item(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON));
	public static final ExchangeStoneItem EXCHANGE_STONE = new ExchangeStoneItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE));

	private static final Identifier MAGMA_CUBE_LOOT = Blocks.MAGMA_BLOCK.getLootTableId();
	// will probably be changed to the entity proper later

	@Override
	public void onInitialize() {

		// Register our items, and make the shards a fuel.
		Registry.register(Registry.ITEM, new Identifier("correspondentcommerce", "rubedo_shard"), SHARD);
		Registry.register(Registry.ITEM, new Identifier("correspondentcommerce", "rubedo_stone"), EXCHANGE_STONE);
		FuelRegistry.INSTANCE.add(SHARD, 12800);

		// Inject rubedo shards into the magma cube's loot table.
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && MAGMA_CUBE_LOOT.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(SHARD))
						.conditionally(SurvivesExplosionLootCondition.builder());

				tableBuilder.pool(poolBuilder);
			}
		});

		LOGGER.info("Correspondent Commerce has loaded!");
	}
}
