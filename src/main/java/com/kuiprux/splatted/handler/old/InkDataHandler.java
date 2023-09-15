package com.kuiprux.splatted.handler.old;

import java.util.HashMap;
import java.util.Map;

import com.kuiprux.splatted.handler.old.LevelInkDataHandler.Vec2i;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class InkDataHandler {
	
	public static final InkDataHandler INSTANCE = new InkDataHandler();
	
	Map<Level, LevelInkDataHandler> paintMap = new HashMap<>();
	
	private InkDataHandler() {}
	
	public void onLevelTick(Level level) {
		if(!paintMap.containsKey(level))
			return;
		
		paintMap.get(level).onLevelTick();
	}
	
	public void paint(Level level, BlockPos blockPos, Direction direction, Vec2i posOnFace, int color) {
		paintMap.computeIfAbsent(level, k -> new LevelInkDataHandler(level)).paint(blockPos, direction, posOnFace, color);
	}

	public void loadData(CompoundTag data) {
		// TODO Auto-generated method stub
		
	}

	public void saveData(CompoundTag data) {
		// TODO Auto-generated method stub
		
	}
}
