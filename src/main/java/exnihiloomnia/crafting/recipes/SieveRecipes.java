package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SieveRecipes {
	public static void register() {
		ResourceLocation group = new ResourceLocation(ENO.MODID);

		//wood sieves
		for (int i = 0; i < 6; i++) {
			if (!ENOConfig.classic_sieve)
				GameRegistry.addShapedRecipe(ENOBlocks.SIEVE_WOOD.getRegistryName(), group,
						new ItemStack(ENOBlocks.SIEVE_WOOD, 1, i),
						"x x",
						"xxx",
						"y y",
						'x', new ItemStack(Blocks.PLANKS, 1, i),
						'y', "stickWood");
			else
				GameRegistry.addShapedRecipe(ENOBlocks.SIEVE_WOOD.getRegistryName(), group,
						new ItemStack(ENOBlocks.SIEVE_WOOD, 1, i),
						"xzx",
						"xzx",
						"y y",
						'x', new ItemStack(Blocks.PLANKS, 1, i),
						'y', "stickWood",
						'z', new ItemStack(ENOItems.MESH_SILK_WHITE));
		}
		
		//meshes
		//wood
		if (!ENOConfig.classic_sieve)
			GameRegistry.addShapedRecipe(ENOItems.MESH_WOOD.getRegistryName(), group,
					new ItemStack(ENOItems.MESH_WOOD, 1),
					"xxx",
					"xxx",
					"xxx",
					'x', "stickWood");
		
		//silk (white)
		GameRegistry.addShapedRecipe(ENOItems.MESH_SILK_WHITE.getRegistryName(), group,
				new ItemStack(ENOItems.MESH_SILK_WHITE, 1),
				"xxx",
				"xxx",
				"xxx",
				'x', new ItemStack(Items.STRING, 1));

		//sifters
		GameRegistry.addShapedRecipe(ENOItems.SIFTER_DIAMOND.getRegistryName(), group,
				new ItemStack(ENOItems.SIFTER_DIAMOND),
						"ddd",
						"sps",
                        "srs",
						'd', "gemDiamond",
						's', "cobblestone",
                        'r', "dustRedstone",
						'p', new ItemStack(Blocks.PISTON));

		GameRegistry.addShapedRecipe(ENOItems.SIFTER_GOLD.getRegistryName(), group,
				new ItemStack(ENOItems.SIFTER_GOLD),
						"ddd",
						"sps",
                        "srs",
						'd', "ingotGold",
						's', "cobblestone",
                        'r', "dustRedstone",
						'p', new ItemStack(Blocks.PISTON));

		GameRegistry.addShapedRecipe(ENOItems.SIFTER_IRON.getRegistryName(), group,
				new ItemStack(ENOItems.SIFTER_IRON),
						"ddd",
						"sps",
                        "srs",
						'd', "ingotIron",
						's', "cobblestone",
                        'r', "dustRedstone",
						'p', new ItemStack(Blocks.PISTON));
	}
}
