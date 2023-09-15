package com.kuiprux.splatted.calchandler;

public class SimpleDamageByRangeCalcHandler extends DamageByRangeCalcHandler {
	
	int range;
	int value;
	
	public SimpleDamageByRangeCalcHandler(int range, int value) {
		this.range = range;
		this.value = value;
	}

	@Override
	public int getDamage(double distance) {
		return distance <= range ? value : 0;
	}
	
}
