package com.kuiprux.splatted;

import com.kuiprux.splatted.item.SplattedItems;
import com.kuiprux.splatted.projectile.SplattedProjectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SplattedRegistries extends SplattedRegistry{

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Splatted.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Splatted.MOD_ID);
	
	
	public static void load() {
		SplattedItems.load();
		SplattedProjectiles.load();
		
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	
}
