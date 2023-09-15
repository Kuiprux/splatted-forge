package com.kuiprux.splatted.calchandler;

public abstract class FirableChecker {
	
	public abstract void update(int elapsedTicks);
	
	public abstract boolean isFirable();

	public abstract boolean canFireMore();

	public abstract void reset();

}
