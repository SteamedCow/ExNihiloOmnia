package exnihiloomnia.crafting.recipes;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CrookRecipes {
	public static void register() {
		ResourceLocation group = new ResourceLocation(ENO.MODID);

		//Crook!
		GameRegistry.addShapedRecipe(ENOItems.CROOK_WOOD.getRegistryName(), group,
						new ItemStack(ENOItems.CROOK_WOOD, 1, 0),
                        "xx",
                        " x",
                        " x",
                        'x', "stickWood");

		//BONE Crook!
		GameRegistry.addShapedRecipe(ENOItems.CROOK_BONE.getRegistryName(), group,
				new ItemStack(ENOItems.CROOK_BONE, 1, 0),
                        "xx",
                        " x",
                        " x",
                        'x', Items.BONE);
	}
}
