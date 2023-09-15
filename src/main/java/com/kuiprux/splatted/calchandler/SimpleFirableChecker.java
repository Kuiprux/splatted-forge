package com.kuiprux.splatted.calchandler;

public class SimpleFirableChecker extends FirableChecker {
	
	int fireTick;
	int fireAmount;
	boolean immediate;
	
	int totalFired = 0;
	
	boolean isFirable = false;
	
	
	public SimpleFirableChecker(int fireTick, int fireAmount) {
		this(fireTick, fireAmount, true);
	}
	
	public SimpleFirableChecker(int fireTick, int fireAmount, boolean immediate) {
		this.fireTick = fireTick;
		this.fireAmount = fireAmount;
		this.immediate = immediate;
	}
	
	@Override
	public void update(int elapsedTick) {
		isFirable = false;
		
		if(!canFireMore())
			return;
		
		int totalFirable = elapsedTick / fireTick + (immediate ? 1 : 0);
		System.out.println(elapsedTick + "\t" + totalFired + "\t" + totalFirable);
		if(totalFired < totalFirable) {
			System.out.println("HEY");
			totalFired++;
			isFirable = true;
		}
	}

	@Override
	public boolean isFirable() {
		return isFirable;
	}
	
	@Override
	public boolean canFireMore() {
		return fireAmount < 0 || totalFired < fireAmount;
	}
	
	@Override
	public void reset() {
		totalFired = 0;
	}

}
