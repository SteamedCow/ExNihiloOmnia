package exnihiloomnia.blocks;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.barrels.BlockBarrel;
import exnihiloomnia.blocks.barrels.BlockBarrelGlassColored;
import exnihiloomnia.blocks.barrels.BlockBarrelWood;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.blocks.crucibles.BlockCrucible;
import exnihiloomnia.blocks.crucibles.BlockCrucibleRaw;
import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import exnihiloomnia.blocks.endcake.BlockEndCake;
import exnihiloomnia.blocks.fluids.BlockFluidWitchwater;
import exnihiloomnia.blocks.leaves.BlockInfestedLeaves;
import exnihiloomnia.blocks.leaves.TileEntityInfestedLeaves;
import exnihiloomnia.blocks.misc.BlockChorusSapling;
import exnihiloomnia.blocks.misc.BlockDust;
import exnihiloomnia.blocks.misc.BlockOtherGravel;
import exnihiloomnia.blocks.sieves.BlockSieveWood;
import exnihiloomnia.blocks.sieves.tileentity.TileEntitySieve;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.itemblocks.ItemBarrelGlassColored;
import exnihiloomnia.items.itemblocks.ItemBarrelWood;
import exnihiloomnia.items.itemblocks.ItemInfestedLeaves;
import exnihiloomnia.items.itemblocks.ItemSieveWood;
import kotlin.jvm.JvmStatic;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ENO.MODID)
public class ENOBlocks {
	public static Block BARREL_WOOD = new BlockBarrelWood().setUnlocalizedName("barrel_wood").setRegistryName("barrel_wood");
	public static Block BARREL_STONE;
	public static Block BARREL_GLASS;
	public static Block BARREL_GLASS_COLORED;
	public static Block CRUCIBLE;
	public static Block CRUCIBLE_RAW;
	public static Block GRAVEL_NETHER;
	public static Block GRAVEL_ENDER;
	public static Block DUST;
	public static Block SIEVE_WOOD;
	public static Block INFESTED_LEAVES;
	public static Block WITCHWATER;

	public static Block END_CAKE;
	public static Block CHORUS_SPROUT;

    public static List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();

        blocks.add(SIEVE_WOOD);
        blocks.add(DUST);
        blocks.add(GRAVEL_ENDER);
        blocks.add(GRAVEL_NETHER);

        blocks.add(CRUCIBLE);
        blocks.add(CRUCIBLE_RAW);
        blocks.add(BARREL_GLASS_COLORED);
        blocks.add(BARREL_GLASS);
        blocks.add(BARREL_STONE);
        blocks.add(BARREL_WOOD);
        blocks.add(INFESTED_LEAVES);

        blocks.add(WITCHWATER);

        blocks.add(CHORUS_SPROUT);

        if (ENOConfig.end_cake)
            blocks.add(END_CAKE);

        return blocks;
    }


	public static void init() {
	    BARREL_WOOD = new BlockBarrelWood().setUnlocalizedName("barrel_wood").setRegistryName("barrel_wood");
        BARREL_STONE = new BlockBarrel(Material.ROCK, SoundType.STONE).setUnlocalizedName("barrel_stone").setRegistryName("barrel_stone");
        BARREL_GLASS = new BlockBarrel(Material.GLASS, SoundType.GLASS).setUnlocalizedName("barrel_glass").setRegistryName("barrel_glass");
        BARREL_GLASS_COLORED = new BlockBarrelGlassColored().setUnlocalizedName("barrel_glass_colored").setRegistryName("barrel_glass_colored");
        CRUCIBLE = new BlockCrucible().setUnlocalizedName("crucible").setRegistryName("crucible");
        CRUCIBLE_RAW = new BlockCrucibleRaw().setUnlocalizedName("crucible_raw").setRegistryName("crucible_raw");
        GRAVEL_ENDER = new BlockOtherGravel().setUnlocalizedName("gravel_ender").setRegistryName("gravel_ender").setCreativeTab(ENOItems.ENO_TAB);
        GRAVEL_NETHER = new BlockOtherGravel().setUnlocalizedName("gravel_nether").setRegistryName("gravel_nether").setCreativeTab(ENOItems.ENO_TAB);
        DUST = new BlockDust().setUnlocalizedName("dust").setRegistryName("dust");
        SIEVE_WOOD = new BlockSieveWood().setUnlocalizedName("sieve_wood").setRegistryName("sieve_wood");
        INFESTED_LEAVES = new BlockInfestedLeaves().setUnlocalizedName("infested_leaves").setRegistryName("infested_leaves");

        WITCHWATER = new BlockFluidWitchwater(ENOFluids.WITCHWATER).setUnlocalizedName("witchwater").setRegistryName("witchwater");

        END_CAKE = new BlockEndCake().setUnlocalizedName("end_cake").setRegistryName("end_cake");
        CHORUS_SPROUT = new BlockChorusSapling().setUnlocalizedName("chorus_sprout").setRegistryName("chorus_sprout");

		GameRegistry.registerTileEntity(TileEntityBarrel.class, ENO.MODID + ":tile_entity_barrel");
		GameRegistry.registerTileEntity(TileEntitySieve.class, ENO.MODID + ":tile_entity_sieve");
		GameRegistry.registerTileEntity(TileEntityCrucible.class, ENO.MODID + ":tile_entity_crucible");
		GameRegistry.registerTileEntity(TileEntityInfestedLeaves.class, ENO.MODID + ":tile_entity_infested_leaves");
	}

    @SubscribeEvent
    @JvmStatic
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : getBlocks()) {
            event.getRegistry().register(block);
        }
    }
}
