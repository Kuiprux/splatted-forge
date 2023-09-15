package com.kuiprux.splatted;

import net.minecraftforge.fml.common.Mod;

@Mod(Splatted.MOD_ID)
public class Splatted {
	
	public static final String MOD_ID = "splatted";

	public Splatted() {
		SplattedRegistries.load();
	}
}
