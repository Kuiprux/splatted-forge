package com.kuiprux.splatted.projectile;

import com.kuiprux.splatted.Splatted;
import com.kuiprux.splatted.SplattedRegistries;
import com.kuiprux.splatted.SplattedRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;

public class SplattedProjectiles extends SplattedRegistry {

	public static final RegistryObject<EntityType<FiredInk>> FIRED_INK = SplattedRegistries.ENTITIES.register(FiredInk.ID, () -> EntityType.Builder.of(FiredInk::new, MobCategory.MISC)
			.sized(0.25f, 0.4f) // dimensions in Minecraft units of the projectile
			.setTrackingRange(4).setUpdateInterval(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
			.build(new ResourceLocation(Splatted.MOD_ID, FiredInk.ID).toString()));


	public static void load() {}
}