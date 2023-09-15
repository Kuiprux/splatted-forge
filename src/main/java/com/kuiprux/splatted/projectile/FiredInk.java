package com.kuiprux.splatted.projectile;

import com.kuiprux.splatted.handler.InkSavedData;
import com.kuiprux.splatted.handler.shape.SimpleCirclePaintShape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.IModelBuilder.Simple;

public class FiredInk extends Projectile {
	
	public static final String ID = "fired-ink";
	
	public FiredInk(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
		// TODO Auto-generated constructor stub
	}

	public static final String NBT_KEY_COLOR = "ink_color";
	public static final String NBT_KEY_SIZE = "ink_size";
	public static final String NBT_KEY_LENGTH = "ink_length";
	
	private int color = 0xC15F2E;
	private double size = 1;
	private double length = 1;
	
    public double powerX;
    public double powerY;
    public double powerZ;
    
    public String weaponId;
    private double fillRange;
    private double fillHeight;
/*
	public FiredInk(EntityType<? extends FiredInk> entityType, World world, String weaponId) {
		super(entityType, world);
		this.weaponId = weaponId;
	}
	/*
    public FiredInk(EntityType<? extends FiredInk> type, World world, double x, double y, double z, double directionX, double directionY, double directionZ) {
        this(type, world);
        this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
        this.refreshPosition();
        double d = Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
        if (d != 0.0) {
            this.powerX = directionX / d * 0.1;
            this.powerY = directionY / d * 0.1;
            this.powerZ = directionZ / d * 0.1;
        }
    }*/

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub
		
	}
	
/*
    @Override
    public void tick() {
        HitResult hitResult;
        Entity entity = this.getOwner();
        if (!this.level().isClientSide() && (entity != null && entity.isRemoved() || !this.level().hasChunkAt(this.blockPosition()))) {
            this.discard();
            return;
        }
        super.tick();
        if ((hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity)).getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }
        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d = this.getX() + vec3.x;
        double e = this.getY() + vec3.y;
        double f = this.getZ() + vec3.z;
        ProjectileUtil.rotateTowardsMovement(this, 0.2f);
        float g = this.getInertia();
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float h = 0.25f;
                this.level().addParticle(ParticleTypes.BUBBLE, d - vec3.x * 0.25, e - vec3.y * 0.25, f - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }
            g = 0.8f;
        }
        this.setDeltaMovement(vec3.add(this.powerX, this.powerY, this.powerZ).scale(g));
        this.setPos(d, e, f);
    }
	*/

	//Code from ThrowableProjectile
	public void tick() {
		super.tick();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		boolean flag = false;
		if (hitresult.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
			BlockState blockstate = this.level().getBlockState(blockpos);
			if (blockstate.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(blockpos);
				flag = true;
			} else if (blockstate.is(Blocks.END_GATEWAY)) {
				BlockEntity blockentity = this.level().getBlockEntity(blockpos);
				if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
					TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this,
							(TheEndGatewayBlockEntity) blockentity);
				}
	
				flag = true;
			}
		}
	
		if (hitresult.getType() != HitResult.Type.MISS && !flag
				&& !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
			this.onHit(hitresult);
		}
	
		this.checkInsideBlocks();
		Vec3 vec3 = this.getDeltaMovement();
		double d2 = this.getX() + vec3.x;
		double d0 = this.getY() + vec3.y;
		double d1 = this.getZ() + vec3.z;
		this.updateRotation();
		if (this.isInWater()) {
			double mult = this.getParticleMultiplier();
			for (int i = 0; i < 4; ++i) {
				this.level().addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * mult, d0 - vec3.y * mult,
						d1 - vec3.z * mult, vec3.x, vec3.y, vec3.z);
			}
	
			System.out.println("WATER");
			this.discard();
		} else {
			this.setDeltaMovement(vec3.scale((double) this.getInertia()));
			if (!this.isNoGravity()) {
				Vec3 vec31 = this.getDeltaMovement();
				this.setDeltaMovement(vec31.x, vec31.y - this.getGravity(), vec31.z);
			}
		
			this.setPos(d2, d0, d1);
		}
	}
	
	protected double getParticleMultiplier() {
		return 0.25;
	}
	
	protected double getInertia() {
		return 0.99;
	}

    protected double getGravity() {
        return 0.1;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
    	super.onHitEntity(entityHitResult);
    	//entityHitResult.getEntity().damage(new DamageSource(SplattedDamageTypes.SPLATTED, this.getOwner()), 3);
    	/*
    	Level level = level();
    	if(!level.isClientSide) {
	    	SimpleCirclePaintShape shape = new SimpleCirclePaintShape(this.position(), 1, blockHitResult.getDirection());
	    	InkSavedData.get((ServerLevel) level).paintWithShape(shape, color);
    	}
    	*/
    	this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
    	super.onHitBlock(blockHitResult);
    	System.out.println("HIT: " + blockHitResult.getLocation());
    	
    	Level level = level();
    	if(!level.isClientSide) {
	    	SimpleCirclePaintShape shape = new SimpleCirclePaintShape(blockHitResult.getLocation(), 1, blockHitResult.getDirection());
	    	InkSavedData.get((ServerLevel) level).paintWithShape(shape, color);
    	}
    	
    	this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    	super.addAdditionalSaveData(tag);
    	
    	tag.putInt(NBT_KEY_COLOR, color);
    	tag.putDouble(NBT_KEY_SIZE, size);
    	tag.putDouble(NBT_KEY_LENGTH, length);
    	tag.putString(NBT_KEY_LENGTH, NBT_KEY_COLOR);
    }
    

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    	super.readAdditionalSaveData(tag);
    	
    	if(tag.contains(NBT_KEY_COLOR, CompoundTag.TAG_INT))
    		color = tag.getInt(NBT_KEY_COLOR);
    	
    	if(tag.contains(NBT_KEY_SIZE, CompoundTag.TAG_DOUBLE))
    		size = tag.getInt(NBT_KEY_SIZE);
    	
    	if(tag.contains(NBT_KEY_LENGTH, CompoundTag.TAG_DOUBLE))
    		length = tag.getInt(NBT_KEY_LENGTH);
    }

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
}
