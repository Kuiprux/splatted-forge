package com.kuiprux.splatted.handler.shape;

import com.kuiprux.splatted.SplattedUtil;
import com.kuiprux.splatted.handler.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.phys.Vec3;

public class SimpleCirclePaintShape implements InkPaintShape {
	
	Vec3 center;
	double range;
	Direction face;
	
	public SimpleCirclePaintShape(Vec3 center, double range, Direction face) {
		super();
		this.center = center;
		this.range = range;
		this.face = face;
	}

	@Override
	public Vec3 getCenter() {
		return center;
	}

	@Override
	public double getRange() {
		return range;
	}

	@Override
	public double getMinY() {
		return 0;
	}

	@Override
	public double getMaxY() {
		return 0;
	}

	@Override
	public Direction getFace() {
		return face;
	}

	@Override
	public boolean shouldPaint(BlockPos pos, int faceX, int faceY) {
		Vec3 actualPos = SplattedUtil.getActualPosition(pos, face, faceX, faceY);
		
		double xDiff = actualPos.x - center.x;
		double yDiff = actualPos.y - center.y;
		double zDiff = actualPos.z - center.z;
		
		double lenSq = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
		
		return lenSq <= range * range;
	}

}
