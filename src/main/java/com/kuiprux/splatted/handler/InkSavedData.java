package com.kuiprux.splatted.handler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.google.common.collect.HashBasedTable;
import com.kuiprux.splatted.SplattedUtil;
import com.kuiprux.splatted.handler.shape.InkPaintShape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

public class InkSavedData extends SavedData {
	
	public static final String ID = "splatted_ink";
	
	private final ServerLevel level;
	private int faceDivisionAmount = ConfigHandler.getFaceSubdivisionAmount(); //TODO
	
	HashBasedTable<BlockPos, Direction, PaintData[][]> paintMap = HashBasedTable.create();
	
	PriorityQueue<PaintData> paintQueue = new PriorityQueue<>(Comparator.comparingLong(paintData -> paintData.createdTick));
	
	public InkSavedData(ServerLevel level) {
		this(level, null);
	}
	
	public InkSavedData(ServerLevel level, CompoundTag tag) {
		this.level = level;
		load(tag);
	}
	
	public static InkSavedData get(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent((tag) -> new InkSavedData(level, tag),
				() -> new InkSavedData(level), ID);
	}
	/*
	public void setFaceDivisionAmount(int amount) {
		resizeAll(faceDivisionAmount, amount);
		faceDivisionAmount = amount;		
	}
	*/
	/*
	private void resizeAll(int oldAmount, int newAmount) {
		HashBasedTable<BlockPos, Direction, PaintData[][]> newPaintMap = HashBasedTable.create();
		PriorityQueue<PaintData> newPaintQueue = new PriorityQueue<>(Comparator.comparingLong(paintData -> paintData.createdTick));
		
		double fullLen = (double) newAmount / oldAmount;
		double targSize = fullLen * fullLen / 2;
		for(Cell<BlockPos, Direction, PaintData[][]> cell : paintMap.cellSet()) {
			newPaintMap.put(cell.getRowKey(), cell.getColumnKey(), new PaintData[newAmount][newAmount]);
			PaintData[][] oldGrid = cell.getValue();
			
			double[][] filledArea = new double[newAmount][newAmount];
			for(int j = 0; j < oldAmount; j++) {
				for(int i = 0; i < oldAmount; i++) {
					//TODO
				}
			}
		}
	}*/

	public void onLevelTick() {
		long curGameTick = level.getGameTime();
		while (!paintQueue.isEmpty() && isPaintTooOld(paintQueue.peek(), curGameTick)) {
		    PaintData oldestPaint = paintQueue.poll();
		    paintMap.get(oldestPaint.blockPos, oldestPaint.direction)[oldestPaint.faceX][oldestPaint.faceY] = null;
		}
	}
	
	private boolean isPaintTooOld(PaintData data, long curGameTick) {
		return data.createdTick + ConfigHandler.getPaintExpireTicks() <= curGameTick;
	}
	
	public void paint(BlockPos blockPos, Direction direction, int faceX, int faceY, int color) {
		this.paint(blockPos, direction, faceX, faceY, color, -1, true);
	}
	
	private void paint(BlockPos blockPos, Direction direction, int faceX, int faceY, int color, long createdTick, boolean shouldMark) {
		faceX = faceX < 0 ? 0 : faceX >= faceDivisionAmount ? faceDivisionAmount - 1 : faceX;
		faceY = faceY < 0 ? 0 : faceY >= faceDivisionAmount ? faceDivisionAmount - 1 : faceY;
		
		PaintData[][] facePaintData = paintMap.get(blockPos, direction);
		if(facePaintData == null) {
			facePaintData = new PaintData[faceDivisionAmount][faceDivisionAmount];
			paintMap.put(blockPos, direction, facePaintData);
		}
		
		if(facePaintData[faceX][faceY] != null) {
			paintQueue.remove(facePaintData[faceX][faceY]);
		}
		
		facePaintData[faceX][faceY] = new PaintData(blockPos, direction, faceX, faceY, color, createdTick < 0 ? level.getGameTime() : createdTick);
		paintQueue.add(facePaintData[faceX][faceY]);
		
		System.out.println("Painted: " + blockPos + " " + direction + " " + faceX + " " + faceY + " " + color);
		
		if(shouldMark)
			this.setDirty();
	}
	
	public void paintWithShape(InkPaintShape paintShape, int color) { //TODO
		Direction face = paintShape.getFace();
		
		Tuple<Vec3i, Vec3i> blockRange = SplattedUtil.getBlockRange(paintShape.getCenter(), paintShape.getRange(), paintShape.getMinY(), paintShape.getMaxY(), face);
		
		Vec3i startPos = blockRange.getA();
		Vec3i endPos = blockRange.getB();
		
		for(int k = startPos.getZ(); k < endPos.getZ(); k++) {
			for(int j = startPos.getY(); j < endPos.getY(); j++) {
				for(int i = startPos.getX(); i < endPos.getX(); i++) {
					for(int y = 0; y < faceDivisionAmount; y++) {
						for(int x = 0; x < faceDivisionAmount; x++) {							
							BlockPos pos = new BlockPos(i, j, k);
							
							if(isBlockSolid(pos) && !isBlockSolid(pos.relative(face)) 
									&& paintShape.shouldPaint(pos, x, y))
								paint(pos, face, x, y, color, -1, false);
						}
					}
				}
			}
		}
		
		this.setDirty();
	}
	
	//TODO handle case when block is placed on ink
	
	private boolean isBlockSolid(BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		return !blockState.isAir() && blockState.canOcclude();
	}
	
	public void load(CompoundTag tag) {
		if(tag == null || paintQueue.size() == 0)
			return;
		
		ListTag list = tag.getList("splattedInk", CompoundTag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++) {
			CompoundTag aTag = list.getCompound(i);
			
			BlockPos blockPos = new BlockPos(aTag.getInt("blockPosX"), aTag.getInt("blockPosY"), aTag.getInt("blockPosZ"));
			Direction direction = Direction.values()[aTag.getInt("direction")];
			int faceX = aTag.getInt("faceX");
			int faceY = aTag.getInt("faceY");
			int color = aTag.getInt("color");
			long createdTick = aTag.getLong("createdTick");
			
			paint(blockPos, direction, faceX, faceY, color, createdTick, false);
		}
		
		this.setDirty();
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		ListTag list = new ListTag();
		
		Iterator<PaintData> iter = paintQueue.iterator();
		while(iter.hasNext()) {
			PaintData data = iter.next();
			CompoundTag aTag = new CompoundTag();
			
			aTag.putInt("blockPosX", data.blockPos.getX());
			aTag.putInt("blockPosY", data.blockPos.getY());
			aTag.putInt("blockPosZ", data.blockPos.getZ());
			aTag.putInt("direction", data.direction.ordinal());
			aTag.putInt("faceX", data.faceX);
			aTag.putInt("faceY", data.faceY);
			aTag.putInt("color", data.color);
			aTag.putLong("createdTick", data.createdTick);
			
			list.add(aTag);
		}
		
		tag.put("splattedInk", list);
		
		return tag;
	}


	record PaintData(BlockPos blockPos, Direction direction, int faceX, int faceY, int color, long createdTick) {}
}
