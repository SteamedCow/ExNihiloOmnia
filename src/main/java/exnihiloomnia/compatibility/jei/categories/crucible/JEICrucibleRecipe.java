package exnihiloomnia.compatibility.jei.categories.crucible;

import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class JEICrucibleRecipe implements IRecipeWrapper {
    private final List<ItemStack> input = new ArrayList<>();
    private final List<FluidStack> output = new ArrayList<>();
    private final CrucibleRegistryEntry entry;

    public JEICrucibleRecipe(CrucibleRegistryEntry entry) {
        input.add(new ItemStack(entry.getInput().getBlock(), 1, entry.getInput().getBlock().getMetaFromState(entry.getInput())));

        output.add(new FluidStack(entry.getFluid(), entry.getFluidVolume()));

        this.entry = entry;
    }

    public CrucibleRegistryEntry getEntry() {
        return entry;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutputs(FluidStack.class, output);
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
