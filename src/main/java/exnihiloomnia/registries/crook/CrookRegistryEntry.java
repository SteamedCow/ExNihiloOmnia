package exnihiloomnia.registries.crook;

import exnihiloomnia.registries.crook.pojos.CrookRecipe;
import exnihiloomnia.registries.crook.pojos.CrookRecipeReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CrookRegistryEntry {
	private final IBlockState input;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.SPECIFIC;
	private final ArrayList<CrookReward> rewards = new ArrayList<>();

	public CrookRegistryEntry(IBlockState input, EnumMetadataBehavior behavior) {
		this.input = input;
		this.behavior = behavior;
	}

	public IBlockState getInput() {
		return input;
	}

	public void addReward(ItemStack item, int base_chance, int luck_modifier) {
		this.rewards.add(new CrookReward(item, base_chance, luck_modifier));
	}

	public ArrayList<CrookReward> getRewards() {
		return rewards;
	}

	public EnumMetadataBehavior getMetadataBehavior() {
		return this.behavior;
	}

	public void dropRewards(World world, ItemStack item, BlockPos pos) {
		for (CrookReward reward : rewards) {
			reward.dropReward(world, item, pos);
		}
	}

	public List<ItemStack> rollRewards(World world, ItemStack item) {
		ArrayList<ItemStack> rewards = new ArrayList<>();

		for (CrookReward reward : this.rewards) {
			ItemStack itemReward = reward.getReward(world, item);
			if (itemReward != null) {
				rewards.add(itemReward);
			}
		}

		return rewards;
	}

	public String getKey() {
		String s = Block.REGISTRY.getNameForObject(input.getBlock()).toString();

		if (behavior == EnumMetadataBehavior.IGNORED) {
			return s + ":*";
		}
		else {
			return s + ":" + input.getBlock().getMetaFromState(input);
		}
	}

	public static CrookRegistryEntry fromRecipe(CrookRecipe recipe) {
		Block block = Block.REGISTRY.getObject(new ResourceLocation(recipe.getId()));

		if (block != null) {
			IBlockState state = block.getBlockState().getBaseState();

			if (state != null) {
				CrookRegistryEntry entry = new CrookRegistryEntry(state, recipe.getBehavior());

				for (CrookRecipeReward reward : recipe.getRewards()) {
					Item item = Item.REGISTRY.getObject(new ResourceLocation(reward.getId()));

					if (item != null) {
						entry.addReward(new ItemStack(item, reward.getAmount(), reward.getMeta()), reward.getBaseChance(), reward.getFortuneModifier());
					}
				}

				return entry;
			}
		}

		return null;
	}

	public CrookRecipe toRecipe() {
		String block = Block.REGISTRY.getNameForObject(this.getInput().getBlock()).toString();

		CrookRecipe recipe = new CrookRecipe(block, this.getInput().getBlock().getMetaFromState(this.getInput()), this.getMetadataBehavior());

		ArrayList<CrookRecipeReward> rewards = new ArrayList<>();

		for (CrookReward reward : this.getRewards())
			rewards.add(new CrookRecipeReward(Item.REGISTRY.getNameForObject(reward.getItem().getItem()).toString(), reward.getItem().getMetadata(), reward.getItem().getCount(), reward.getBaseChance(), reward.getFortuneModifier()));

		recipe.setRewards(rewards);

		return recipe;
	}
}
