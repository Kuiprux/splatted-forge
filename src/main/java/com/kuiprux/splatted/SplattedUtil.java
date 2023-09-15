package com.kuiprux.splatted;

import com.kuiprux.splatted.handler.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;

public class SplattedUtil {

	public static Vec3 getActualPosition(BlockPos pos, Direction face, int faceX, int faceY) {
		double faceLen = 1.0 / ConfigHandler.getFaceSubdivisionAmount() / 2;
		
		double actualX = pos.getX();
		double actualY = pos.getY();
		double actualZ = pos.getZ();
		
		switch(face.getAxis()) {
		case X:
			actualZ = pos.getZ() + faceLen * (faceX * 2 + 1);
			actualY = pos.getY() + faceLen * (faceY * 2 + 1);
			
			if(face.getAxisDirection() == AxisDirection.POSITIVE)
				actualX++;
			break;
		case Y:
			actualX = pos.getX() + faceLen * (faceX * 2 + 1);
			actualZ = pos.getZ() + faceLen * (faceY * 2 + 1);

			if(face.getAxisDirection() == AxisDirection.POSITIVE)
				actualY++;
			break;
		case Z:
			actualX = pos.getX() + faceLen * (faceX * 2 + 1);
			actualY = pos.getY() + faceLen * (faceY * 2 + 1);

			if(face.getAxisDirection() == AxisDirection.POSITIVE)
				actualZ++;
			break;
		}
		
		return new Vec3(actualX, actualY, actualZ);
	}

	public static Tuple<Vec3i, Vec3i> getBlockRange(Vec3 center, double range, double minY, double maxY,
			Direction face) {
		int startX = 0;
		int startY = 0;
		int startZ = 0;
		int endX = 0;
		int endY = 0;
		int endZ = 0;
		
		switch(face.getAxis()) {
		case X:
			startY = (int) Math.floor(center.y - range);
			endY = (int) Math.ceil(center.y + range);
			startZ = (int) Math.floor(center.z - range);
			endZ = (int) Math.ceil(center.z + range);
			
			if(face.getAxisDirection() == AxisDirection.POSITIVE) {
				startX = customFloor(center.x + minY);
				endX = customCeil(center.x + maxY) - 1;
			} else {
				startX = customFloor(center.x - maxY) + 1;
				endX = customCeil(center.x - minY);
			}
			break;
		case Y:
			startX = customFloor(center.x - range);
			endX = customCeil(center.x + range);
			startZ = customFloor(center.z - range);
			endZ = customCeil(center.z + range);
			
			if(face.getAxisDirection() == AxisDirection.POSITIVE) {
				startY = customFloor(center.y + minY);
				endY = customCeil(center.y + maxY) - 1;
			} else {
				startY = customFloor(center.y - maxY) + 1;
				endY = customCeil(center.y - minY);
			}
			break;
		case Z:
			startX = customFloor(center.x - range);
			endX = customCeil(center.x + range);
			startY = customFloor(center.y - range);
			endY = customCeil(center.y + range);
			
			if(face.getAxisDirection() == AxisDirection.POSITIVE) {
				startZ = customFloor(center.z + minY);
				endZ = customCeil(center.z + maxY) - 1;
			} else {
				startZ = customFloor(center.z - maxY) + 1;
				endZ = customCeil(center.z - minY);
			}
			break;
		}
		
		return new Tuple<>(new Vec3i(startX, startY, startZ), new Vec3i(endX, endY, endZ));
	}
	
	
	private static int customFloor(double val) {
		int N = (int) Math.floor(val);
		return N == val ? N-1 : N;
	}
	
	private static int customCeil(double val) {
		int N = (int) Math.ceil(val);
		return N == val ? N+1 : N;
	}
}
