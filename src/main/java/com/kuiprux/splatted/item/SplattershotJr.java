package com.kuiprux.splatted.item;

import com.kuiprux.splatted.projectile.FiredInk;
import com.kuiprux.splatted.projectile.SplattedProjectiles;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SplattershotJr extends WeaponBase {
	
	public static final String ID = "splattershot-jr";

	public SplattershotJr() {
		super(ID);
	}

	@Override
	public void fire(Level level, LivingEntity user, ItemStack stack) {
		System.out.println("\tFIRED");
		FiredInk ink = new FiredInk(SplattedProjectiles.FIRED_INK.get(), level);
		fireInk(level, user, ink);
	}

}
