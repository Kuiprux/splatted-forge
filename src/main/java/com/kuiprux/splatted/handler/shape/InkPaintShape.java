package com.kuiprux.splatted.handler.shape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public interface InkPaintShape {

	public Vec3 getCenter();
	public double getRange();
	public double getMinY();
	public double getMaxY();
	public Direction getFace();
	public boolean shouldPaint(BlockPos pos, int faceX, int faceY);
	
}
