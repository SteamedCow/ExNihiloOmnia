package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class DollRecipes {
	public static void register() {
		ResourceLocation group = new ResourceLocation(ENO.MODID);

		//Dolls
		GameRegistry.addShapedRecipe(ENOItems.PORCELAIN_DOLL.getRegistryName(), group,
						new ItemStack(ENOItems.PORCELAIN_DOLL, 1),
                        "xdx",
                        " x ",
                        "x x",
                        'x', ENOItems.PORCELAIN,
                        'd', "gemDiamond");

        GameRegistry.addShapedRecipe(ENOItems.PORCELAIN_DOLL.getRegistryName(), group,
                        new ItemStack(ENOItems.PORCELAIN_DOLL, 1),
                        "xdx",
                        " x ",
                        "x x",
                        'x', ENOItems.PORCELAIN,
                        'd', "gemEmerald");

		GameRegistry.addShapedRecipe(ENOItems.BLAZE_DOLL.getRegistryName(), group,
						new ItemStack(ENOItems.BLAZE_DOLL, 1),
                        "bwb",
                        "gdg",
                        "brb",
						'd', ENOItems.PORCELAIN_DOLL,
                        'b', Items.BLAZE_POWDER,
                        'w', Items.NETHER_WART,
						'g', "dustGlowstone",
						'r', "dustRedstone");

		GameRegistry.addShapedRecipe(ENOItems.END_DOLL.getRegistryName(), group,
						new ItemStack(ENOItems.END_DOLL, 1),
						"bwb",
						"gdg",
						"brb",
						'd', ENOItems.PORCELAIN_DOLL,
						'b', "dyeBlack",
						'w', Items.NETHER_WART,
						'g', new ItemStack(Items.DYE, 1, 4),
						'r', "dustRedstone");
	}
}
