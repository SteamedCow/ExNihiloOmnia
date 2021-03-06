package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.registries.WeightedRewards
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.JsonUtils
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.registries.IForgeRegistryEntry

class SieveRecipe(
        /**
         * What you have to put in the sieve
         */
        val input: Ingredient,

        /**
         * The amount of time in ticks it takes to sift the input.
         * Default is 40 ticks, or 2 seconds
         */
        val siftTime: Int,

        /**
         * What could drop when fully processed.
         */
        val rewards: WeightedRewards
) : IForgeRegistryEntry.Impl<SieveRecipe>() {

    fun matches(input: ItemStack): Boolean {
        return this.input.apply(input)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        fun deserialize(recipe: JsonObject): SieveRecipe? {
            if (LibRegistries.INPUT !in recipe) throw JsonSyntaxException("sieve recipe has no input")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS, RegistryLoader.CONTEXT)) return null

            val input = CraftingHelper.getIngredient(recipe.get(LibRegistries.INPUT), RegistryLoader.CONTEXT)
            val siftTime = JsonUtils.getInt(recipe, LibRegistries.TIME, 40)

            RegistryLoader.pushCtx(LibRegistries.REWARDS)
            val output = WeightedRewards.deserialize(JsonUtils.getJsonArray(recipe, LibRegistries.REWARDS))
            RegistryLoader.popCtx()

            return SieveRecipe(input, siftTime, output)
        }
    }
}
