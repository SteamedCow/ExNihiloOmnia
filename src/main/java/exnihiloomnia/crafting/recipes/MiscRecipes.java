package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.crafting.ENOCrafting;
import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MiscRecipes {
    private static ItemStack stone = new ItemStack(ENOItems.STONE);
	
	public static void registerOtherRecipes() {
		ResourceLocation group = new ResourceLocation(ENO.MODID);

        //end cake
        if (ENOConfig.end_cake)
            GameRegistry.addShapedRecipe(ENOBlocks.END_CAKE.getRegistryName(), group,
					new ItemStack(ENOBlocks.END_CAKE),
                        "eee",
                        "cgc",
                        "eee",
                        'e', new ItemStack(Items.ENDER_EYE),
                        'g', new ItemStack(Items.GOLDEN_APPLE),
                        'c', new ItemStack(Items.CAKE));

		//web
		GameRegistry.addShapedRecipe(ENOBlocks.END_CAKE.getRegistryName(), group,
				new ItemStack(Blocks.WEB, 1),
						"xxx",
						"xyx",
						"xxx",
						'x', new ItemStack(Items.STRING, 1),
						'y', new ItemStack(Items.SLIME_BALL, 1));

		if (ENOCrafting.stone_required > 0) {
			Ingredient[] stones = new Ingredient[ENOCrafting.stone_required];

			for (int i = 0; i < ENOCrafting.stone_required; i++) {
				if (i <= stones.length)
					stones[i] = Ingredient.fromItem(ENOItems.STONE);
			}

			GameRegistry.addShapelessRecipe(Blocks.COBBLESTONE.getRegistryName(), group,
					new ItemStack(Blocks.COBBLESTONE), stones);
		}

		//porcelain
		GameRegistry.addShapelessRecipe(Blocks.COBBLESTONE.getRegistryName(), group,
				new ItemStack(ENOItems.PORCELAIN, 1),
				Ingredient.fromItem(Items.CLAY_BALL),
				Ingredient.fromItem(ENOItems.ASH),
				Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))); //bonemeal
	}
	
	public static void registerSmeltingRecipes() {
		GameRegistry.addSmelting(new ItemStack(Items.STICK, 1), new ItemStack(ENOItems.ASH, 1), 0);
		GameRegistry.addSmelting(new ItemStack(Blocks.MOSSY_COBBLESTONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0), 0);
		GameRegistry.addSmelting(new ItemStack(Blocks.STONEBRICK, 1, 1), new ItemStack(Blocks.STONEBRICK, 1, 2), 0);
		GameRegistry.addSmelting(new ItemStack(ENOItems.SILKWORM, 1), new ItemStack(ENOItems.COOKED_SILKWORM, 1), 0);
	}
}
