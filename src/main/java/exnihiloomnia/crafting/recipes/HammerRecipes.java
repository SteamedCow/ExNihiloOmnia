package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class HammerRecipes {
	public static void register() {
		ResourceLocation group = new ResourceLocation(ENO.MODID);

		//Hammers!
		GameRegistry.addShapedRecipe(ENOItems.HAMMER_WOOD.getRegistryName(), group,
						new ItemStack(ENOItems.HAMMER_WOOD, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', "plankWood",
                        'y', "stickWood");

		GameRegistry.addShapedRecipe(ENOItems.HAMMER_STONE.getRegistryName(), group,
				new ItemStack(ENOItems.HAMMER_STONE, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Blocks.COBBLESTONE,
                        'y', "stickWood");

		GameRegistry.addShapedRecipe(ENOItems.HAMMER_IRON.getRegistryName(), group,
				new ItemStack(ENOItems.HAMMER_IRON, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Items.IRON_INGOT,
                        'y', "stickWood");

		GameRegistry.addShapedRecipe(ENOItems.HAMMER_GOLD.getRegistryName(), group,
						new ItemStack(ENOItems.HAMMER_GOLD, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Items.GOLD_INGOT,
                        'y', "stickWood");

		GameRegistry.addShapedRecipe(ENOItems.HAMMER_DIAMOND.getRegistryName(), group,
						new ItemStack(ENOItems.HAMMER_DIAMOND, 1, 0),
                        " x ",
                        " yx",
                        "y  ",
                        'x', Items.DIAMOND,
                        'y', "stickWood");
	}
}
