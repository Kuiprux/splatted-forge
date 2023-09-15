package com.kuiprux.splatted.item;

import com.kuiprux.splatted.SplattedRegistries;
import com.kuiprux.splatted.SplattedRegistry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class SplattedItems extends SplattedRegistry {

	public static final RegistryObject<Item> SPLATTERSHOT_JR = SplattedRegistries.ITEMS.register(SplattershotJr.ID, () -> new SplattershotJr());
	


	public static void load() {}
}
