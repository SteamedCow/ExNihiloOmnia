package exnihiloomnia.compatibility.jei.categories.compost;

import exnihiloomnia.registries.composting.CompostRegistryEntry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class JEICompostRecipe implements IRecipeWrapper{
    private final ArrayList<ItemStack> input = new ArrayList<>();
    private final ArrayList<ItemStack> outputs = new ArrayList<>();

    private final CompostRegistryEntry entry;

    public JEICompostRecipe(CompostRegistryEntry entry, ItemStack in, ItemStack out) {
        this.entry = entry;

        in.setCount(1000 / entry.getVolume());

        input.add(in);
        outputs.add(out);
    }

    public CompostRegistryEntry getEntry() {
        return entry;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, outputs);
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
