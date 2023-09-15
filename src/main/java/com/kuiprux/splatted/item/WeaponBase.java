package com.kuiprux.splatted.item;

import com.kuiprux.splatted.calchandler.DamageByRangeCalcHandler;
import com.kuiprux.splatted.calchandler.FirableChecker;
import com.kuiprux.splatted.calchandler.SimpleDamageByRangeCalcHandler;
import com.kuiprux.splatted.calchandler.SimpleFirableChecker;
import com.kuiprux.splatted.projectile.FiredInk;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public abstract class WeaponBase extends Item {

	String name;
	FirableChecker firableChecker;
	DamageByRangeCalcHandler hitDamageCalc;
	boolean jumpable = true;
	double walkSpeedMultiplier = 1.0;

	public WeaponBase(String name) {
		this(name, 5, 4);
	}

	public WeaponBase(String name, int fireTick, int fireAmount) {
		this(name, fireTick, fireAmount, 0, 0);
	}

	public WeaponBase(String name, int fireTick, int fireAmount, int hitRange, int hitDamage) {
		this(name, new SimpleFirableChecker(fireTick, fireAmount), new SimpleDamageByRangeCalcHandler(hitRange, hitDamage));
	}

	public WeaponBase(String name, FirableChecker firableChecker, DamageByRangeCalcHandler hitDamageCalc) {
		super(new Properties().stacksTo(1).fireResistant());
		this.name = name;
		this.firableChecker = firableChecker;
		this.hitDamageCalc = hitDamageCalc;
		System.out.println("HI");
	}
	
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }
	
    @Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		//System.out.println("USE");
		
		if(hand == InteractionHand.OFF_HAND)
			return InteractionResultHolder.fail(user.getItemInHand(hand));
		
		firableChecker.reset();
		return ItemUtils.startUsingInstantly(world, user, hand);
	}
	
    @Override
	public int getUseDuration(ItemStack stack) {
		//System.out.println("MAX_USE_TIME");
		return Integer.MAX_VALUE;
	}

	//TODO there will be a bug where user can fire fast when they spam right click
	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if(!world.isClientSide) {
			//System.out.println("USAGE_TICK");
			firableChecker.update(Integer.MAX_VALUE - remainingUseTicks);
			
			if(firableChecker.isFirable()) {
				fire(world, user, stack);
				//finishUsingItem(stack, world, user);
			}
			
			//if(!firableChecker.canFireMore())
				//finishUsing(stack, world, user);
		}
    }
	
	public abstract void fire(Level world, LivingEntity user, ItemStack stack);
	
	@Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
    	//System.out.println("FINISH_USING");
		firableChecker.reset();
    	
    	return stack;
    }
    
    protected void fireInk(Level world, LivingEntity user, FiredInk ink) {
        ink.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, 1.0f, 1.0f);
        ink.setOwner(user);
        ink.setPos(user.getX(), user.getEyeY() - (double)0.1f, user.getZ());
        world.addFreshEntity(ink);
    }

}
