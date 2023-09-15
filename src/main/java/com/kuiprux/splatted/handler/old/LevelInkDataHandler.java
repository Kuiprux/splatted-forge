package com.kuiprux.splatted.handler.old;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import com.kuiprux.splatted.handler.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class LevelInkDataHandler extends SavedData {
	
	private final Level level;
	
	public LevelInkDataHandler(Level level) {
		this.level = level;
	}
	
	Map<BlockPos, Map<Direction, Map<Vec2i, PaintData>>> paintMap = new HashMap<>();
	
	PriorityQueue<PaintData> paintQueue = new PriorityQueue<>(Comparator.comparingLong(paintData -> paintData.createdTick));
	
	public void onLevelTick() {
		long curGameTick = level.getGameTime();
		while (!paintQueue.isEmpty() && isPaintTooOld(paintQueue.peek(), curGameTick)) {
		    PaintData oldestPaint = paintQueue.poll();
		    paintMap.get(oldestPaint.blockPos).get(oldestPaint.direction).remove(oldestPaint.posOnFace);
		}
	}
	
	private boolean isPaintTooOld(PaintData data, long curGameTick) {
		return data.createdTick + ConfigHandler.getPaintExpireTicks() <= curGameTick;
	}

	public void paint(BlockPos blockPos, Direction direction, Vec2i posOnFace, int color) {
		Map<Vec2i, PaintData> facePaintMap = paintMap.computeIfAbsent(blockPos, k -> new HashMap<>())
	        	.computeIfAbsent(direction, k -> new HashMap<>());
		
		if(facePaintMap.containsKey(posOnFace)) {
			PaintData existingData = facePaintMap.get(posOnFace);
			paintQueue.remove(existingData);
		}
		
		PaintData newData = new PaintData(blockPos, direction, posOnFace, color, level.getGameTime());
		facePaintMap.put(posOnFace, newData);
		paintQueue.add(newData);
	}

	@Override
	public CompoundTag save(CompoundTag pCompoundTag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	record Vec2i(int x, int y) {}
	record PaintData(BlockPos blockPos, Direction direction, Vec2i posOnFace, int color, long createdTick) {}
}
