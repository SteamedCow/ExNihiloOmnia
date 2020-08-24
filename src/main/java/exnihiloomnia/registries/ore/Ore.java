package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ore.pojos.POJOre;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class Ore {

    private static int META;

    private int meta;
    private final String name;
    private final int color;
    private final int rarity;
    private final boolean hasGravel;
    private final boolean hasNether;
    private final boolean hasEnd;

    private String parentMod = null;

    private List<String> oreDictNames = new ArrayList<>();

    private ItemStack ingot = null;

    public Ore(String name, Color color, int rarity, boolean hasGravel, boolean hasNether, boolean hasEnd) {
        this.name = name.toLowerCase();
        this.color = color.toInt();
        this.rarity = rarity;
        this.hasGravel = hasGravel;
        this.hasNether = hasNether;
        this.hasEnd = hasEnd;

        this.meta = META++;
    }

    public ResourceLocation getOreName(EnumOreBlockType type) {
        return new ResourceLocation(ENO.MODID + ":ore_" + type.getName() + "_" + this.getName());
    }

    public boolean hasType(EnumOreBlockType type) {
        return  (this.hasGravel && type == EnumOreBlockType.GRAVEL) ||
                (this.hasNether && type == EnumOreBlockType.GRAVEL_NETHER) ||
                (this.hasEnd && type == EnumOreBlockType.GRAVEL_ENDER) ||
                type == EnumOreBlockType.SAND ||
                type == EnumOreBlockType.DUST;
    }

    public Ore setMeta(int meta) {
        this.meta = meta;
        return this;
    }

    public Block getBlock() {
        return OreRegistry.blocks.get(name);
    }

    public void addCrafting() {
        Block oreBlock = getBlock();

        for (EnumOreBlockType type : EnumOreBlockType.values()) {
            if (hasType(type) && type.getCrafting() != null) {
                EnumOreBlockType outType = EnumOreBlockType.fromMetadata(this.meta);
                Item in = type.getCrafting();
                Ingredient ingredient = Ingredient.fromItem(in);

                GameRegistry.addShapelessRecipe(outType.getLocation(), outType.getLocation(), new ItemStack(oreBlock, 1, type.ordinal()),
                        ingredient, ingredient, ingredient, ingredient);
            }
        }
    }

    public void addSmelting() {
        Block oreBlock = getBlock();

        ItemStack output = ingot == null ? new ItemStack(ENOItems.INGOT_ORE, 1, this.meta) : ingot.copy();

        for (EnumOreBlockType type : EnumOreBlockType.values())
            if (hasType(type)) GameRegistry.addSmelting(new ItemStack(oreBlock, 1, type.ordinal()), output, 0);
    }

    public ItemStack getIngot() {
        return ingot;
    }

    public Ore setIngot(Item ingot) {
        if (ingot != null)
            this.ingot = new ItemStack(ingot);
        return this;
    }

    public Ore setIngot(Item ingot, int meta) {
        if (ingot != null)
            this.ingot = new ItemStack(ingot, 1, meta);
        return this;
    }

    public Ore setParentMod(String modid) {
        this.parentMod = modid;
        return this;
    }

    public String getParentMod() {
        return parentMod;
    }

    public List<String> getOreDictNames() {
        return oreDictNames;
    }

    public Ore setOreDictNames(List<String> oreDictNames) {
        this.oreDictNames = oreDictNames;
        return this;
    }

    public Ore addOreDictName(String name) {
        this.oreDictNames.add(name);
        return this;
    }

    public String getOreDictName(String pre) {
        String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);
        return pre + name1;
    }

    public int getMetadata() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getRarity() {
        return rarity;
    }

    public boolean hasGravel() {
        return hasGravel;
    }

    public boolean hasNether() {
        return hasNether;
    }

    public boolean hasEnd() {
        return hasEnd;
    }

    public static Ore fromPOJOre(POJOre ore) {
        String name = ore.getName();
        Color color = new Color(ore.getColor());
        int rarity = ore.getRarity();
        boolean hasGravel = ore.isHasGravel();
        boolean hasNether = ore.isHasNether();
        boolean hasEnd = ore.isHasEnd();

        List<String> oreDictNames = ore.getOreDictNames();

        Item ingot = Item.REGISTRY.getObject(new ResourceLocation(ore.getIngot()));

        Ore ret = new Ore(name, color, rarity, hasGravel, hasNether, hasEnd);

        ret.setIngot(ingot, ore.getIngotMeta());

        if (oreDictNames.size() > 0)
            ret.setOreDictNames(oreDictNames);

        return ret;
    }

    public POJOre toPOJOre() {
        POJOre ret = new POJOre();

        ret.setOreDictNames(this.oreDictNames)
        .setColor(new Color(this.color).toString().toUpperCase())
        .setName(this.name)
        .setHasGravel(this.hasGravel)
        .setHasNether(this.hasNether)
        .setHasEnd(this.hasEnd)
        .setRarity(this.rarity);

        if (this.ingot != null) {
            ret.setIngot(Item.REGISTRY.getNameForObject(this.ingot.getItem()).toString());
            ret.setIngotMeta(this.ingot.getItemDamage());
        }

        return ret;
    }
}
