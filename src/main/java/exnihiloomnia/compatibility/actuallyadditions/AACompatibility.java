package exnihiloomnia.compatibility.actuallyadditions;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import net.minecraft.item.ItemStack;

public class AACompatibility {
    public static void addCompatibility(ItemStack input, ItemStack output, ItemStack output2) {
        output.setCount(5);
        output2.setCount(2);

        ActuallyAdditionsAPI.addCrusherRecipe(input, output, output2, 30);
    }
}
