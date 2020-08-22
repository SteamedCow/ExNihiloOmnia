package exnihiloomnia.blocks.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidWitchwater extends BlockFluidClassic {
	public BlockFluidWitchwater(Fluid fluid) {
		super(fluid, Material.WATER);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!world.isRemote && !entity.isDead) {
			if (entity instanceof EntityVillager) {
				EntityVillager villager = (EntityVillager) entity;
				
				if (world.getDifficulty() != EnumDifficulty.PEACEFUL) {
					if (!villager.isChild()) {
						replaceEntity(world, villager, new EntityWitch(world));
					} else {
						EntityZombieVillager zombie = replaceEntity(world, villager, new EntityZombieVillager(world));

						zombie.setForgeProfession(villager.getProfessionForge());
						zombie.setChild(villager.isChild());
					}
				} else {
					villager.onStruckByLightning(null);
				}
			} else if (entity instanceof EntitySkeleton) {
				replaceEntity(world, (EntitySkeleton) entity, new EntityWitherSkeleton(world));
			} else if (entity instanceof EntitySpider && !(entity instanceof EntityCaveSpider)) {
				replaceEntity(world, (EntitySpider) entity, new EntityCaveSpider(world));
			} else if (entity instanceof EntitySquid && world.getDifficulty() != EnumDifficulty.PEACEFUL) {
				replaceEntity(world, (EntitySquid) entity, new EntityGhast(world));
			} else if (entity instanceof EntityCreeper) {
				if (!((EntityCreeper) entity).getPowered()) {
					EntityCreeper creeper = (EntityCreeper) entity;
					creeper.onStruckByLightning(null);
					creeper.setHealth(creeper.getMaxHealth());
				}
			} else if(entity instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) entity;
				animal.onStruckByLightning(null);
			} else if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 210, 0));
				player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 210, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 210, 0));
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 210, 0));
			}
		}
	}

	private <T extends EntityLivingBase> T replaceEntity(World world, EntityLivingBase oldEntity, T newEntity) {
		oldEntity.setDead();

		newEntity.setLocationAndAngles(oldEntity.posX, oldEntity.posY + 2, oldEntity.posZ, oldEntity.rotationYaw, oldEntity.rotationPitch);
		newEntity.renderYawOffset = oldEntity.renderYawOffset;
		newEntity.setHealth(newEntity.getMaxHealth());

		world.spawnEntity(newEntity);

		return newEntity;
	}
}
